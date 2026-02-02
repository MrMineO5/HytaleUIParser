package app.ultradev.hytaleuiparser.validation.types

enum class TypeType(
    val isPrimitive: Boolean,
    val isEnum: Boolean,
    val allowedFields: Map<String, TypeType>,
    val enum: Set<String>,
) {
    String,
    Int32,
    Float,
    Boolean,
    Color,

    SoundStyle(
        mapOf(
            "SoundPath" to String,
            "MinPitch" to Float,
            "MaxPitch" to Float,
            "Volume" to Float,
        )
    ),
    SoundsStyle(
        mapOf(
            "Activate" to SoundStyle,
            "MouseHover" to SoundStyle,
            "Close" to SoundStyle,
            "Context" to SoundStyle, // TODO: Verify this appears on all sound styles.
        )
    ),
    // Used by TextButton/Button for sounds.
    ButtonSounds(
        SoundsStyle.allowedFields
    ),
    LayoutMode(
        setOf(
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
            "Left" to Int32,
            "Right" to Int32,
            "Top" to Int32,
            "Bottom" to Int32,
            "Width" to Int32,
            "Height" to Int32,
            "MinWidth" to Int32,
            "MaxWidth" to Int32,
            "Full" to Int32,
            "Horizontal" to Int32,
            "Vertical" to Int32,
        )
    ),
    Padding(
        mapOf(
            "Left" to Int32,
            "Right" to Int32,
            "Top" to Int32,
            "Bottom" to Int32,
            "Horizontal" to Int32,
            "Vertical" to Int32,
            "Full" to Int32,
        )
    ),

    ProgressBarAlignment(
        setOf(
            "Vertical", "Horizontal"
        )
    ),

    Alignment(
        setOf(
            // BenchInfoPane.ui - use of "TopLeft"
            "Top", "Bottom", "Left", "Right", "TopLeft"
        )
    ),

    Direction(
        setOf(
            "Center", "Start", "End"
        )
    ),
    ProgressBarDirection(
      Direction.allowedFields  
    ),
    ItemGridInfoDisplayMode(
        setOf(
            // TODO: Confirm how this works, if it is just a list of valid states...
            "None"
        )
    ),
    LabelStyle(
        // TODO: move this to styles? Inherit from styles?
        mapOf(
            "FontSize" to Int32,
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
            "VerticalBorder" to Int32,
            "HorizontalBorder" to Int32,
            "Border" to Int32,
            "Color" to Color,
            "Anchor" to Anchor,
        )
    ),

    ScrollbarStyle(
        mapOf(
            "OnlyVisibleWhenHovered" to Boolean,
            "Spacing" to Int32,
            "Size" to Int32,
            "Background" to PatchStyle,
            "Handle" to PatchStyle,
            "HoveredHandle" to PatchStyle,
            "DraggedHandle" to PatchStyle,
        )
    ),

    CheckBoxStyleState(
        mapOf(
            "DefaultBackground" to PatchStyle,
            "HoveredBackground" to PatchStyle,
            "PressedBackground" to PatchStyle,
            "DisabledBackground" to PatchStyle,
            "ChangedSound" to SoundStyle,
        )
    ),
    CheckBoxStyle(
        mapOf(
            "Checked" to CheckBoxStyleState,
            "Unchecked" to CheckBoxStyleState
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
    ButtonStyleState(
        mapOf(
            "Background" to PatchStyle,
            "LabelStyle" to LabelStyle,
        )
    ),
    ButtonStyle(
        mapOf(
            "Default" to ButtonStyleState,
            "Hovered" to ButtonStyleState,
            "Pressed" to ButtonStyleState,
            "Disabled" to ButtonStyleState,
            "Sounds" to ButtonSounds,
        )
    ),
    SubMenuItemStyleState(
        mapOf(
            "Background" to PatchStyle,
            "LabelStyle" to LabelStyle,
            "BindingLabelStyle" to LabelStyle
        )
    ),
    SubMenuItemStyle(
        mapOf(
            "Default" to SubMenuItemStyleState,
            "Hovered" to SubMenuItemStyleState,
            "Pressed" to SubMenuItemStyleState,
            "Disabled" to SubMenuItemStyleState,
            "Sounds" to ButtonSounds,
        )
    ),
    TextButtonStyleState(
        ButtonStyleState.allowedFields
    ),
    TextButtonStyle(
        mapOf(
            "Default" to TextButtonStyleState,
            "Hovered" to TextButtonStyleState,
            "Pressed" to TextButtonStyleState,
            "Disabled" to TextButtonStyleState,
            "Sounds" to ButtonSounds,
        )
    ),

    SliderStyle(
        mapOf(
            "Background" to PatchStyle,
            "Handle" to String,
            "HandleWidth" to Int32,
            "HandleHeight" to Int32,
            "Sounds" to ButtonSounds,
        )
    ),

    // Text field styles - used by TextField, CompactTextField, MultilineTextField, NumberField, SliderNumberField, FloatSliderNumberField
    InputFieldStyle(
        mapOf(
            "TextColor" to Color,
            "FontSize" to Int32,
            "RenderBold" to Boolean,
            "RenderItalics" to Boolean,
            "RenderUppercase" to Boolean,
        )
    ),
    TextTooltipStyle(
        mapOf(
            "Background" to PatchStyle,
            "MaxWidth" to Int32,
            "LabelStyle" to LabelStyle,
            // This is actually an integer in examples.
            "Padding" to Int32,
            // ALIGNMENT??? Is this the exact same?
            "Alignment" to Alignment
        )
    ),
    InputFieldButtonSide(
        setOf("Left", "Right")
    ),

    // Icon is a really weird type. It appears on a TextField in FileSelector.ui under Icon, and a "ClearButtonStyle".
    InputFieldIcon(
        mapOf(
            "Texture" to PatchStyle, // Can be patchstyle.
            // These can all be floats.
            "Width" to Int32,
            "Height" to Int32,
            "Offset" to Int32,
            "Side" to InputFieldButtonSide,
        )
    ),
    InputFieldButtonStyle(
        InputFieldIcon.allowedFields + mapOf(
            "HoveredTexture" to PatchStyle,
            "PressedTexture" to PatchStyle
        )
    ),
    InputFieldDecorationStyleState(
        mapOf(
            "Background" to PatchStyle,
            "Icon" to InputFieldIcon,
            "ClearButtonStyle" to InputFieldButtonStyle
        )
    ),
    InputFieldDecorationStyle(
        mapOf(
            "Default" to InputFieldDecorationStyleState,
            "Focused" to InputFieldDecorationStyleState
        )
    ),

    ColorPickerStyle(
        mapOf(
            "OpacitySelectorBackground" to String, // TODO: Are these PatchStyles?
            "ButtonBackground" to String,
            "ButtonFill" to String,
            "TextFieldDecoration" to InputFieldDecorationStyle,
            "TextFieldPadding" to Padding,
            "TextFieldHeight" to Int32,
        )
    ),

    ColorFormat(setOf("Rgb", "Rgba")), // TODO: Find more formats?

    ColorPickerDropdownBoxStateBackground(
        mapOf(
            "Default" to PatchStyle,
            "Hovered" to PatchStyle,
            "Pressed" to PatchStyle
        )
    ),
    ColorPickerDropdownBoxStyle( // THIS IS A DISTINCTLY DIFFERENT TYPE.
        mapOf(
            "ColorPickerStyle" to ColorPickerStyle,
            "Background" to ColorPickerDropdownBoxStateBackground,
            "ArrowBackground" to ColorPickerDropdownBoxStateBackground,
            "Overlay" to ColorPickerDropdownBoxStateBackground,
            "PanelBackground" to PatchStyle,
            "PanelPadding" to Padding,
            "PanelWidth" to Int32,
            "PanelOffset" to Int32,
            "ArrowAnchor" to Anchor,
            "Sounds" to ButtonSounds,
        )
    ),

    SpriteFrame(
        mapOf(
            "Width" to Int32,
            "Height" to Int32,
            "PerRow" to Int32,
            "Count" to Int32,
        )
    ),

    NumberFieldFormat(
        mapOf(
            "MaxDecimalPlaces" to Int32,
            "Step" to Float,
            "MinValue" to Float,
            "MaxValue" to Float,
        )
    ),

    ItemGridStyle(
        mapOf(
            "SlotSize" to Int32,
            "SlotIconSize" to Int32,
            "SlotSpacing" to Int32,
            "SlotBackground" to PatchStyle,
            "QuantityPopupSlotOverlay" to PatchStyle,
            "BrokenSlotBackgroundOverlay" to PatchStyle,
            "BrokenSlotIconOverlay" to PatchStyle,
            "DefaultItemIcon" to PatchStyle,
            // UI Path
            "DurabilityBar" to String,
            "DurabilityBarBackground" to PatchStyle,
            "DurabilityBarAnchor" to Anchor,
            "DurabilityBarColorStart" to Color,
            "DurabilityBarColorEnd" to Color,
            "CursedIconPatch" to PatchStyle,
            "CursedIconAnchor" to Anchor,
            "ItemStackHoveredSound" to SoundStyle,
            "ItemStackActivateSound" to SoundStyle,
            // MouseHover sound?
            // Close sound?
        )
    ),

    TabStyleState(
        mapOf(
            "LabelStyle" to LabelStyle,
            "Padding" to Padding,
            "Background" to PatchStyle,
            "Overlay" to PatchStyle,
            "IconAnchor" to Anchor,
            "Anchor" to Anchor,
            "TooltipStyle" to TextTooltipStyle,
            "IconOpacity" to Float,
            "ContentMaskTexturePath" to String
        )
    ),

    TabStyle(
        mapOf(
            "Default" to TabStyleState,
            "Hovered" to TabStyleState,
            "Pressed" to TabStyleState,
        )
    ),

    TabNavigationStyle(
        mapOf(
            "TabStyle" to TabStyle,
            "SelectedTabStyle" to TabStyle,
            "TabSounds" to ButtonSounds,
            "SeparatorAnchor" to Anchor,
            "SeparatorBackground" to PatchStyle,
        )
    ),

    DropdownBoxSounds(
        mapOf(
            "Activate" to SoundStyle,
            "MouseHover" to SoundStyle,
            "Close" to SoundStyle
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
            "ArrowWidth" to Int32,
            "ArrowHeight" to Int32,
            "LabelStyle" to LabelStyle,
            "EntryLabelStyle" to LabelStyle,
            "NoItemsLabelStyle" to LabelStyle,
            "SelectedEntryLabelStyle" to LabelStyle,
            "HorizontalPadding" to Int32,
            "PanelScrollbarStyle" to ScrollbarStyle,
            "PanelBackground" to PatchStyle,
            "PanelPadding" to Int32,
            "PanelWidth" to Int32,
            "PanelAlign" to Alignment,
            "PanelOffset" to Int32,
            "EntryHeight" to Int32,
            "EntryIconWidth" to Int32,
            "EntryIconHeight" to Int32,
            "EntriesInViewport" to Int32,
            "HorizontalEntryPadding" to Int32,
            "HoveredEntryBackground" to PatchStyle,
            "PressedEntryBackground" to PatchStyle,
            "Sounds" to DropdownBoxSounds,
            "EntrySounds" to ButtonSounds,
            "FocusOutlineSize" to Int32,
            "FocusOutlineColor" to Color,
            "PanelTitleLabelStyle" to LabelStyle,
            "SelectedEntryIconBackground" to PatchStyle,
            "IconTexturePath" to String,
            "IconWidth" to Int32,
            "IconHeight" to Int32,
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
            "SlotDropIcon" to PatchStyle,
            "SlotDeleteIcon" to PatchStyle,
            "SlotHoverOverlay" to PatchStyle,
        )
    ),
    LabeledCheckBoxStyleState(
        mapOf(
            "DefaultBackground" to PatchStyle,
            "HoveredBackground" to PatchStyle,
            "PressedBackground" to PatchStyle,
            "Text" to String,
            "DefaultLabelStyle" to LabelStyle,
            "HoveredLabelStyle" to LabelStyle,
            "PressedLabelStyle" to LabelStyle,
            "ChangedSound" to SoundStyle,
        )
    ),
    LabeledCheckBoxStyle(
        mapOf(
            "Checked" to LabeledCheckBoxStyleState,
            "Unchecked" to LabeledCheckBoxStyleState,
        )
    ),

    FileDropdownBoxStyle(
        mapOf(
            "DefaultBackground" to PatchStyle,
            "HoveredBackground" to PatchStyle,
            "PressedBackground" to PatchStyle,
            "DefaultArrowTexturePath" to String,
            "HoveredArrowTexturePath" to String,
            "PressedArrowTexturePath" to String,
            "ArrowWidth" to Int32,
            "ArrowHeight" to Int32,
            "LabelStyle" to LabelStyle,
            "HorizontalPadding" to Int32,
            "PanelAlign" to Alignment,
            "PanelOffset" to Int32,
            "Sounds" to DropdownBoxSounds,
        )
    ),

    PopupMenuLayerStyle(
        mapOf(
            "Background" to PatchStyle,
            "Padding" to Int32, // TODO: Check this actually supports full padding, not just integer
            "BaseHeight" to Int32,
            "MaxWidth" to Int32,
            "TitleStyle" to LabelStyle,
            "TitleBackground" to PatchStyle,
            "RowHeight" to Int32,
            "ItemLabelStyle" to LabelStyle,
            "ItemPadding" to Padding,
            "ItemBackground" to PatchStyle,
            "ItemIconSize" to Int32,
            "HoveredItemBackground" to PatchStyle,
            "PressedItemBackground" to PatchStyle,
            "ItemSounds" to SoundsStyle,
        )
    ),

    ;

    constructor(allowedFields: Map<String, TypeType>) : this(false, false, allowedFields, emptySet())
    constructor() : this(true, false, emptyMap(), emptySet())
    constructor(enum: Set<String>) : this(false, true, emptyMap(), enum)

    fun canNegate(): Boolean = this == Int32 || this == Float

    fun default(): String {
        if (this.isPrimitive) {
            return when (this) {
                Boolean -> "false"
                Int32 -> "0"
                Float -> "0.0"
                String -> "\"\""
                Color -> "#000000(1.0)"
                else -> throw IllegalStateException("No default value for primitive type: $this")
            }
        } else if (this.isEnum) {
            return enum.first()
        } else {
            return "()"
        }
    }

    fun displayFullStructure(): String {
        if (this.isPrimitive) return this.name
        if (this.isEnum) return "enum " + this.name + " (" + enum.joinToString(", ") + ")"
        return "type " + this.name + " {\n" + allowedFields.map { (key, value) -> "   $key: ${value.name}\n" }
            .joinToString("") + "}"
    }
}