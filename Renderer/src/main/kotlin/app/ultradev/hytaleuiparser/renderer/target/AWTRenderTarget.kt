package app.ultradev.hytaleuiparser.renderer.target

import app.ultradev.hytaleuiparser.renderer.NineSlice
import app.ultradev.hytaleuiparser.renderer.RenderBox
import app.ultradev.hytaleuiparser.renderer.render.RenderImage
import app.ultradev.hytaleuiparser.renderer.text.TextRenderMode
import app.ultradev.hytaleuiparser.renderer.text.TextRenderStyle
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.RenderingHints
import java.awt.image.BufferedImage

class AWTRenderTarget(val graphics: Graphics) : RenderTarget {
    override val box: RenderBox
        get() = graphics.clipBounds.let { RenderBox(it.x, it.y, it.width, it.height) }

    private var offsetX = 0
    private var offsetY = 0

    init {
        if (graphics is Graphics2D) {
            // Enable antialiasing for text
            graphics.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON
            )
            graphics.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON
            )
        }
    }

    override fun renderImage(
        image: RenderImage,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        horizontalBorder: Int,
        verticalBorder: Int
    ) {
        if (width == 0 || height == 0) return
        val scaled = NineSlice.scale(
            image.image,
            width * image.scale, height * image.scale,
            horizontalBorder * image.scale, verticalBorder * image.scale,
            horizontalBorder * image.scale, verticalBorder * image.scale
        )
        val correctSize = if (image.scale != 1) {
            scaled.getScaledInstance(scaled.width / image.scale, scaled.height / image.scale, BufferedImage.SCALE_FAST)
        } else scaled
        graphics.drawImage(correctSize, x + offsetX, y + offsetY, null)
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

        val alignments = info.calculateAlignment(box.shift(offsetX, offsetY), lines)
        lines.zip(alignments).forEach { (line, coord) ->
            when (TextRenderMode.active) {
                TextRenderMode.TTF -> {
                    graphics.font = info.font
                    graphics.drawString(line, coord.first, coord.second)
                }
                TextRenderMode.MSDF -> info.msdfFont.drawString(graphics, info.fontSize, coord.first, coord.second, line)
            }
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