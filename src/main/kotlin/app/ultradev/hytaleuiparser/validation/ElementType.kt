package app.ultradev.hytaleuiparser.validation

import app.ultradev.hytaleuiparser.validation.types.TypeType

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
            "Seconds" to TypeType.Integer
        )
    ),
    Label(
        mapOf(
            "Style" to TypeType.LabelStyle,
            "Text" to TypeType.String,
            "MaskTexturePath" to TypeType.String,
            "TextSpans" to TypeType.String,
        )
    ),
    TextButton(
        Elements.BUTTON_PROPERTIES + mapOf(
            "Text" to TypeType.String,
            "MaskTexturePath" to TypeType.String,
            "Style" to TypeType.ButtonStyle,
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
            "MaxLength" to TypeType.Integer,
            "IsReadOnly" to TypeType.Boolean,
            "PasswordChar" to TypeType.String,
        )
    ),
    NumberField(
        mapOf(
            "Format" to TypeType.NumberFieldFormat,
            // TODO: Ellie uses Double's in Hyui for value/step/min/max. Confirm if float possible.
            "Value" to TypeType.Double,
            "Min" to TypeType.Double,
            "Max" to TypeType.Double,
            "Step" to TypeType.Double,
            "Style" to TypeType.InputFieldStyle,
            "PlaceholderStyle" to TypeType.InputFieldStyle,
        )
    ),
    DropdownBox(
        mapOf(
            "Style" to TypeType.DropdownBoxStyle,
            "NoItemsText" to TypeType.String,
            "PanelTitleText" to TypeType.String,
            "MaxSelection" to TypeType.Integer,
            "ShowLabel" to TypeType.Boolean,
        )
    ),
    Sprite(
        mapOf(
            "TexturePath" to TypeType.String,
            "Frame" to TypeType.SpriteFrame,
        )
    ),
    CompactTextField,
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
            "MaxVisibleLines" to TypeType.Integer,
            "MaxLength" to TypeType.Integer,
            "AutoGrow" to TypeType.Boolean,
            "ContentPadding" to TypeType.Padding,
        )
    ),
    ColorPickerDropdownBox(
        mapOf(
            "Style" to TypeType.ColorPickerDropdownBoxStyle,
            "Format" to TypeType.ColorPickerFormat,
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
            "BarTexturePath" to TypeType.String, // TODO: Why is this a thing? PatchStyle has a texture path...?
            "EffectTexturePath" to TypeType.String,
            "EffectWidth" to TypeType.Integer,
            "EffectHeight" to TypeType.Integer,
            "EffectOffset" to TypeType.Integer,
            "Direction" to TypeType.Direction,
            "Alignment" to TypeType.BarAlignment,
        )
    ),
    Slider(
        mapOf(
            "Style" to TypeType.SliderStyle,
            "Min" to TypeType.Integer,
            "Max" to TypeType.Integer,
            "Step" to TypeType.Float, // TODO: How can an integer slider have a float step???
            "Value" to TypeType.Integer,
        )
    ),
    ItemSlotButton(
        mapOf(
            "Style" to TypeType.ButtonStyle,
        )
    ),
    ItemSlot(
        mapOf(
            "ShowQualityBackground" to TypeType.Boolean,
            "ShowQuantity" to TypeType.Boolean,
        )
    ),
    AssetImage,
    SceneBlur,
    ItemGrid(
        mapOf(
            "Style" to TypeType.ItemGridStyle,
            "SlotsPerRow" to TypeType.Integer,
            "RenderItemQualityBackground" to TypeType.Boolean,
            "AreItemsDraggable" to TypeType.Boolean,
            "KeepScrollPosition" to TypeType.Boolean,
            "ShowScrollbar" to TypeType.Boolean,
            "ScrollbarStyle" to TypeType.ScrollbarStyle,
            "InfoDisplay" to TypeType.InfoDisplay,
        )
    ),
    ItemIcon,
    ColorPicker(
        mapOf(
            "Style" to TypeType.ColorPickerStyle,
            "Format" to TypeType.ColorPickerFormat,
        )
    ),

    // CLIENT elements.
    BackgroundImage(
        mapOf(
            // Image is a path.
            "Image" to TypeType.String,
            // Ultrawide image is a path.
            "ImageUW" to TypeType.String
        )
    ),
    TabNavigation(
        mapOf(
            "Style" to TypeType.TabNavigationStyle,
            "SelectedTab" to TypeType.String,
            "AllowUnselection" to TypeType.Boolean,
        )
    ),
    ToggleButton(
        mapOf(
            "Style" to TypeType.ButtonStyle,
            "CheckedStyle" to TypeType.ButtonStyle,
        )
    ),
    ItemPreviewComponent(
        mapOf(
            // We assume float...
            "ItemScale" to TypeType.Float,
        )
    ),
    CharacterPreviewComponent,
    SliderNumberField(
        mapOf(
            "SliderStyle" to TypeType.SliderStyle,
            "Style" to TypeType.InputFieldStyle,
            "NumberFieldContainerAnchor" to TypeType.Anchor,
            "NumberFieldStyle" to TypeType.InputFieldStyle,
            "Min" to TypeType.Double,
            "Max" to TypeType.Double,
            // TODO: Confirm step valid on slider number field.
            "Step" to TypeType.Double,
            "Value" to TypeType.Double,
            
        )
    ),
    BlockSelector(
        mapOf(
            "Capacity" to TypeType.Integer,
            "Style" to TypeType.BlockSelectorStyle,
        )
    ),
    ReorderableListGrip,
    TabButton(
        mapOf(
            "Id" to TypeType.String,
            // TODO: Confirm if patchstyle, or only string - seen only as string.
            "Icon" to TypeType.String,
            "IconSelected" to TypeType.String,
        )
    ),
    // WHY DOES THIS EXIST?
    FloatSliderNumberField(
        mapOf(
            "SliderStyle" to TypeType.SliderStyle,
            "Style" to TypeType.InputFieldStyle,
            "NumberFieldContainerAnchor" to TypeType.Anchor,
            "NumberFieldStyle" to TypeType.InputFieldStyle,
            "NumberFieldMaxDecimalPlaces" to TypeType.Integer,
            "Min" to TypeType.Float,
            "Max" to TypeType.Float,
            "Step" to TypeType.Float,
            "Value" to TypeType.Float,
        )
    ),
    ActionButton( // TODO: Should we create common properties for all buttons?
        Elements.BUTTON_PROPERTIES + mapOf(
            "KeyBindingLabel" to TypeType.String,
            "Alignment" to TypeType.Alignment,
            "ActionName" to TypeType.String,
        )
    ),
    Panel,
    LabeledCheckBox,
    PlayerPreviewComponent,
    HotkeyLabel(mapOf(
        "InputBindingKey" to TypeType.String,
        "InputBindingKeyPrefix" to TypeType.String,
    )),
    MenuItem(
        mapOf(
            "Text" to TypeType.String,
            "TextTooltipStyle" to TypeType.TextTooltipStyle,
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
}