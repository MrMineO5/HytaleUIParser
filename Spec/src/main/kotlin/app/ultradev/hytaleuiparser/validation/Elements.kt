package app.ultradev.hytaleuiparser.validation

import app.ultradev.hytaleuiparser.validation.types.TypeType

object Elements {
    val COMMON_PROPERTIES = mapOf(
        "Anchor" to TypeType.Anchor,
        "Padding" to TypeType.Padding,
        "Background" to TypeType.PatchStyle,
        "FlexWeight" to TypeType.Int32,
        "HitTestVisible" to TypeType.Boolean,
        "Visible" to TypeType.Boolean,
        "TooltipText" to TypeType.String,
        "TextTooltipStyle" to TypeType.TextTooltipStyle,
        "TooltipTextSpans" to TypeType.String,
        "TextTooltipShowDelay" to TypeType.Float,
        "ContentWidth" to TypeType.Int32,
        "ContentHeight" to TypeType.Int32,
        "AutoScrollDown" to TypeType.Boolean,
        "KeepScrollPosition" to TypeType.Boolean,
        "MouseWheelScrollBehavior" to TypeType.MouseWheelScrollBehaviorType,
        "MaskTexturePath" to TypeType.String,
        "OutlineColor" to TypeType.Color,
        "OutlineSize" to TypeType.Float,
        "Overscroll" to TypeType.Boolean,
    )

    val BUTTON_PROPERTIES = mapOf(
        "Disabled" to TypeType.Boolean,
    )
}