package app.ultradev.hytaleuiparser.renderer.text

import app.ultradev.hytaleuiparser.generated.types.LabelStyle
import java.awt.Font
import java.awt.RenderingHints
import java.awt.font.FontRenderContext
import java.awt.font.TextAttribute
import kotlin.math.roundToInt

data class TextRenderInfo(
    val fontName: String,
    val fontSize: Float,
    val bold: Boolean,
    val italics: Boolean,
    val underlined: Boolean,
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

    companion object {
        fun fromLabelStyle(style: LabelStyle): TextRenderInfo {
            return TextRenderInfo(
                style.fontName ?: "Default",
                style.fontSize ?: 16f,
                style.renderBold ?: false,
                style.renderItalics ?: false,
                style.renderUnderlined ?: false,
            )
        }
    }
}
