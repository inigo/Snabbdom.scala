package com.raquo.snabbdom.utils.testing.matching

import com.raquo.snabbdom.setters.Prop
import com.raquo.snabbdom.utils.testing.UtilSpec.repr
import org.scalajs.dom

import scala.scalajs.js

class PropRuleOps[V](val prop: Prop[V]) extends AnyVal {

  def is(expected: V): Rule = new Rule {
    def applyTo(testNode: ExpectedElement): Unit = {
      testNode.addCheck(nodePropIs(prop, Some(expected)))
    }
  }

  def isEmpty: Rule = new Rule {
    def applyTo(testNode: ExpectedElement): Unit = {
      testNode.addCheck(nodePropIs(prop, None))
    }
  }

  private def nodePropIs(prop: Prop[V], maybeExpectedValue: Option[V])(node: dom.Node): MaybeError = {
    val maybeActualValue = getProp(node, prop)
    if (node.isInstanceOf[dom.Element]) {
      (maybeActualValue, maybeExpectedValue) match {
        case (None, None) => None
        case (None, Some(expectedValue)) =>
          Some(s"Prop `${prop.name}` is missing, expected ${repr(expectedValue)}")
        case (Some(actualValue), None) =>
          Some(s"Prop `${prop.name}` should not be present: actual value ${repr(actualValue)}, expected to be missing")
        case (Some(actualValue), Some(expectedValue)) =>
          if (actualValue != expectedValue) {
            Some(s"Prop `${prop.name}` value is incorrect: actual value ${repr(actualValue)}, expected value ${repr(expectedValue)}")
          } else {
            None
          }
      }
    } else {
      Some(s"Unable to verify Prop `${prop.name}` because node $node is not a DOM Element (might be a text node?)")
    }
  }

  private def getProp(element: dom.Node, prop: Prop[V]): Option[V] = {
    val propValue = element.asInstanceOf[js.Dynamic].selectDynamic(prop.name)
    val jsUndef = js.undefined
    propValue.asInstanceOf[Any] match {
      case str: String if str.length == 0 => None
      case `jsUndef` => None
      case null => None
      case _ => Some(propValue.asInstanceOf[V])
    }
  }
}