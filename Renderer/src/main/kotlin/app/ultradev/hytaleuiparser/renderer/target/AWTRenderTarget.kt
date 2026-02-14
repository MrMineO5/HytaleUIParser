package app.ultradev.hytaleuiparser.renderer.target

import app.ultradev.hytaleuiparser.renderer.NineSlice
import app.ultradev.hytaleuiparser.renderer.RenderBox
import app.ultradev.hytaleuiparser.renderer.text.TextRenderStyle
import java.awt.Color
import java.awt.Graphics
import java.awt.image.BufferedImage

class AWTRenderTarget(val graphics: Graphics) : RenderTarget {
    override val box: RenderBox
        get() = graphics.clipBounds.let { RenderBox(it.x, it.y, it.width, it.height) }

    override fun renderImage(
        image: BufferedImage,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        horizontalBorder: Int,
        verticalBorder: Int
    ) {
        if (width == 0 || height == 0) return
        val scaled = NineSlice.scale(
            image,
            width, height,
            horizontalBorder, verticalBorder,
            horizontalBorder, verticalBorder
        )
        graphics.drawImage(scaled, x, y, null)
    }

    override fun renderFill(color: Color, x: Int, y: Int, width: Int, height: Int) {
        graphics.color = color
        graphics.fillRect(x, y, width, height)
    }

    override fun renderText(text: String, box: RenderBox, color: Color, info: TextRenderStyle) {
        val (tx, ty) = info.calculateAlignment(box, text)

        graphics.color = color
        graphics.font = info.font
        graphics.drawString(text, tx, ty)
    }
}