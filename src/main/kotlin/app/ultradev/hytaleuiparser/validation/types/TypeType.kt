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
    ScrollbarStyle(
        mapOf(
            "OnlyVisibleWhenHovered" to Boolean,
            "Spacing" to Integer, 
            "Size" to Integer
            // TODO: Check size/spacing data types.
        )
    ),
    SoundsStyleElement(
        mapOf(
            "SoundPath" to TypeType.String,
            "MinPitch" to TypeType.Float,
            "MaxPitch" to TypeType.Float,
            "Volume" to TypeType.Integer, // TODO: Confirm volume is not actually float with Set.
        )
    ),
    SoundsStyle(
        mapOf(
            "Activate" to TypeType.SoundsStyleElement,
            "MouseHover" to TypeType.SoundsStyleElement,
            "Close" to TypeType.SoundsStyleElement,
            "Context" to TypeType.SoundsStyleElement, // TODO: Verify this appears on all sound styles.
        )
    ),
 
    LayoutMode(
        listOf(
            "TopScrolling",
            "MiddleCenter",
            "Left",
            "Right",
            "Full",
            "Middle",
            "Bottom",
            "BottomScrolling",
            "CenterMiddle",
            "Top",
            "LeftCenterWrap",
            "Center"
        )
    ),
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
            "OutlineColor" to String, 
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
    CheckedStyleInnerElement(
    mapOf(
            "DefaultBackground" to TypeType.PatchStyle,
            "HoveredBackground" to TypeType.PatchStyle,
            "PressedBackground" to TypeType.PatchStyle,
            "DisabledBackground" to TypeType.PatchStyle,
            "ChangedSound" to TypeType.SoundsStyleElement,
        )
    ),
    CheckedStyleElement(
        mapOf(
            "Checked" to TypeType.CheckedStyleInnerElement,
            "Unchecked" to TypeType.CheckedStyleInnerElement
        )
    ),
    CheckedStyle(
        mapOf(
            "Default" to CheckedStyleElement,
            "Hovered" to CheckedStyleElement,
            "Pressed" to TypeType.CheckedStyleElement,
            "Sounds" to TypeType.SoundsStyle,
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
            "Hovered" to ButtonStyleElement,
            "Pressed" to ButtonStyleElement,
            "Disabled" to ButtonStyleElement,
            "Sounds" to SoundsStyle,
        )
    ),
    
    // Text field styles - used by TextField, CompactTextField, MultilineTextField, NumberField, SliderNumberField, FloatSliderNumberField
    InputFieldStyle(
        mapOf(
            "TextColor" to String,
        )
    ),
    TextTooltipStyle(
        mapOf(
            "Background" to PatchStyle,
            "MaxWidth" to Integer,
            "LabelStyle" to LabelStyle,
            // This is actually an integer in examples.
            "Padding" to Integer
        )
    ),
    
    ;

    constructor(allowedFields: Map<String, TypeType>) : this(false, false, allowedFields, emptyList())
    constructor() : this(true, false, emptyMap(), emptyList())
    constructor(enum: List<String>) : this(false, true, emptyMap(), enum)
}