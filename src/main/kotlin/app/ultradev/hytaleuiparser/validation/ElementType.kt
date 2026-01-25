package app.ultradev.hytaleuiparser.validation

import app.ultradev.hytaleuiparser.validation.types.TypeType

enum class ElementType {
    Group(

    ),
    TimerLabel,
    Label,
    TextButton,
    Button,
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
    CircularProgressBar,
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
    BackgroundImage,
    TabNavigation,
    ToggleButton,
    ItemPreviewComponent,
    
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