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

    BarAlignment(
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
    InfoDisplay(
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
    ButtonStyleState(
        mapOf(
            "Background" to PatchStyle,
            "LabelStyle" to LabelStyle,
            "BindingLabelStyle" to LabelStyle
        )
    ),
    ButtonStyle(
        mapOf(
            "Default" to ButtonStyleState,
            "Hovered" to ButtonStyleState,
            "Pressed" to ButtonStyleState,
            "Disabled" to ButtonStyleState,
            "Sounds" to SoundsStyle,
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
            "Sounds" to SoundsStyle,
        )
    ),

    SliderStyle(
        mapOf(
            "Background" to PatchStyle,
            "Handle" to String,
            "HandleWidth" to Int32,
            "HandleHeight" to Int32,
            "Sounds" to SoundsStyle,
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
    Side(
        setOf("Left", "Right")
    ),

    // Icon is a really weird type. It appears on a TextField in FileSelector.ui under Icon, and a "ClearButtonStyle".
    // TODO: Should the Icon and ClearButtonStyle be separated, they are very similar.
    Icon(
        mapOf(
            "Texture" to PatchStyle, // Can be patchstyle.
            "Width" to Int32,
            "Height" to Int32,
            "Offset" to Int32,
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
            "TextFieldHeight" to Int32,
        )
    ),

    ColorFormat(setOf("Rgb", "Rgba")), // TODO: Find more formats?

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
            "PanelOffset" to Int32,
            "ArrowAnchor" to Anchor,
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

    TabStateStyle(
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
            "Default" to TabStateStyle,
            "Hovered" to TabStateStyle,
            "Pressed" to TabStateStyle,
        )
    ),

    TabNavigationStyle(
        mapOf(
            "TabStyle" to TabNavigationStyleElement,
            "SelectedTabStyle" to TabNavigationStyleElement,
            "TabSounds" to SoundsStyle,
            "SeparatorAnchor" to Anchor,
            "SeparatorBackground" to String, // TODO: Is this a PatchStyle?
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
            "ArrowWidth" to Int32,
            "ArrowHeight" to Int32,
            "LabelStyle" to LabelStyle,
            "EntryLabelStyle" to LabelStyle,
            "NoItemsLabelStyle" to LabelStyle,
            "SelectedEntryLabelStyle" to LabelStyle,
            "HorizontalPadding" to Int32,
            "PanelScrollbarStyle" to ScrollbarStyle,
            "PanelBackground" to PatchStyle,
            "PanelPadding" to Padding, // TODO: Check this actually supports full padding, not just integer
            "PanelWidth" to Int32,
            "PanelAlign" to Alignment,
            "PanelOffset" to Int32,
            "EntryHeight" to Int32,
            "EntryIconWidth" to Int32,
            "EntryIconHeight" to Int32,
            "EntriesInViewport" to Int32,
            "HorizontalEntryPadding" to Padding, // TODO: Check this actually supports full padding, not just integer
            "HoveredEntryBackground" to PatchStyle,
            "PressedEntryBackground" to PatchStyle,
            "Sounds" to DropdownBoxSounds,
            "EntrySounds" to SoundsStyle,
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