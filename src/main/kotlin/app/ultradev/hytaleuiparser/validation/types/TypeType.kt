package app.ultradev.hytaleuiparser.validation.types

enum class TypeType(
    val isPrimitive: Boolean,
    val isEnum: Boolean,
    val allowedFields: Map<String, TypeType>,
    val enum: List<String>,
) {
    String,
    Integer,
    Boolean,
    Float,
    Double,
    Anchor(
        mapOf(
            "Left" to Integer,
            "Right" to Integer,
            "Top" to Integer,
            "Bottom" to Integer,
            "Width" to Integer,
            "Height" to Integer,
            "MinWidth" to Integer,
            "MaxWidth" to Integer,
            "Full" to Integer,
            "Horizontal" to Integer,
            "Vertical" to Integer,
        )
    ),
    Padding(
        mapOf(
            "Left" to Integer,
            "Right" to Integer,
            "Top" to Integer,
            "Bottom" to Integer,
            "Horizontal" to Integer,
            "Vertical" to Integer,
            "Full" to Integer,
        )
    ),
    
    // Center, Left, Right, Start, End
    LabelAlignment(listOf(
        "Center", "Start", "End"
    )),
    LabelStyle(
        // TODO: move this to styles? Inherit from styles?
        mapOf(
            "FontSize" to Integer,
            "FontName" to String, // "Primary", "Secondary"
            "LetterSpacing" to Integer,
            "TextColor" to String,
            "RenderBold" to Boolean,
            "RenderUppercase" to Boolean,
            "RenderItalics" to Boolean,
            "Alignment" to LabelAlignment,
            "HorizontalAlignment" to LabelAlignment,
            "VerticalAlignment" to LabelAlignment,
        )
    ),
    PatchStyle(
        mapOf(
            "TexturePath" to String,
            "VerticalBorder" to Integer,
            "HorizontalBorder" to Integer,
            "Border" to Integer,
            "Color" to String, 
            "LabelStyle" to LabelStyle,
            "Anchor" to Anchor,
        )
    ),
    
    // Button styles - used by Button, TextButton, BackButton, ActionButton, ToggleButton, TabButton, ItemSlotButton
    ButtonStyleElement(
        mapOf(
            "Background" to PatchStyle,
            "LabelStyle" to LabelStyle
        )
    ),
    ButtonStyle(
        mapOf(
            "Default" to ButtonStyleElement,
            "Hover" to ButtonStyleElement,
            "Pressed" to ButtonStyleElement,
            "Disabled" to ButtonStyleElement
        )
    ),
    
    // Text field styles - used by TextField, CompactTextField, MultilineTextField, NumberField, SliderNumberField, FloatSliderNumberField
    InputFieldStyle(
        mapOf(
            "TextColor" to String,
        )
    ),

    ;

    constructor(allowedFields: Map<String, TypeType>) : this(false, false, allowedFields, emptyList())
    constructor() : this(true, false, emptyMap(), emptyList())
    constructor(enum: List<String>) : this(false, true, emptyMap(), enum)
}