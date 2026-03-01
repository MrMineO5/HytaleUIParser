package app.ultradev.hytaleuiparser.renderer.target

import app.ultradev.hytaleuiparser.renderer.RenderBox
import app.ultradev.hytaleuiparser.renderer.render.RenderImage
import app.ultradev.hytaleuiparser.renderer.text.TextRenderStyle
import java.awt.Color
import java.awt.image.BufferedImage

object NopRenderTarget : RenderTarget {
    override val box: RenderBox
        get() = RenderBox(0, 0, 1, 1)

    override fun renderImage(
        image: RenderImage,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        horizontalBorder: Int,
        verticalBorder: Int
    ) {
    }

    override fun renderFill(
        color: Color,
        x: Int,
        y: Int,
        width: Int,
        height: Int
    ) {
    }

    override fun renderText(
        text: String,
        box: RenderBox,
        info: TextRenderStyle
    ) {
    }

    override fun setClip(box: RenderBox?): RenderBox? = box
    override fun setOffset(x: Int, y: Int): Pair<Int, Int> = x to y
}