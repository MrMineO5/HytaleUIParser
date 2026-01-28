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
    // This is used for SliderNumberField
    Double,
    Color,

    Sound(
        mapOf(
            "SoundPath" to String,
            "MinPitch" to Float,
            "MaxPitch" to Float,
            "Volume" to Float, // TODO: Confirm volume is not actually float with Set.
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
        // These can actually all be floats...?
        // ItemLibraryPanel.ui -> Use of math operators with non-integer values. Does it round? Cast?
        mapOf(
            "Left" to Float,
            "Right" to Float,
            "Top" to Float,
            "Bottom" to Float,
            "Width" to Float,
            "Height" to Float,
            "MinWidth" to Float,
            "MaxWidth" to Float,
            "Full" to Float,
            "Horizontal" to Float,
            "Vertical" to Float,
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
        // BenchInfoPane.ui - use of "TopLeft"
        "Top", "Bottom", "Left", "Right", "TopLeft"
    )),
    
    Direction(listOf(
        "Center", "Start", "End"
    )),
    InfoDisplay(listOf(
       // TODO: Confirm how this works, if it is just a list of valid states...
        "None"
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
            "LabelStyle" to LabelStyle,
            "BindingLabelStyle" to LabelStyle
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
            "RenderBold" to Boolean,
            "RenderItalics" to Boolean,
            "RenderUppercase" to Boolean,
        )
    ),
    TextTooltipStyle(
        mapOf(
            "Background" to PatchStyle,
            "MaxWidth" to Integer,
            "LabelStyle" to LabelStyle,
            // This is actually an integer in examples.
            "Padding" to Integer,
            // ALIGNMENT??? Is this the exact same?
            "Alignment" to Alignment
        )
    ),
    Side(
        listOf("Left", "Right")
    ),
    // Icon is a really weird type. It appears on a TextField in FileSelector.ui under Icon, and a "ClearButtonStyle".
    // TODO: Should the Icon and ClearButtonStyle be separated, they are very similar.
    Icon(
        mapOf(
            "Texture" to PatchStyle, // Can be patchstyle.
            "Width" to Integer,
            "Height" to Integer,
            "Offset" to Integer,
            // Only appears on the ClearButtonStyle so far.
            "HoveredTexture" to PatchStyle,
            "PressedTexture" to PatchStyle,
            "Side" to Side,
        )
    ),
    TextFieldDecorationElement(
        mapOf(
            "Background" to PatchStyle,
            "Icon" to Icon,
            "ClearButtonStyle" to Icon
        )
    ),
    TextFieldDecoration(
        mapOf(
            "Default" to TextFieldDecorationElement
        )
    ),

    ColorPickerStyle(
        mapOf(
            "OpacitySelectorBackground" to String, // TODO: Are these PatchStyles?
            "ButtonBackground" to String,
            "ButtonFill" to String,
            "TextFieldDecoration" to TextFieldDecoration,
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
            "ItemStackActivateSound" to Sound,
            // MouseHover sound?
            // Close sound?
        )
    ),

    TabNavigationStyleState(
        mapOf(
            "LabelStyle" to LabelStyle,
            "Padding" to Padding,
            "Background" to PatchStyle,
            "Overlay" to PatchStyle,
            "IconAnchor" to Anchor,
            "Anchor" to Anchor,
            "TooltipStyle" to TextTooltipStyle,
            // TODO: VERIFY - is it double or float?
            "IconOpacity" to Float,
            "ContentMaskTexturePath" to String,
            
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
            "TabSounds" to SoundsStyle
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
            "EntryIconWidth" to Integer,
            "EntryIconHeight" to Integer,
            "EntriesInViewport" to Integer,
            "HorizontalEntryPadding" to Padding, // TODO: Check this actually supports full padding, not just integer
            "HoveredEntryBackground" to PatchStyle,
            "PressedEntryBackground" to PatchStyle,
            "Sounds" to DropdownBoxSounds,
            "EntrySounds" to SoundsStyle,
            "FocusOutlineSize" to Integer,
            "FocusOutlineColor" to Color,
            "PanelTitleLabelStyle" to LabelStyle,
            "SelectedEntryIconBackground" to PatchStyle,
            "IconTexturePath" to String,
            "IconWidth" to Integer,
            "IconHeight" to Integer,
        )
    ),
    
    PopupStyle(
        mapOf(
            "Background" to Color,
            "ButtonPadding" to Padding,
            "Padding" to Padding,
            "TooltipStyle" to TextTooltipStyle,
            "ButtonStyle" to ButtonStyle,
            "SelectedButtonStyle" to ButtonStyle,
        )
    ),
    MenuItemStyle(
        mapOf(
            // These are actually patchstyles, they implement very similar features.
            "Default" to PatchStyle,
            "Hovered" to PatchStyle,
            // TODO: Are there other states?
        )
    ),
    BlockSelectorStyle(
        mapOf(
            "ItemGridStyle" to ItemGridStyle,
            // TODO: Confirm patchstyle or only path? Presented as path only.
            "SlotDropIcon" to PatchStyle,
            "SlotDeleteIcon" to PatchStyle,
            "SlotHoverOverlay" to PatchStyle,
        )
    ),
    LabeledCheckBoxStyleElement(
        mapOf(
            "DefaultBackground" to PatchStyle,
            "HoveredBackground" to PatchStyle,
            "PressedBackground" to PatchStyle,
            "Text" to String,
            "DefaultLabelStyle" to LabelStyle,
            "HoveredLabelStyle" to LabelStyle,
            "PressedLabelStyle" to LabelStyle,
            "ChangedSound" to Sound,
        )
    ),
    LabeledCheckBoxStyle(
        mapOf(
            "Checked" to LabeledCheckBoxStyleElement,
            "Unchecked" to LabeledCheckBoxStyleElement,
        )
    )
    ;

    constructor(allowedFields: Map<String, TypeType>) : this(false, false, allowedFields, emptyList())
    constructor() : this(true, false, emptyMap(), emptyList())
    constructor(enum: List<String>) : this(false, true, emptyMap(), enum)

    fun canNegate(): Boolean = this == Integer || this == Float
}