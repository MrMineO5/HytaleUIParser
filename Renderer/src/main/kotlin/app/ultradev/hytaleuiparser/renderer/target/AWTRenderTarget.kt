package app.ultradev.hytaleuiparser.renderer.target

import app.ultradev.hytaleuiparser.renderer.NineSlice
import app.ultradev.hytaleuiparser.renderer.type.RenderBox
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
    override val windowBounds: RenderBox
        get() = graphics.clipBounds.let { RenderBox(it.x, it.y, it.width, it.height) }

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
        box: RenderBox,
        horizontalBorder: Int,
        verticalBorder: Int
    ) {
        if (box.isEmpty()) return
        val scaled = NineSlice.scale(
            image.image,
            box.width * image.scale, box.height * image.scale,
            horizontalBorder * image.scale, verticalBorder * image.scale,
            horizontalBorder * image.scale, verticalBorder * image.scale
        )
        val correctSize = if (image.scale != 1) {
            scaled.getScaledInstance(scaled.width / image.scale, scaled.height / image.scale, BufferedImage.SCALE_FAST)
        } else scaled
        graphics.drawImage(correctSize, box.x, box.y, null)
    }

    override fun renderFill(color: Color, box: RenderBox) {
        graphics.color = color
        graphics.fillRect(box.x, box.y, box.width, box.height)
    }

    override fun renderText(text: String, box: RenderBox, info: TextRenderStyle) {
        var textToDraw = text
        if (info.uppercase) textToDraw = textToDraw.uppercase()
        val lines = if (info.wrap) {
            info.wrap(text, box.width)
        } else textToDraw.split("\n")

        graphics.color = info.color

        val alignments = info.calculateAlignment(box, lines)
        lines.zip(alignments).forEach { (line, coord) ->
            when (TextRenderMode.active) {
                TextRenderMode.TTF -> {
                    graphics.font = info.font
                    graphics.drawString(line, coord.first.toInt(), coord.second.toInt())
                }
                TextRenderMode.MSDF -> info.msdfFont.drawString(graphics, info.fontSize, coord.first.toInt(), coord.second.toInt(), line)
            }
        }
    }

    override fun setClip(box: RenderBox?) {
        graphics.clip = box?.let { Rectangle(it.x, it.y, it.width, it.height) }
    }
}