package com.raquo.snabbdom

import com.raquo.snabbdom.tags.div
import com.raquo.snabbdom.styles._

import scala.util.Random

class StyleSpec extends UnitSpec {

  it("sets styles") {
    val expectedDisplay = "block"
    val expectedHeight = s"${Random.nextInt(15)}px"
    val expectedWidth = s"${15 + Random.nextInt(7)}px"

    mount("div", div(display.block))
    expectNode(div like (display is expectedDisplay))
    unmount()

    mount("div [width, height]", div(
      width := expectedWidth,
      height := expectedHeight
    ))
    expectNode(
      div like(
        width is expectedWidth,
        height is expectedHeight
      )
    )
    unmount()
  }

}
