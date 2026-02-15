package app.ultradev.hytaleuiparser.renderer.text

import app.ultradev.hytaleuiparser.generated.types.InputFieldStyle
import app.ultradev.hytaleuiparser.generated.types.LabelAlignment
import app.ultradev.hytaleuiparser.generated.types.LabelStyle
import app.ultradev.hytaleuiparser.renderer.RenderBox
import java.awt.Color
import java.awt.Font
import java.awt.font.FontRenderContext
import java.awt.font.TextAttribute
import java.awt.geom.Rectangle2D
import kotlin.math.roundToInt

data class TextRenderStyle(
    val fontName: String,
    val fontSize: Float,
    val color: Color,
    val bold: Boolean,
    val italics: Boolean,
    val underlined: Boolean,

    val horizontalAlignment: LabelAlignment,
    val verticalAlignment: LabelAlignment,
) {
    val font by lazy { toFont() }
    private fun toFont(): Font {
        val baseFont = when (fontName) {
            "Default" -> GameFonts.default
            "Secondary" -> GameFonts.secondary
            else -> error("Unknown font: $fontName")
        }

        val attrs = mutableMapOf<TextAttribute, Any>()
        attrs[TextAttribute.SIZE] = fontSize
        attrs[TextAttribute.WEIGHT] = if (bold) TextAttribute.WEIGHT_BOLD else TextAttribute.WEIGHT_REGULAR
        attrs[TextAttribute.POSTURE] = if (italics) TextAttribute.POSTURE_OBLIQUE else TextAttribute.POSTURE_REGULAR

        return baseFont.deriveFont(attrs)
    }

    fun getWidth(context: FontRenderContext, text: String): Int {
        return font.getStringBounds(
            text,
            context
        ).let { it.width + it.x }.roundToInt()
    }
    fun getBounds(context: FontRenderContext, text: String): Rectangle2D {
        return font.getStringBounds(
            text,
            context
        )
    }

    fun calculateAlignment(box: RenderBox, text: String): Pair<Int, Int> {
        val bounds = getBounds(FontRenderContext(null, false, false), text)

        val renderX = when (horizontalAlignment) {
            LabelAlignment.Start -> box.x - bounds.x
            LabelAlignment.Center -> box.x - bounds.x + (box.width - bounds.width) / 2
            LabelAlignment.End -> box.x - bounds.x + box.width - bounds.width
        }.toInt()
        val renderY = when (verticalAlignment) {
            LabelAlignment.Start -> box.y - bounds.y
            LabelAlignment.Center -> box.y - bounds.y + (box.height - bounds.height) / 2
            LabelAlignment.End -> box.y - bounds.y + box.height - bounds.height
        }.toInt()

        return renderX to renderY
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
            )
        }
    }
}
