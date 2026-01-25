package app.ultradev.hytaleuiparser.validation

import app.ultradev.hytaleuiparser.validation.types.TypeType

object Elements {
    val COMMON_PROPERTIES = mapOf(
        "Anchor" to TypeType.Anchor,
        "Padding" to TypeType.Padding,
        "Background" to TypeType.PatchStyle,
        "FlexWeight" to TypeType.Integer,
        "HitTestVisible" to TypeType.Boolean,
        "Visible" to TypeType.Boolean,
        "LayoutMode" to TypeType.LayoutMode,
        "TooltipText" to TypeType.String,
        "TextTooltipStyle" to TypeType.TextTooltipStyle,
        "TooltipTextSpans" to TypeType.String,
    )

    val BUTTON_PROPERTIES = mapOf(
        "Disabled" to TypeType.Boolean,
    )
}