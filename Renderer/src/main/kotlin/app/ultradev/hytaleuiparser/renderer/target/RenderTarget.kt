package app.ultradev.hytaleuiparser.renderer.target

import app.ultradev.hytaleuiparser.renderer.RenderBox
import app.ultradev.hytaleuiparser.renderer.render.RenderImage
import app.ultradev.hytaleuiparser.renderer.text.TextRenderStyle
import java.awt.Color
import java.awt.image.BufferedImage

interface RenderTarget {
    val box: RenderBox

    fun renderImage(
        image: RenderImage,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        horizontalBorder: Int = 0,
        verticalBorder: Int = 0
    )

    fun renderFill(color: Color, x: Int, y: Int, width: Int, height: Int)

    fun renderText(
        text: String,
        box: RenderBox,
        info: TextRenderStyle,
    )

    fun setClip(box: RenderBox?): RenderBox?
    fun setOffset(x: Int, y: Int): Pair<Int, Int>
}