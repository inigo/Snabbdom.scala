package com.raquo.snabbdom.utils.testing.matching

import com.raquo.snabbdom.Snabbdom.PatchFn
import com.raquo.snabbdom.collections.Builders
import com.raquo.snabbdom.hooks.{ModuleHooks, NodeHooks}
import com.raquo.snabbdom.{NativeModule, Snabbdom}
import com.raquo.snabbdom.nodes.{Node, NodeData}
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.|

trait MountOps[N <: Node[N, D], D <: NodeData[N, D]] { this: Builders[N, D] =>

  var container: dom.Element = null

  var mountedNode: N = _

  private[this] var jsPatch: PatchFn[N, D] = noopPatchFn _

  val snabbdomModules: js.Array[NativeModule | ModuleHooks[N, D]]

  val defaultMountedElementClue = "root"

  var mountedElementClue: String = defaultMountedElementClue

  // @TODO[API] improve names - we use `do` prefix because `assert` and `fail` are taken by ScalaTest

  def doAssert(condition: Boolean, message: String): Unit

  def doFail(message: String): Nothing

  def expectNode(expectedNode: ExpectedNode[N, D]): Unit = {
    val errors = expectedNode.checkNode(
      mountedElement,
      clue = mountedElementClue
    )
    if (errors.nonEmpty) {
      doFail(s"Rendered element does not match expectations:\n${errors.mkString("\n")}")
    }
  }

  def initSnabbdom(): PatchFn[N, D] = Snabbdom.init(snabbdomModules)

  def clearDocument(): Unit = {
    removeChildren(dom.document.body)
    jsPatch = noopPatchFn _
  }

  def resetDocument(): Unit = {
    clearDocument()
    container = createContainer()
    dom.document.body.appendChild(container)
    jsPatch = initSnabbdom()
  }

  def mountedElement: dom.Element = {
    val numMountedElements = container.childNodes.length
    if (numMountedElements == 0) {
      doFail(s"ASSERT FAIL [mountedElement]: container has no children. Did you forget to call mount()? Or maybe you've unmounted or cleared the document since then?")
    } else if (numMountedElements > 1) {
      doFail(s"ASSERT FAIL [mountedElement]: container must have exactly 1 child, $numMountedElements found. Did you forget to unmount() or clear the document?")
    }

    container.firstChild match {
      case element: dom.Element => element
      case node =>
        doFail(s"ASSERT FAIL [mountedElement]: mounted node $node is not an Element")
    }
  }

  def mount(clue: String, node: N): Unit = {
    mount(node, clue)
  }

  def mount(node: N, clue: String = defaultMountedElementClue): Unit = {
    doAssert(
      container != null && container.parentNode == dom.document.body,
      "ASSERT FAIL [mount]: Container is null or not mounted to <body> (what did you do!?)"
    )
    // @TODO Looks like this assert is somehow triggering this Scala.js bug? @see https://github.com/scala-js/scala-js/issues/2712
    // @TODO Or maybe something else is going on? Revisit when Scala.js 0.6.15 is released
    // @TODO: Note: this bug has been manifesting in Laminar unit tests, not Snabbdom's own tests
    //    assert(
    //      container.firstChild == null,
    //      "ASSERT FAIL [mount]: Unexpected children in container. Call unmount() before mounting again."
    //    )

    val entry = dom.document.createElement("div")
    entry.setAttribute("id", "snabbdom-entry")

    //    val initialNode = dom.document.createElement("div")
    //    entry.appendChild(initialNode)

    mountedElementClue = clue
    container.appendChild(entry)

    patch(entry, node)
  }

  def patch(nodeOrDOMElement: N | dom.Element, newNode: N): N = {

    // When a patch happens on a mounted node (e.g. because its AttrReceiver patches it)
    // we get a new VNode reference that we need to save if we want to properly unmount it later.
    // @TODO This looks a bit dangerous – is it safe around mount() / unmount() and async operations? Do we need any assert()-s?
    if (newNode.data.hooks.isEmpty) {
      newNode.data.hooks = new NodeHooks[N, D]()
    }
    newNode.data.hooks.get.addPostPatchHook((oldNode: N, newNode: N) => {
//      dom.console.log("updating patched node")
      mountedNode = newNode
    })

    mountedNode = jsPatch(nodeOrDOMElement, newNode)
    mountedNode
  }

  def patchMounted(newNode: N): N = {
    doAssert(
      mountedNode != null,
      "ASSERT FAIL [patchMounted]: Nothing to patch. You need to mount() before trying to patchMounted()."
    )
    patch(mountedNode, newNode)
  }

  /** Ensure that any previously used snabbdom node is destroyed before we attempt to mount a new node */
  def unmount(): Unit = {
    doAssert(
      mountedNode != null,
      "ASSERT FAIL [unmount]: Nothing to unmount. You need to mount() before trying to unmount()"
    )
    patchMounted(node("div"))
    removeChildren(container)
  }

  private def createContainer(): dom.Element = {
    val container = dom.document.createElement("div")
    container.setAttribute("id", "snabbdom-container")
    container
  }

  private def removeChildren(node: dom.Element): Unit = {
    while (node.firstChild != null) {
      node.removeChild(node.firstChild)
    }
  }

  private def noopPatchFn(nodeOrDOMElement: N | dom.Element, newNode: N): N = {
    doFail("Patch function is not defined in the test suite. Somehow, resetDocument() was not called. Normally this is done automatically in beforeEach()")
  }
}
