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

    BarAlignment(listOf(
        "Vertical", "Horizontal"
    )),

    Alignment(listOf(
        "Top", "Bottom", "Left", "Right"
    )),
    
    Direction(listOf(
        "Center", "Start", "End"
    )),
    LabelStyle(
        // TODO: move this to styles? Inherit from styles?
        mapOf(
            "FontSize" to Integer,
            "FontName" to String, // "Primary", "Secondary"
            "LetterSpacing" to Float,
            "TextColor" to Color,
            "RenderBold" to Boolean,
            "RenderUppercase" to Boolean,
            "RenderItalics" to Boolean,
            "Alignment" to Direction,
            "HorizontalAlignment" to Direction,
            "VerticalAlignment" to Direction,
            "OutlineColor" to Color,
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
    CheckBoxStyle(
        mapOf(
            "Checked" to CheckedStyleInnerElement,
            "Unchecked" to CheckedStyleInnerElement
        )
    ),
    CheckedStyle(
        mapOf(
            "Default" to CheckBoxStyle,
            "Hovered" to CheckBoxStyle,
            "Pressed" to CheckBoxStyle,
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
            "TextColor" to Color,
            "FontSize" to Integer,
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

    ColorPickerDropdownBoxBackgroundThing(
        mapOf(
            "Default" to String
        )
    ),
    ColorPickerDropdownBoxStyle( // TODO: Does this have *all* properties of DropdownBoxStyle? If so we can extend that one
        mapOf(
            "ColorPickerStyle" to ColorPickerStyle,
            "Background" to ColorPickerDropdownBoxBackgroundThing,
            "ArrowBackground" to ColorPickerDropdownBoxBackgroundThing,
            "Overlay" to ColorPickerDropdownBoxBackgroundThing,
            "PanelBackground" to PatchStyle,
            "PanelPadding" to Padding,
            "PanelOffset" to Integer,
            "ArrowAnchor" to Anchor,
        )
    ),

    SpriteFrame(
        mapOf(
            "Width" to Integer,
            "Height" to Integer,
            "PerRow" to Integer,
            "Count" to Integer,
        )
    ),

    NumberFieldFormat(
        mapOf(
            "MaxDecimalPlaces" to Integer,
            "Step" to Float,
            "MinValue" to Float,
            "MaxValue" to Float,
        )
    ),

    ItemGridStyle(
        mapOf(
            "SlotSize" to Integer,
            "SlotIconSize" to Integer,
            "SlotSpacing" to Integer,
            "SlotBackground" to PatchStyle,
            "QuantityPopupSlotOverlay" to String, // TODO: No example of patch style use, check if they accept patch styles
            "BrokenSlotBackgroundOverlay" to String,
            "BrokenSlotIconOverlay" to String,
            "DefaultItemIcon" to String,
            "DurabilityBar" to String,
            "DurabilityBarBackground" to String,
            "DurabilityBarAnchor" to Anchor,
            "DurabilityBarColorStart" to Color,
            "DurabilityBarColorEnd" to Color,
            "CursedIconPatch" to PatchStyle, // TODO: I assumed this is a patchstyle from the name, check this too
            "CursedIconAnchor" to Anchor,
            "ItemStackHoveredSound" to Sound,
        )
    ),

    TabNavigationStyleState(
        mapOf(
            "LabelStyle" to LabelStyle,
            "Padding" to Padding,
            "Background" to Color, // TODO: Could be patchstyle
        )
    ),

    TabNavigationStyleElement(
        mapOf(
            "Default" to TabNavigationStyleState,
            "Hovered" to TabNavigationStyleState,
            "Pressed" to TabNavigationStyleState,
        )
    ),

    TabNavigationStyle(
        mapOf(
            "TabStyle" to TabNavigationStyleElement,
            "SelectedTabStyle" to TabNavigationStyleElement,
        )
    ),

    DropdownBoxSounds(
        mapOf(
            "Activate" to Sound,
            "MouseHover" to Sound,
            "Close" to Sound
        )
    ),
    DropdownBoxStyle(
        mapOf(
            "DefaultBackground" to PatchStyle,
            "HoveredBackground" to PatchStyle,
            "PressedBackground" to PatchStyle,
            "DefaultArrowTexturePath" to String,
            "HoveredArrowTexturePath" to String,
            "PressedArrowTexturePath" to String,
            "ArrowWidth" to Integer,
            "ArrowHeight" to Integer,
            "LabelStyle" to LabelStyle,
            "EntryLabelStyle" to LabelStyle,
            "NoItemsLabelStyle" to LabelStyle,
            "SelectedEntryLabelStyle" to LabelStyle,
            "HorizontalPadding" to Integer,
            "PanelScrollbarStyle" to ScrollbarStyle,
            "PanelBackground" to PatchStyle,
            "PanelPadding" to Padding, // TODO: Check this actually supports full padding, not just integer
            "PanelWidth" to Integer,
            "PanelAlign" to Alignment,
            "PanelOffset" to Integer,
            "EntryHeight" to Integer,
            "EntriesInViewport" to Integer,
            "HorizontalEntryPadding" to Padding, // TODO: Check this actually supports full padding, not just integer
            "HoveredEntryBackground" to PatchStyle,
            "PressedEntryBackground" to PatchStyle,
            "Sounds" to DropdownBoxSounds,
            "EntrySounds" to SoundsStyle,
            "FocusOutlineSize" to Integer,
            "FocusOutlineColor" to Color,
        )
    ),
    
    ;

    constructor(allowedFields: Map<String, TypeType>) : this(false, false, allowedFields, emptyList())
    constructor() : this(true, false, emptyMap(), emptyList())
    constructor(enum: List<String>) : this(false, true, emptyMap(), enum)

    fun canNegate(): Boolean = this == Integer || this == Float
}