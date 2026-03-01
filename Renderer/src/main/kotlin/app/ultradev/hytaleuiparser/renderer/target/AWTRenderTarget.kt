package app.ultradev.hytaleuiparser.renderer.target

import app.ultradev.hytaleuiparser.renderer.NineSlice
import app.ultradev.hytaleuiparser.renderer.RenderBox
import app.ultradev.hytaleuiparser.renderer.text.TextRenderStyle
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.RenderingHints
import java.awt.font.FontRenderContext
import java.awt.image.BufferedImage

class AWTRenderTarget(val graphics: Graphics) : RenderTarget {
    override val box: RenderBox
        get() = graphics.clipBounds.let { RenderBox(it.x, it.y, it.width, it.height) }

    private val fontRenderContext: FontRenderContext

    private var offsetX = 0
    private var offsetY = 0

    init {
        if (graphics is Graphics2D) {
            // Enable antialiasing for text
            graphics.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON
            )
            fontRenderContext = graphics.fontRenderContext
        } else {
            fontRenderContext = FontRenderContext(null, false, false)
        }
    }

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
        graphics.drawImage(scaled, x + offsetX, y + offsetY, null)
    }

    override fun renderFill(color: Color, x: Int, y: Int, width: Int, height: Int) {
        graphics.color = color
        graphics.fillRect(x + offsetX, y + offsetY, width, height)
    }

    override fun renderText(text: String, box: RenderBox, info: TextRenderStyle) {
        var textToDraw = text
        if (info.uppercase) textToDraw = textToDraw.uppercase()
        val lines = if (info.wrap) {
            info.wrap(text, box.width)
        } else textToDraw.split("\n")

        graphics.color = info.color
        graphics.font = info.font

        val alignments = info.calculateAlignment(box.shift(offsetX, offsetY), lines)
        lines.zip(alignments).forEach { (line, coord) ->
            graphics.drawString(line, coord.first, coord.second)
        }
    }

    override fun setClip(box: RenderBox?): RenderBox? {
        val old = graphics.clipBounds?.let { RenderBox(it.x, it.y, it.width, it.height) }
        graphics.clip = box?.let { Rectangle(it.x, it.y, it.width, it.height) }
        return old
    }

    override fun setOffset(x: Int, y: Int): Pair<Int, Int> {
        val oldX = offsetX
        val oldY = offsetY
        offsetX = x
        offsetY = y
        return oldX to oldY
    }
}