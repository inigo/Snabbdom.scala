package com.raquo

import com.raquo.snabbdom.collections.Builders
import com.raquo.snabbdom.collections.attrs.{Attrs, GlobalAttrs, InputAttrs}
import com.raquo.snabbdom.collections.eventProps.{ClipboardEventProps, FormEventProps, KeyboardEventProps, MouseEventProps}
import com.raquo.snabbdom.collections.props.Props
import com.raquo.snabbdom.collections.styles.Styles
import com.raquo.snabbdom.collections.tags.{Tags, Tags2}
import com.raquo.snabbdom.hooks.ModuleHooks
import com.raquo.snabbdom.nodes.{ChildNode, Conversions, IterableNode, Node, NodeData}
import com.raquo.snabbdom.setters.KeyKey
import org.scalajs.dom.MouseEvent
import org.scalajs.dom.raw.Event

import scala.scalajs.js
import scala.scalajs.js.|

package object snabbdom extends {

  type EventCallback[TEvent <: Event] = js.Function1[TEvent, Unit]

  type GenericEventCallback = EventCallback[Event]

  type MouseEventCallback = EventCallback[MouseEvent]

  implicit val vnodeBuilders = new VNodeBuilders {}

  /** These are snabbdom's built-in modules that Snabbdom.scala supports.
    * You may choose to pass more modules to [[Snabbdom.init]] (see [[ModuleHooks]]),
    * and you might want to subclass [[Node]], [[NodeData]] and [[Builders]] if you do that.
    * For an example of this approach, see my Laminar project.
    *
    * Note that if you fail to include any of the following built-in modules
    * in the init call, the types will not be adjusted to reflect their absence.
    * I will try to fix that eventually.
    */
  val modules: js.Array[NativeModule | ModuleHooks[VNode, VNodeData]] = js.Array(
    AttrsModule,
    PropsModule,
    EventsModule,
    StyleModule
  )

  object tags extends Tags[VNode, VNodeData] with VNodeBuilders

  object allTags extends Tags[VNode, VNodeData] with Tags2[VNode, VNodeData] with VNodeBuilders

  object attrs extends Attrs[VNode, VNodeData] with InputAttrs[VNode, VNodeData] with GlobalAttrs[VNode, VNodeData] with VNodeBuilders

  object props extends Props[VNode, VNodeData] with VNodeBuilders // @TODO add more `with`?

  object events
    extends MouseEventProps[VNode, VNodeData]
    with FormEventProps[VNode, VNodeData]
    with KeyboardEventProps[VNode, VNodeData]
    with ClipboardEventProps[VNode, VNodeData]
    with VNodeBuilders

  object styles extends Styles[VNode, VNodeData] with VNodeBuilders

  /** Setter of snabbdom's special key property */
  val key = new KeyKey[VNode, VNodeData]

  @inline implicit def textToChildNode(
    text: String
  ): ChildNode[VNode, VNodeData] = {
    Conversions.textToChildNode[VNode, VNodeData](text)(vnodeBuilders)
  }

  @inline implicit def nodeToChildNode(
    vnode: VNode
  ): ChildNode[VNode, VNodeData] = {
    Conversions.nodeToChildNode[VNode, VNodeData](vnode)(vnodeBuilders)
  }

  @inline implicit def toIterableNode(
    modifiers: Iterable[Modifier[VNode, VNodeData]]
  ): IterableNode[VNode, VNodeData] = {
    Conversions.toIterableNode[VNode, VNodeData](modifiers)
  }

  @inline implicit def optionToModifier[T](
    maybeModifier: Option[T]
  )(
    implicit toModifier: T => Modifier[VNode, VNodeData]
  ): Modifier[VNode, VNodeData] = {
    Conversions.optionToModifier[T, VNode, VNodeData](maybeModifier)
  }
}
