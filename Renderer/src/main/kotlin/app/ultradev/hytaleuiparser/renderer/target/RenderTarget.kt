package app.ultradev.hytaleuiparser.renderer.target

import app.ultradev.hytaleuiparser.renderer.RenderBox
import app.ultradev.hytaleuiparser.renderer.text.TextRenderStyle
import java.awt.Color
import java.awt.image.BufferedImage

interface RenderTarget {
    val box: RenderBox

    fun renderImage(
        image: BufferedImage,
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
        color: Color,
        info: TextRenderStyle,
    )
}