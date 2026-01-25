package app.ultradev.hytaleuiparser.validation

import app.ultradev.hytaleuiparser.validation.types.TypeType

enum class ElementType {
    Group(
        mapOf(
            "LayoutMode" to TypeType.LayoutMode,
            "ScrollbarStyle" to TypeType.ScrollbarStyle,
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
            "TextTooltipStyle" to TypeType.TextTooltipStyle,
            "TooltipTextSpans" to TypeType.String,
            "TextSpans" to TypeType.String,
        )
    ),
    TextButton(
        mapOf(
            "Text" to TypeType.String,
            "TooltipText" to TypeType.String,
            "TextTooltipStyle" to TypeType.TextTooltipStyle,
            "Disabled" to TypeType.Boolean,
            "MaskTexturePath" to TypeType.String,
            "Style" to TypeType.ButtonStyle,
        )
    ),
    Button(
        mapOf(
            "TooltipText" to TypeType.String,
            "TextTooltipStyle" to TypeType.TextTooltipStyle,
            "Disabled" to TypeType.Boolean,
            "MaskTexturePath" to TypeType.String,
            "Style" to TypeType.ButtonStyle,
        )
    ),
    CheckBox,
    TextField,
    NumberField,
    DropdownBox,
    Sprite,
    CompactTextField,
    BackButton,
    FloatSlider,
    MultilineTextField,
    ColorPickerDropdownBox,
    CircularProgressBar(
        mapOf(
            "Value" to TypeType.Float,
            // Path to texture for background of circular pg.
            "MaskTexturePath" to TypeType.String,
            "Color" to TypeType.String,
        )
    ),
    ProgressBar,
    Slider,
    ItemSlotButton,
    ItemSlot,
    AssetImage,
    SceneBlur,
    ItemGrid,
    ItemIcon,
    ColorPicker,
    
    // CLIENT elements.
    BackgroundImage(
        mapOf(
            // Image is a path.
            "Image" to TypeType.String,
            // Ultrawide image is a path.
            "ImageUW" to TypeType.String
        )
    ),
    TabNavigation,
    ToggleButton,
    ItemPreviewComponent,
    CharacterPreviewComponent,
    SliderNumberField,
    BlockSelector,
    ReorderableListGrip,
    TabButton,
    FloatSliderNumberField,
    ActionButton,
    Panel,
    LabeledCheckBox,
    PlayerPreviewComponent,
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