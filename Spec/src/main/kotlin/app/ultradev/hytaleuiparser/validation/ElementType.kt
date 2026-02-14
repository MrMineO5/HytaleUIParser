package app.ultradev.hytaleuiparser.validation

import app.ultradev.hytaleuiparser.validation.types.TypeType
import kotlin.collections.plus

enum class ElementType {
    Group(
        mapOf(
            "ScrollbarStyle" to TypeType.ScrollbarStyle,
            "MaskTexturePath" to TypeType.String,
            "AutoScrollDown" to TypeType.Boolean,
            "KeepScrollPosition" to TypeType.Boolean,
            "LayoutMode" to TypeType.LayoutMode,
        )
    ),
    TimerLabel(
        mapOf(
            "Style" to TypeType.LabelStyle,
            "Seconds" to TypeType.Int32,
            "Text" to TypeType.String,
        )
    ),
    Label(
        mapOf(
            "Style" to TypeType.LabelStyle,
            "Text" to TypeType.String,
            // This MUST be localisation string - aka "Message".
            "TextSpans" to TypeType.String,
        )
    ),
    TextButton(
        Elements.BUTTON_PROPERTIES + mapOf(
            "Text" to TypeType.String,
            "Style" to TypeType.TextButtonStyle,
            "Disabled" to TypeType.Boolean,
        )
    ),
    Button(
        Elements.BUTTON_PROPERTIES + mapOf(
            "Style" to TypeType.ButtonStyle,
            "LayoutMode" to TypeType.LayoutMode,

        )
    ),
    CheckBox(
        mapOf(
            "Style" to TypeType.CheckBoxStyle,
            "Value" to TypeType.Boolean,
            "Disabled" to TypeType.Boolean,
        )
    ),
    CheckBoxContainer(
        Group.properties
    ),
    TextField(
        mapOf(
            "Style" to TypeType.InputFieldStyle,
            "PlaceholderStyle" to TypeType.InputFieldStyle,
            "PlaceholderText" to TypeType.String,
            // CAN be float.
            "MaxLength" to TypeType.Int32,
            "IsReadOnly" to TypeType.Boolean,
            "PasswordChar" to TypeType.String,
            "Decoration" to TypeType.InputFieldDecorationStyle,
            "Value" to TypeType.String,
            "AutoFocus" to TypeType.Boolean,
            "AutoSelectAll" to TypeType.Boolean,
            "IsReadOnly" to TypeType.Boolean,
        )
    ),
    NumberField(
        mapOf(
            "Format" to TypeType.NumberFieldFormat,
            "Value" to TypeType.Float,
            "Style" to TypeType.InputFieldStyle,
            "PlaceholderStyle" to TypeType.InputFieldStyle,
            "PasswordChar" to TypeType.String,
            "Decoration" to TypeType.InputFieldDecorationStyle,
            "AutoFocus" to TypeType.Boolean,
            "AutoSelectAll" to TypeType.Boolean,
            "IsReadOnly" to TypeType.Boolean,
            "MaxLength" to TypeType.Int32,
        )
    ),
    DropdownBox(
        mapOf(
            "Style" to TypeType.DropdownBoxStyle,
            "NoItemsText" to TypeType.String,
            "PanelTitleText" to TypeType.String,
            "MaxSelection" to TypeType.Int32,
            "ShowLabel" to TypeType.Boolean,
            // TODO: Work out way to do List<String> - I don't think this is an actual array
            //  It will complain if we add [] to the UI file that it cannot convert between array<->list.
            // "SelectedValues" to TypeType.Array
            "Value" to TypeType.String,
            "Disabled" to TypeType.Boolean,
            "IsReadOnly" to TypeType.Boolean,
            "ShowSearchInput" to TypeType.Boolean,
            "ForcedLabel" to TypeType.String,
            "DisplayNonExistingValue" to TypeType.Boolean,
        )
    ),
    Sprite(
        mapOf(
            "TexturePath" to TypeType.String,
            "Frame" to TypeType.SpriteFrame,
            "FramesPerSecond" to TypeType.Int32,
            "IsPlaying" to TypeType.Boolean,
            "AutoPlay" to TypeType.Boolean,
            "Angle" to TypeType.Float,
            "RepeatCount" to TypeType.Int32,
        )
    ),
    CompactTextField(
        mapOf(
            "CollapsedWidth" to TypeType.Int32,
            "ExpandedWidth" to TypeType.Int32,
            "PlaceholderText" to TypeType.String,
            "Style" to TypeType.InputFieldStyle,
            "PlaceholderStyle" to TypeType.InputFieldStyle,
            "Decoration" to TypeType.InputFieldDecorationStyle,
            "Value" to TypeType.String,
            // Single character only... TODO: New primitive?
            "PasswordChar" to TypeType.String,
            "AutoFocus" to TypeType.String,
            "AutoSelectAll" to TypeType.String,
            "IsReadOnly" to TypeType.String,
            "MaxLength" to TypeType.Int32,
        )
    ),
    FloatSlider(
        mapOf(
            "Style" to TypeType.SliderStyle,
            "Min" to TypeType.Float,
            "Max" to TypeType.Float,
            "Step" to TypeType.Float,
            "Value" to TypeType.Float,
        )
    ),
    MultilineTextField(
        mapOf(
            "Style" to TypeType.InputFieldStyle,
            "PlaceholderStyle" to TypeType.InputFieldStyle,
            "PlaceholderText" to TypeType.String,
            "MaxVisibleLines" to TypeType.Int32,
            "MaxLength" to TypeType.Int32,
            "AutoGrow" to TypeType.Boolean,
            "ContentPadding" to TypeType.Padding,
            "ScrollbarStyle" to TypeType.ScrollbarStyle,
            "Value" to TypeType.String,
            "AutoFocus" to TypeType.Boolean,
            "AutoSelectAll" to TypeType.Boolean,
            "IsReadOnly" to TypeType.Boolean,
            "MaxLength" to TypeType.Int32,
            "MaxLines" to TypeType.Int32,
            "MaxVisibleLines" to TypeType.Int32,
            "ContentPadding" to TypeType.PatchStyle,
            "PlaceholderText" to TypeType.String,
        )
    ),
    ColorPickerDropdownBox(
        mapOf(
            "Style" to TypeType.ColorPickerDropdownBoxStyle,
            "Format" to TypeType.ColorFormat,
            "DisplayTextField" to TypeType.Boolean,
            "ResetTransparencyWhenChangingColor" to TypeType.Boolean,
            "IsReadOnly" to TypeType.Boolean,
        )
    ),
    CircularProgressBar(
        mapOf(
            "Value" to TypeType.Float,
            // Path to texture for background of circular pg.
            "MaskTexturePath" to TypeType.String,
            "Color" to TypeType.Color,
        )
    ),
    CodeEditor(
        mapOf(
            "ScrollbarStyle" to TypeType.ScrollbarStyle,
            "Value" to TypeType.String,
            "Language" to TypeType.CodeEditorLanguage,
            "LineNumberWidth" to TypeType.Int32,
            "LineNumberBackground" to TypeType.PatchStyle,
            "LineNumberTextColor" to TypeType.Color,
            "LineNumberPadding" to TypeType.Int32,
            "Style" to TypeType.InputFieldStyle,
            "PlaceholderStyle" to TypeType.InputFieldStyle,
            "Decoration" to TypeType.InputFieldDecorationStyle,
            "AutoFocus" to TypeType.Boolean,
            "AutoSelectAll" to TypeType.Boolean,
            "IsReadOnly" to TypeType.Boolean,
            "MaxLines" to TypeType.Int32,
            "MaxVisibleLines" to TypeType.Int32,
            "AutoGrow" to TypeType.Boolean,
            "ContentPadding" to TypeType.Padding,
            "PlaceholderText" to TypeType.String,
        )
    ),
    ColorOptionGrid(
      mapOf(
          // TODO: How does this even work? List<ColorOption> which is not defined
          //  anywhere.
          //"ColorOptions" to TypeType.ColorOption
          "ColorsPerRow" to TypeType.Int32,
          //"Selected" to TypeType.ColorOption
          "Style" to TypeType.ColorOptionGridStyle,
      )
    ),
    ProgressBar(
        mapOf(
            "Value" to TypeType.Float,
            "Bar" to TypeType.PatchStyle,

            // These CANNOT be patchstyles.
            "BarTexturePath" to TypeType.String,
            "EffectTexturePath" to TypeType.String,
            // This can and is valid on a circular and normal progress bar.
            "MaskTexturePath" to TypeType.String,

            "EffectWidth" to TypeType.Int32,
            "EffectHeight" to TypeType.Int32,
            "EffectOffset" to TypeType.Int32,
            "Direction" to TypeType.ProgressBarDirection,
            "Alignment" to TypeType.ProgressBarAlignment,
        )
    ),
    Slider(
        mapOf(
            "Style" to TypeType.SliderStyle,
            "Min" to TypeType.Int32,
            "Max" to TypeType.Int32,
            "Step" to TypeType.Int32,
            "Value" to TypeType.Int32,
            "IsReadOnly" to TypeType.Boolean,
        )
    ),
    ItemSlotButton(
        mapOf(
            "Style" to TypeType.ButtonStyle,
            "ItemId" to TypeType.String,
            "LayoutMode" to TypeType.LayoutMode,
            "Disabled" to TypeType.Boolean,
        )
    ),
    ItemSlot(
        mapOf(
            "ShowQualityBackground" to TypeType.Boolean,
            "ShowQuantity" to TypeType.Boolean,
            "ItemId" to TypeType.String,
            "Quantity" to TypeType.Int32,
            "ShowDurabilityBar" to TypeType.Boolean,
        )
    ),
    AssetImage(
        mapOf(
            "LayoutMode" to TypeType.LayoutMode,
            "AssetPath" to TypeType.String,
        )
    ),
    SceneBlur,
    ItemGrid(
        mapOf(
            "Style" to TypeType.ItemGridStyle,
            "SlotsPerRow" to TypeType.Int32,
            "RenderItemQualityBackground" to TypeType.Boolean,
            "AreItemsDraggable" to TypeType.Boolean,
            "KeepScrollPosition" to TypeType.Boolean,
            "ShowScrollbar" to TypeType.Boolean,
            "ScrollbarStyle" to TypeType.ScrollbarStyle,
            "InfoDisplay" to TypeType.ItemGridInfoDisplayMode,
            // TODO: work out arrays of item stacks and slots!
            // "ItemStacks" to TypeType.ClientItemStack
            "AdjacentInfoPaneGridWidth" to TypeType.Int32,
            "InventorySectionId" to TypeType.Int32,
            "AllowMaxStackDraggableItems" to TypeType.Boolean,
            "DisplayItemQuantity" to TypeType.Boolean,
        )
    ),
    ItemIcon(
        mapOf(
            "ItemId" to TypeType.String,
        )
    ),
    ColorPicker(
        mapOf(
            "Style" to TypeType.ColorPickerStyle,
            "Format" to TypeType.ColorFormat,
            "Value" to TypeType.Color,
            "DisplayTextField" to TypeType.Boolean,
            "ResetTransparencyWhenChangingColor" to TypeType.Boolean,
        )
    ),

    // CLIENT elements

    // NOT working on mods.
    BackgroundImage(
        mapOf(
            // Image is a path.
            "Image" to TypeType.String,
            // Ultrawide image is a path.
            "ImageUW" to TypeType.String
        )
    ),

    // WORKING on mods.
    TabNavigation(
        mapOf(
            "Style" to TypeType.TabNavigationStyle,
            "SelectedTab" to TypeType.String,
            "AllowUnselection" to TypeType.Boolean,
        )
    ),
    // WORKING on mods.
    ToggleButton(
        mapOf(
            "Style" to TypeType.ToggleButtonStyle,
            "CheckedStyle" to TypeType.ToggleButtonStyle,
            "IsChecked" to TypeType.Boolean,
            "Disabled" to TypeType.Boolean,

        )
    ),
    // NOT working on mods. Will kick client with "Exception has been thrown by the target of an invocation".
    ItemPreviewComponent(
        mapOf(
            // We assume float...
            "ItemScale" to TypeType.Float,
            "ItemId" to TypeType.String,
        )
    ),
    // NOT working on mods. Will kick client with "Exception has been thrown by the target of an invocation".
    CharacterPreviewComponent,
    // WORKING on mods.
    SliderNumberField(
        mapOf(
            "SliderStyle" to TypeType.SliderStyle,
            "NumberFieldContainerAnchor" to TypeType.Anchor,
            "NumberFieldStyle" to TypeType.InputFieldStyle,
            // If min is less than max, it will crash the client.
            "Min" to TypeType.Int32,
            "Max" to TypeType.Int32,
            "Step" to TypeType.Int32,
            "Value" to TypeType.Int32,
            "NumberFieldMaxDecimalPlaces" to TypeType.Int32,
            "NumberFieldDefaultValue" to TypeType.Float,
            "NumberFieldSuffix" to TypeType.Float,

        )
    ),
    // WORKING on mods.
    BlockSelector(
        mapOf(
            // Whilst float is accepted, it is traditionally an Int32.
            "Capacity" to TypeType.Float,
            "Style" to TypeType.BlockSelectorStyle,
            "Value" to TypeType.String,
        )
    ),
    // WORKING on mods.
    ReorderableListGrip(
        mapOf(
            "ScrollbarStyle" to TypeType.ScrollbarStyle,
            "LayoutMode" to TypeType.LayoutMode,
            "IsDragEnabled" to TypeType.Boolean,
        )
    ),
    // WORKING on mods.
    ReorderableList(
        mapOf(
            "ScrollbarStyle" to TypeType.ScrollbarStyle,
            "LayoutMode" to TypeType.LayoutMode,
            "DropIndicatorAnchor" to TypeType.Anchor,
            "DropIndicatorBackground" to TypeType.PatchStyle,
        )
    ),
    // WORKING on mods - MUST be a child of a TabNavigation element otherwise client crashes (NullReferenceException)
    TabButton(
        mapOf(
            "Id" to TypeType.String,
            "Icon" to TypeType.PatchStyle,
            "IconSelected" to TypeType.PatchStyle,
            "IconAnchor" to TypeType.Anchor,
            "LayoutMode" to TypeType.LayoutMode,
            "Text" to TypeType.String,
            "Disabled" to TypeType.Boolean,
            "Style" to TypeType.ButtonStyle,
        )
    ),
    // WORKING on mods.
    FloatSliderNumberField(
        mapOf(
            "SliderStyle" to TypeType.SliderStyle,
            "NumberFieldContainerAnchor" to TypeType.Anchor,
            "NumberFieldStyle" to TypeType.InputFieldStyle,
            "NumberFieldMaxDecimalPlaces" to TypeType.Int32,
            "NumberFieldDefaultValue" to TypeType.Float,
            // If min is less than max, it will crash the client.
            "Min" to TypeType.Float,
            "Max" to TypeType.Float,
            "Step" to TypeType.Float,
            "Value" to TypeType.Float,
        )
    ),
    // WORKING on mods.
    ActionButton( // TODO: Should we create common properties for all buttons?
        Elements.BUTTON_PROPERTIES + mapOf(
            "KeyBindingLabel" to TypeType.String,
            "Alignment" to TypeType.Alignment,
            "ActionName" to TypeType.String,
            "BindingModifier1Label" to TypeType.String,
            "BindingModifier2Label" to TypeType.String,
            "IsAvailable" to TypeType.Boolean,
            "IsHoldBinding" to TypeType.Boolean,
            "LayoutMode" to TypeType.LayoutMode,
        )
    ),
    BackButton(
        ActionButton.properties
    ),
    Panel(
        mapOf(
            "LayoutMode" to TypeType.LayoutMode,
            "ScrollbarStyle" to TypeType.ScrollbarStyle,
        )
    ),
    // WORKING on mods.
    LabeledCheckBox(
        mapOf(
            "Style" to TypeType.LabeledCheckBoxStyle,
            "Value" to TypeType.Boolean,
            "Disabled" to TypeType.Boolean,
        )
    ),
    // DOES NOT work on mods, unknown node type.
    PlayerPreviewComponent(
        mapOf(
            "Scale" to TypeType.Float,
        )
    ),
    // WORKING on mods.
    HotkeyLabel(
        mapOf(
            "InputBindingKey" to TypeType.String,
            "InputBindingKeyPrefix" to TypeType.String,
            "InputBindingKeyPrefixBinding" to TypeType.String,
        )
    ),
    // WORKING in mods.
    MenuItem(
        mapOf(
            "Text" to TypeType.String,
            // MESSAGE data type.
            "TextSpans" to TypeType.String,
            "PopupStyle" to TypeType.PopupStyle,
            "Style" to TypeType.TextButtonStyle,
            "SelectedStyle" to TypeType.TextButtonStyle,
            "Icon" to TypeType.PatchStyle,
            "IconAnchor" to TypeType.Anchor,
            "IsSelected" to TypeType.Boolean,
            "Disabled" to TypeType.Boolean,
        )
    ),
    DropdownEntry(
        mapOf(
            "Text" to TypeType.String,
            "LayoutMode" to TypeType.LayoutMode,
            "Value" to TypeType.String,
            "Selected" to TypeType.Boolean,
            "Disabled" to TypeType.Boolean,
            "Style" to TypeType.ButtonStyle,
        )
    ),
    DynamicPane(
        mapOf(
            "LayoutMode" to TypeType.LayoutMode,
            "ScrollbarStyle" to TypeType.ScrollbarStyle,
            "MinSize" to TypeType.Int32,
            "ResizeAt" to TypeType.ResizeType,
            "ResizerSize" to TypeType.Int32,
            "ResizerBackground" to TypeType.PatchStyle,
        )
    ),
    DynamicPaneContainer(
        mapOf(
            "LayoutMode" to TypeType.LayoutMode,
            "ScrollbarStyle" to TypeType.ScrollbarStyle,
        )
    ),

    ;

    val properties: Map<String, TypeType>

    constructor() : this(emptyMap())
    constructor(properties: Map<String, TypeType>) {
        this.properties = Elements.COMMON_PROPERTIES + properties
    }

    fun allowsChildren(): Boolean {
        return when (this) {
            SceneBlur -> false
            else -> true
        }
    }


    fun displayFullStructure(): String {
        return "element " + this.name + " {\n" + properties.map { (key, value) -> "   $key: ${value.name}\n" }.joinToString("") + "}"
    }
}