package app.ultradev.hytaleuiparser.renderer.target

import app.ultradev.hytaleuiparser.renderer.type.RenderBox
import app.ultradev.hytaleuiparser.renderer.render.RenderImage
import app.ultradev.hytaleuiparser.renderer.text.TextRenderStyle
import java.awt.Color

interface RenderTarget {
    val windowBounds: RenderBox

    fun renderImage(
        image: RenderImage,
        box: RenderBox,
        horizontalBorder: Int = 0,
        verticalBorder: Int = 0
    )

    fun renderFill(color: Color, box: RenderBox)

    fun renderText(
        text: String,
        box: RenderBox,
        info: TextRenderStyle,
    )

    fun setClip(box: RenderBox?)
}