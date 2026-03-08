package app.ultradev.hytaleuiparser.renderer.target

import app.ultradev.hytaleuiparser.renderer.type.RenderBox
import app.ultradev.hytaleuiparser.renderer.render.RenderImage
import app.ultradev.hytaleuiparser.renderer.text.TextRenderStyle
import java.awt.Color

object NopRenderTarget : RenderTarget {
    override val windowBounds: RenderBox
        get() = RenderBox(0, 0, 1, 1)

    override fun renderImage(
        image: RenderImage,
        box: RenderBox,
        horizontalBorder: Int,
        verticalBorder: Int
    ) {
    }

    override fun renderFill(
        color: Color,
        box: RenderBox
    ) {
    }

    override fun renderText(
        text: String,
        box: RenderBox,
        info: TextRenderStyle
    ) {
    }

    override fun setClip(box: RenderBox?) {}
}