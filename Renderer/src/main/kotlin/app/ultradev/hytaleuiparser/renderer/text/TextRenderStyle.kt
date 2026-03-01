package app.ultradev.hytaleuiparser.renderer.text

import app.ultradev.hytaleuiparser.generated.types.InputFieldStyle
import app.ultradev.hytaleuiparser.generated.types.LabelAlignment
import app.ultradev.hytaleuiparser.generated.types.LabelStyle
import app.ultradev.hytaleuiparser.renderer.RenderBox
import java.awt.Color
import java.awt.Font
import java.awt.Toolkit
import java.awt.font.TextAttribute

data class TextRenderStyle(
    val fontName: String,
    val fontSize: Float,
    val color: Color,
    val bold: Boolean,
    val italics: Boolean,
    val underlined: Boolean,

    val horizontalAlignment: LabelAlignment,
    val verticalAlignment: LabelAlignment,

    val uppercase: Boolean,
    val wrap: Boolean,
) {
    val font by lazy { toFont() }
    val fontMetrics by lazy { Toolkit.getDefaultToolkit().getFontMetrics(font) }

    private fun toFont(): Font {
        val baseFont = when (fontName) {
            "Default" -> {
                if (bold) GameFonts.defaultBold else GameFonts.default
            }

            "Secondary" -> GameFonts.secondary
            else -> error("Unknown font: $fontName")
        }

        val attrs = mutableMapOf<TextAttribute, Any>()
        attrs[TextAttribute.SIZE] = fontSize
        if (italics) attrs[TextAttribute.POSTURE] = TextAttribute.POSTURE_OBLIQUE

        return baseFont.deriveFont(attrs)
    }

    fun wrap(text: String, maxWidth: Int): List<String> {
        if (text.contains("\n")) return text.split("\n").flatMap { wrap(it, maxWidth) }
        val words = text.split(" ")
        val lines = mutableListOf<String>()
        var currentLine = ""

        words.forEach { word ->
            val newLine = currentLine.let {
                if (it.isNotEmpty()) "$it " else it
            } + word
            if (getWidth(newLine) > maxWidth) {
                if (currentLine.isNotEmpty()) {
                    lines.add(currentLine)
                    currentLine = word
                } else {
                    currentLine = word
                    while (getWidth(currentLine) > maxWidth) {
                        var leftIndex = 0
                        var rightIndex = currentLine.length
                        while (leftIndex < rightIndex) {
                            val midIndex = (leftIndex + rightIndex) / 2
                            val midWord = currentLine.substring(0, midIndex)
                            val midWidth = getWidth(midWord)
                            if (midWidth > maxWidth) {
                                rightIndex = midIndex
                            } else {
                                leftIndex = midIndex + 1
                            }
                        }
                        val midIndex = (leftIndex + rightIndex) / 2
                        val midWord = currentLine.substring(0, midIndex)
                        currentLine = currentLine.substring(midIndex)
                        lines.add(midWord)
                    }
                }
            } else {
                currentLine = newLine
            }
        }
        if (currentLine.isNotEmpty()) {
            lines.add(currentLine)
        }
        return lines
    }

    fun getWidth(text: String): Int {
        return fontMetrics.stringWidth(text)
    }

    fun getHeight(): Int {
        // TODO: So weird this is necessary
        return fontMetrics.height - 1
    }

    fun calculateAlignment(box: RenderBox, text: List<String>): List<Pair<Int, Int>> {
        val totalHeight = getHeight() * text.size

        val startY = when (verticalAlignment) {
            LabelAlignment.Start -> box.y
            LabelAlignment.Center -> box.y + (box.height - totalHeight) / 2
            LabelAlignment.End -> box.y + box.height - totalHeight
        } + fontMetrics.ascent

        return text.mapIndexed { index, line ->
            val width = getWidth(line)
            val renderX = when (horizontalAlignment) {
                LabelAlignment.Start -> box.x
                LabelAlignment.Center -> box.x + (box.width - width) / 2
                LabelAlignment.End -> box.x + box.width - width
            }

            renderX to startY + index * getHeight()
        }
    }

    companion object {
        fun fromLabelStyle(style: LabelStyle): TextRenderStyle {
            return TextRenderStyle(
                style.fontName ?: "Default",
                style.fontSize ?: 16f,
                style.textColor ?: Color.WHITE,
                style.renderBold ?: false,
                style.renderItalics ?: false,
                style.renderUnderlined ?: false,
                style.horizontalAlignment ?: style.alignment ?: LabelAlignment.Start,
                style.verticalAlignment ?: style.alignment ?: LabelAlignment.Start,
                style.renderUppercase ?: false,
                style.wrap ?: false,
            )
        }

        fun fromInputFieldStyle(style: InputFieldStyle): TextRenderStyle {
            return TextRenderStyle(
                style.fontName ?: "Default",
                style.fontSize ?: 16f,
                style.textColor ?: Color.WHITE,
                style.renderBold ?: false,
                style.renderItalics ?: false,
                false,
                LabelAlignment.Start,
                LabelAlignment.Center,
                style.renderUppercase ?: false,
                false,
            )
        }
    }
}
