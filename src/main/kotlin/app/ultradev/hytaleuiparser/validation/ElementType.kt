package app.ultradev.hytaleuiparser.validation

import app.ultradev.hytaleuiparser.validation.types.TypeType
import kotlin.collections.component1
import kotlin.collections.component2

enum class ElementType {
    Group(
        mapOf(
            "ScrollbarStyle" to TypeType.ScrollbarStyle,
            "MaskTexturePath" to TypeType.String,
            "AutoScrollDown" to TypeType.Boolean,
            "KeepScrollPosition" to TypeType.Boolean,
        )
    ),
    TimerLabel(
        mapOf(
            "Style" to TypeType.LabelStyle,
            "Seconds" to TypeType.Int32
        )
    ),
    Label(
        mapOf(
            "Style" to TypeType.LabelStyle,
            "Text" to TypeType.String,
            "MaskTexturePath" to TypeType.String,
            // This MUST be localisation string - aka "Message".
            "TextSpans" to TypeType.String,
        )
    ),
    TextButton(
        Elements.BUTTON_PROPERTIES + mapOf(
            "Text" to TypeType.String,
            "MaskTexturePath" to TypeType.String,
            "Style" to TypeType.TextButtonStyle,
        )
    ),
    Button(
        Elements.BUTTON_PROPERTIES + mapOf(
            "MaskTexturePath" to TypeType.String,
            "Style" to TypeType.ButtonStyle,
        )
    ),
    CheckBox(
        mapOf(
            "Style" to TypeType.CheckBoxStyle,
            "Value" to TypeType.Boolean,
        )
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
            "Decoration" to TypeType.InputFieldDecorationStyle
        )
    ),
    NumberField(
        mapOf(
            "Format" to TypeType.NumberFieldFormat,
            "Value" to TypeType.Float,
            "Style" to TypeType.InputFieldStyle,
            "PlaceholderStyle" to TypeType.InputFieldStyle,
        )
    ),
    DropdownBox(
        mapOf(
            "Style" to TypeType.DropdownBoxStyle,
            "NoItemsText" to TypeType.String,
            "PanelTitleText" to TypeType.String,
            "MaxSelection" to TypeType.Int32,
            "ShowLabel" to TypeType.Boolean,
        )
    ),
    Sprite(
        mapOf(
            "TexturePath" to TypeType.String,
            "Frame" to TypeType.SpriteFrame,
            "FramesPerSecond" to TypeType.Int32,
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
        )
    ),
    BackButton,
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
        )
    ),
    ColorPickerDropdownBox(
        mapOf(
            "Style" to TypeType.ColorPickerDropdownBoxStyle,
            "Format" to TypeType.ColorFormat,
            "DisplayTextField" to TypeType.Boolean,
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
    ProgressBar(
        mapOf(
            "Value" to TypeType.Float,
            "Bar" to TypeType.PatchStyle,
            
            // These CANNOT be patchstyles.
            "BarTexturePath" to TypeType.String,
            "EffectTexturePath" to TypeType.String,
            // This can and is valid on a circular and normal progress bar.
            "MaskTexturePath" to TypeType.String,
            
            "EffectWidth" to TypeType.Float,
            "EffectHeight" to TypeType.Float,
            "EffectOffset" to TypeType.Float,
            "Direction" to TypeType.ProgressBarDirection,
            "Alignment" to TypeType.ProgressBarAlignment,
        )
    ),
    Slider(
        mapOf(
            "Style" to TypeType.SliderStyle,
            "Min" to TypeType.Float,
            "Max" to TypeType.Float,
            "Step" to TypeType.Float,
            "Value" to TypeType.Float,
        )
    ),
    ItemSlotButton(
        mapOf(
            "Style" to TypeType.ButtonStyle,
            "ItemId" to TypeType.String,
        )
    ),
    ItemSlot(
        mapOf(
            "ShowQualityBackground" to TypeType.Boolean,
            "ShowQuantity" to TypeType.Boolean,
            "ItemId" to TypeType.String,
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
            "Style" to TypeType.ButtonStyle,
            "CheckedStyle" to TypeType.ButtonStyle,
        )
    ),
    // NOT working on mods. Will kick client with "Exception has been thrown by the target of an invocation".
    ItemPreviewComponent(
        mapOf(
            // We assume float...
            "ItemScale" to TypeType.Float,
        )
    ),
    // NOT working on mods. Will kick client with "Exception has been thrown by the target of an invocation".
    CharacterPreviewComponent,
    // WORKING on mods.
    SliderNumberField(
        mapOf(
            "SliderStyle" to TypeType.SliderStyle,
            "Style" to TypeType.InputFieldStyle,
            "NumberFieldContainerAnchor" to TypeType.Anchor,
            "NumberFieldStyle" to TypeType.InputFieldStyle,
            // If min is less than max, it will crash the client.
            "Min" to TypeType.Float,
            "Max" to TypeType.Float,
            "Step" to TypeType.Float,
            "Value" to TypeType.Float,
            
        )
    ),
    // WORKING on mods.
    BlockSelector(
        mapOf(
            // Whilst float is accepted, it is traditionally an Int32.
            "Capacity" to TypeType.Float,
            "Style" to TypeType.BlockSelectorStyle,
        )
    ),
    // WORKING on mods.
    ReorderableListGrip,
    // WORKING on mods - MUST be a child of a TabNavigation element otherwise client crashes (NullReferenceException)
    TabButton(
        mapOf(
            "Id" to TypeType.String,
            "Icon" to TypeType.PatchStyle,
            "IconSelected" to TypeType.PatchStyle,
        )
    ),
    // WORKING on mods.
    FloatSliderNumberField(
        mapOf(
            "SliderStyle" to TypeType.SliderStyle,
            "NumberFieldContainerAnchor" to TypeType.Anchor,
            "NumberFieldStyle" to TypeType.InputFieldStyle,
            "NumberFieldMaxDecimalPlaces" to TypeType.Int32,
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
        )
    ),
    // CANNOT find panel in list of elements.
    Panel,
    // WORKING on mods.
    LabeledCheckBox(
        mapOf(
            "Style" to TypeType.LabeledCheckBoxStyle,
        )
    ),
    // DOES NOT work on mods, unknown node type.
    PlayerPreviewComponent(
        mapOf(
            "Scale" to TypeType.Float,
        )
    ),
    // WORKING on mods.
    HotkeyLabel(mapOf(
        "InputBindingKey" to TypeType.String,
        "InputBindingKeyPrefix" to TypeType.String,
    )),
    // WORKING in mods.
    MenuItem(
        mapOf(
            "Text" to TypeType.String,
            "PopupStyle" to TypeType.PopupStyle,
            "Style" to TypeType.MenuItemStyle,
            "SelectedStyle" to TypeType.MenuItemStyle,
            "Icon" to TypeType.PatchStyle,
            "IconAnchor" to TypeType.Anchor
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