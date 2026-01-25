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
    Color,

    Sound(
        mapOf(
            "SoundPath" to String,
            "MinPitch" to Float,
            "MaxPitch" to Float,
            "Volume" to Integer, // TODO: Confirm volume is not actually float with Set.
        )
    ),
    SoundsStyle(
        mapOf(
            "Activate" to Sound,
            "MouseHover" to Sound,
            "Close" to Sound,
            "Context" to Sound, // TODO: Verify this appears on all sound styles.
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
            "TextColor" to Color,
            "RenderBold" to Boolean,
            "RenderUppercase" to Boolean,
            "RenderItalics" to Boolean,
            "Alignment" to LabelAlignment,
            "HorizontalAlignment" to LabelAlignment,
            "VerticalAlignment" to LabelAlignment,
            "OutlineColor" to String,
            "Wrap" to Boolean,
        )
    ),
    PatchStyle(
        mapOf(
            "TexturePath" to String,
            "VerticalBorder" to Integer,
            "HorizontalBorder" to Integer,
            "Border" to Integer,
            "Color" to Color,
            "LabelStyle" to LabelStyle,
            "Anchor" to Anchor,
        )
    ),

    ScrollbarStyle(
        mapOf(
            "OnlyVisibleWhenHovered" to Boolean,
            "Spacing" to Integer,
            "Size" to Integer,
            "Background" to PatchStyle,
            "Handle" to PatchStyle,
            "HoveredHandle" to PatchStyle,
            "DraggedHandle" to PatchStyle,
            // TODO: Check size/spacing data types.
        )
    ),

    CheckedStyleInnerElement(
    mapOf(
            "DefaultBackground" to PatchStyle,
            "HoveredBackground" to PatchStyle,
            "PressedBackground" to PatchStyle,
            "DisabledBackground" to PatchStyle,
            "ChangedSound" to Sound,
        )
    ),
    CheckedStyleElement(
        mapOf(
            "Checked" to CheckedStyleInnerElement,
            "Unchecked" to CheckedStyleInnerElement
        )
    ),
    CheckedStyle(
        mapOf(
            "Default" to CheckedStyleElement,
            "Hovered" to CheckedStyleElement,
            "Pressed" to CheckedStyleElement,
            "Sounds" to SoundsStyle,
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

    SliderStyle(
        mapOf(
            "Background" to PatchStyle,
            "Handle" to String,
            "HandleWidth" to Integer,
            "HandleHeight" to Integer,
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

    ColorPickerTextFieldDecorationElement( // TODO: Maybe this should actually correspond to a more commonly used structure
        mapOf(
            "Background" to Color,
        )
    ),
    ColorPickerTextFieldDecoration(
        mapOf(
            "Default" to ColorPickerTextFieldDecorationElement
        )
    ),

    ColorPickerStyle(
        mapOf(
            "OpacitySelectorBackground" to String, // TODO: Are these PatchStyles?
            "ButtonBackground" to String,
            "ButtonFill" to String,
            "TextFieldDecoration" to ColorPickerTextFieldDecoration,
            "TextFieldPadding" to Padding,
            "TextFieldHeight" to Integer,
        )
    ),

    ColorPickerFormat(listOf("Rgb")), // TODO: Find more formats?

    ColorPickerDropdownBoxStyle(
        mapOf(
            "ColorPickerStyle" to ColorPickerStyle,

        )
    )
    
    ;

    constructor(allowedFields: Map<String, TypeType>) : this(false, false, allowedFields, emptyList())
    constructor() : this(true, false, emptyMap(), emptyList())
    constructor(enum: List<String>) : this(false, true, emptyMap(), enum)

    fun canNegate(): Boolean = this == Integer || this == Float || this == Double
}