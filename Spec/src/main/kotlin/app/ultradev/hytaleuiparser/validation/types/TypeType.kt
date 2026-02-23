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
    Dict,

    LabelSpan(
        mapOf(
            "Text" to String,
            "IsUppercase" to Boolean,
            "IsBold" to Boolean,
            "IsItalics" to Boolean,
            "IsMonospace" to Boolean,
            "IsUnderlined" to Boolean,
            "Color" to Color,
            "OutlineColor" to Color,
            "Link" to String,
            "Params" to Dict,
        )
    ),

    SoundStyle(
        mapOf(
            "SoundPath" to String,
            "MinPitch" to Float,
            "MaxPitch" to Float,
            "Volume" to Float,
            "StopExistingPlayback" to Boolean,
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
            "Full",

            "Left",
            "Right",
            "Top",
            "Bottom",

            "Center",
            "Middle",

            "CenterMiddle",
            "MiddleCenter",

            "TopScrolling",
            "BottomScrolling",
            "LeftScrolling",
            "RightScrolling",

            "LeftCenterWrap",
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
    DropdownBoxAlign(
        setOf(
            // BenchInfoPane.ui - use of "TopLeft"
            "Top", "Bottom", "Left", "Right"
        )
    ),
    LabelAlignment(
        setOf(
            "Center", "Start", "End"
        )
    ),
    ProgressBarDirection(
      LabelAlignment.enum
    ),
    ItemGridInfoDisplayMode(
        setOf(
            "None",
            "Tooltip",
            "Adjacent"
        )
    ),
    LabelStyle(
        // TODO: move this to styles? Inherit from styles?
        mapOf(
            "FontSize" to Float,
            "FontName" to String, // "", "Secondary"
            "LetterSpacing" to Float,
            "TextColor" to Color,
            "RenderBold" to Boolean,
            "RenderUppercase" to Boolean,
            "RenderUnderlined" to Boolean,
            "RenderItalics" to Boolean,
            "Alignment" to LabelAlignment,
            "HorizontalAlignment" to LabelAlignment,
            "VerticalAlignment" to LabelAlignment,
            "OutlineColor" to Color,
            "Wrap" to Boolean,
            "ShrinkTextToFit" to Boolean,
            "MinShrinkTextToFitFontSize" to Float,
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
            "Area" to Padding,
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
            "HoveredSound" to SoundStyle,
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
            "LabelMaskTexturePath" to String,
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
    ToggleButtonStyleState(
        mapOf(
            "Background" to PatchStyle,
        )
    ),
    ToggleButtonStyle(
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
            "BindingLabelStyle" to LabelStyle,
            "LabelMaskTexturePath" to String
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
            "Fill" to PatchStyle,
            "Handle" to String,
            "HandleWidth" to Int32,
            "HandleHeight" to Int32,
            "Sounds" to ButtonSounds,
        )
    ),

    // Text field styles - used by TextField, CompactTextField, MultilineTextField, NumberField, SliderNumberField, FloatSliderNumberField
    InputFieldStyle(
        mapOf(
            "FontName" to String,
            "FontSize" to Float,
            "TextColor" to Color,
            "RenderUppercase" to Boolean,
            "RenderBold" to Boolean,
            "RenderItalics" to Boolean,
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
            "PressedTexture" to PatchStyle,
            "Texture" to PatchStyle,
            "Width" to Int32,
            "Height" to Int32,
            "Offset" to Int32,
            "Side" to InputFieldButtonSide,
        )
    ),
    InputFieldDecorationStyleState(
        mapOf(
            "Background" to PatchStyle,
            "Icon" to InputFieldIcon,
            "ClearButtonStyle" to InputFieldButtonStyle,
            "ToggleVisibilityButtonStyle" to InputFieldButtonStyle,
            "OutlineSize" to Int32,
            "OutlineColor" to Color,
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
            "OpacitySelectorBackground" to PatchStyle,
            "ButtonBackground" to String,
            "ButtonFill" to String,
            "TextFieldDecoration" to InputFieldDecorationStyle,
            "TextFieldInputStyle" to InputFieldStyle,
            "TextFieldPadding" to Padding,
            "TextFieldHeight" to Int32,
        )
    ),

    ColorFormat(setOf("Rgb", "Rgba", "RgbShort")),

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
            "DefaultValue" to Float,
            "Suffix" to String,
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
        )
    ),

    @Deprecated("Use TabStateStyle instead")
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
    TabStateStyle(
      TabStyleState.allowedFields
    ),
    TabStyle(
        mapOf(
            "Default" to TabStateStyle,
            "Hovered" to TabStateStyle,
            "Pressed" to TabStateStyle,
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
    DropdownBoxSearchInputStyle(
      mapOf(
          "Background" to PatchStyle,
          "Icon" to InputFieldIcon,
          "Style" to InputFieldStyle,
          "PlaceholderStyle" to InputFieldStyle,
          "Anchor" to Anchor,
          "Padding" to Padding,
          "PlaceholderText" to String,
          "ClearButtonStyle" to InputFieldButtonStyle
      )
    ),
    DropdownBoxStyle(
        mapOf(
            "DefaultBackground" to PatchStyle,
            "HoveredBackground" to PatchStyle,
            "PressedBackground" to PatchStyle,
            "DisabledBackground" to PatchStyle,
            "DefaultArrowTexturePath" to String,
            "HoveredArrowTexturePath" to String,
            "PressedArrowTexturePath" to String,
            "DisabledArrowTexturePath" to String,
            "ArrowWidth" to Int32,
            "ArrowHeight" to Int32,
            "LabelStyle" to LabelStyle,
            "DisabledLabelStyle" to LabelStyle,
            "EntryLabelStyle" to LabelStyle,
            "NoItemsLabelStyle" to LabelStyle,
            "SelectedEntryLabelStyle" to LabelStyle,
            "HorizontalPadding" to Int32,
            "PanelScrollbarStyle" to ScrollbarStyle,
            "PanelBackground" to PatchStyle,
            "PanelPadding" to Int32,
            "PanelWidth" to Int32,
            "PanelAlign" to DropdownBoxAlign,
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
            "EntryIconBackground" to PatchStyle,
            "IconTexturePath" to String,
            "IconWidth" to Int32,
            "IconHeight" to Int32,
            "SearchInputStyle" to DropdownBoxSearchInputStyle
        )
    ),
    PopupStyle(
        mapOf(
            "Background" to Color,
            "ButtonPadding" to Padding,
            "Padding" to Padding,
            "Width" to Int32,
            "TooltipStyle" to TextTooltipStyle,
            "ButtonStyle" to SubMenuItemStyle,
            "SelectedButtonStyle" to SubMenuItemStyle,
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
            "DisabledBackground" to PatchStyle,
            "Text" to String,
            "DefaultLabelStyle" to LabelStyle,
            "HoveredLabelStyle" to LabelStyle,
            "PressedLabelStyle" to LabelStyle,
            "DisabledLabelStyle" to LabelStyle,
            "ChangedSound" to SoundStyle,
            "HoveredSound" to SoundStyle,
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
            "PanelAlign" to DropdownBoxAlign,
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
    MouseWheelScrollBehaviorType(
        setOf("Default", "VerticalOnly", "HorizontalOnly")
    ),
    CodeEditorLanguage(
        setOf("Text", "Json")
    ),
    ColorOptionGridStyle(
        mapOf(
            "OptionSize" to Int32,
            "OptionSpacingHorizontal" to Int32,
            "OptionSpacingVertical" to Int32,
            "HighlightSize" to Int32,
            "HighlightOffsetLeft" to Int32,
            "HighlightOffsetTop" to Int32,
            "HighlightBackground" to PatchStyle,
            "MaskTexturePath" to String,
            "FrameBackground" to PatchStyle,
            "Sounds" to ButtonSounds,
        )
    ),
    ResizeType(
        setOf("None", "Start", "End")
    )
    ;

    val isStruct: Boolean get() = !isPrimitive && !isEnum

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