package app.ultradev.hytaleuiparser.validation

import app.ultradev.hytaleuiparser.validation.types.TypeType
import java.util.*

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
        ),
        false
    ),
    Label(
        mapOf(
            "Style" to TypeType.LabelStyle,
            "Text" to TypeType.String,
            // This MUST be localisation string - aka "Message".
            "TextSpans" to TypeType.String,
        ),
        false
    ),
    TextButton(
        Elements.BUTTON_PROPERTIES + mapOf(
            "Text" to TypeType.String,
            "Style" to TypeType.TextButtonStyle,
            "Disabled" to TypeType.Boolean,
        ),
        false
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
        ),
        false
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
        ),
        false
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
        ),
        false
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
        ),
        requiredChildTypes = EnumSet.of(DropdownEntry)
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
        ),
        false
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
        ),
        false
    ),
    FloatSlider(
        mapOf(
            "Style" to TypeType.SliderStyle,
            "Min" to TypeType.Float,
            "Max" to TypeType.Float,
            "Step" to TypeType.Float,
            "Value" to TypeType.Float,
        ),
        false
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
        ),
        false
    ),
    ColorPickerDropdownBox(
        mapOf(
            "Style" to TypeType.ColorPickerDropdownBoxStyle,
            "Format" to TypeType.ColorFormat,
            "DisplayTextField" to TypeType.Boolean,
            "ResetTransparencyWhenChangingColor" to TypeType.Boolean,
            "IsReadOnly" to TypeType.Boolean,
        ),
        false
    ),
    CircularProgressBar(
        mapOf(
            "Value" to TypeType.Float,
            // Path to texture for background of circular pg.
            "MaskTexturePath" to TypeType.String,
            "Color" to TypeType.Color,
        ),
        false
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
        ),
        false
    ),

    // This is in the docs, but it... doesn't exist
    ColorOptionGrid(
        mapOf(
            // TODO: How does this even work? List<ColorOption> which is not defined
            //  anywhere.
            //"ColorOptions" to TypeType.ColorOption
            "ColorsPerRow" to TypeType.Int32,
            //"Selected" to TypeType.ColorOption
            "Style" to TypeType.ColorOptionGridStyle,
        ),
        false
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
        ),
        false
    ),
    Slider(
        mapOf(
            "Style" to TypeType.SliderStyle,
            "Min" to TypeType.Int32,
            "Max" to TypeType.Int32,
            "Step" to TypeType.Int32,
            "Value" to TypeType.Int32,
            "IsReadOnly" to TypeType.Boolean,
        ),
        false
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
        ),
        false
    ),
    AssetImage(
        mapOf(
            "LayoutMode" to TypeType.LayoutMode,
            "AssetPath" to TypeType.String,
        )
    ),
    SceneBlur(emptyMap(), false),
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
        ),
        false
    ),
    ItemIcon(
        mapOf(
            "ItemId" to TypeType.String,
        ),
        false
    ),
    ColorPicker(
        mapOf(
            "Style" to TypeType.ColorPickerStyle,
            "Format" to TypeType.ColorFormat,
            "Value" to TypeType.Color,
            "DisplayTextField" to TypeType.Boolean,
            "ResetTransparencyWhenChangingColor" to TypeType.Boolean,
        ),
        false
    ),

    BackgroundImage(
        mapOf(
            // Image is a path.
            "Image" to TypeType.String,
            // Ultrawide image is a path.
            "ImageUW" to TypeType.String
        ),
        clientOnly = true,
    ),
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
    TabNavigation(
        mapOf(
            "Style" to TypeType.TabNavigationStyle,
            "SelectedTab" to TypeType.String,
            "AllowUnselection" to TypeType.Boolean,
        ),
        requiredChildTypes = EnumSet.of(TabButton)
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

    // NOT working on mods. Invalid cast error on client
    //  TODO: Figure out if there is a set we can send to the client to fix
    ItemPreviewComponent(
        mapOf(
            // We assume float...
            "ItemScale" to TypeType.Float,
            "ItemId" to TypeType.String,
        ),
    ),

    // NOT working on mods. Invalid cast error on client
    //  TODO: Figure out if there is a set we can send to the client to fix
    CharacterPreviewComponent,

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
        ),
        false
    ),
    BlockSelector(
        mapOf(
            "Capacity" to TypeType.Float,
            "Style" to TypeType.BlockSelectorStyle,
            "Value" to TypeType.String,
        ),
        false
    ),
    ReorderableListGrip(
        mapOf(
            "ScrollbarStyle" to TypeType.ScrollbarStyle,
            "LayoutMode" to TypeType.LayoutMode,
            "IsDragEnabled" to TypeType.Boolean,
        )
    ),
    ReorderableList(
        mapOf(
            "ScrollbarStyle" to TypeType.ScrollbarStyle,
            "LayoutMode" to TypeType.LayoutMode,
            "DropIndicatorAnchor" to TypeType.Anchor,
            "DropIndicatorBackground" to TypeType.PatchStyle,
        )
    ),
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
        ),
        false
    ),
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
        ),
        false
    ),
    BackButton(
        ActionButton.properties,
        false
    ),
    Panel(
        mapOf(
            "LayoutMode" to TypeType.LayoutMode,
            "ScrollbarStyle" to TypeType.ScrollbarStyle,
        )
    ),
    LabeledCheckBox(
        mapOf(
            "Style" to TypeType.LabeledCheckBoxStyle,
            "Value" to TypeType.Boolean,
            "Disabled" to TypeType.Boolean,
        ),
        false
    ),
    PlayerPreviewComponent(
        mapOf(
            "Scale" to TypeType.Float,
        ),
        clientOnly = true
    ),
    HotkeyLabel(
        mapOf(
            "InputBindingKey" to TypeType.String,
            "InputBindingKeyPrefix" to TypeType.String,
            "InputBindingKeyPrefixBinding" to TypeType.String,
        ),
        false
    ),

    /**
     * Has no LayoutMode property but allows children
     *
     * Children are laid out equivalently to LayoutMode: Left
     */
    MenuItem(
        mapOf(
            "Text" to TypeType.String,
            // TODO: IList`1
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
    val allowsChildren: Boolean
    val clientOnly: Boolean

    val requiredChildTypes: Set<ElementType>

    constructor(
        properties: Map<String, TypeType> = emptyMap(),
        allowsChildren: Boolean = true,
        clientOnly: Boolean = false,
        requiredChildTypes: Set<ElementType> = emptySet()
    ) {
        this.properties = Elements.COMMON_PROPERTIES + properties
        this.allowsChildren = allowsChildren
        this.clientOnly = clientOnly
        this.requiredChildTypes = requiredChildTypes
    }

    @Deprecated(
        "Use allowsChildren field instead",
        replaceWith = ReplaceWith("this.allowsChildren")
    )
    fun allowsChildren(): Boolean {
        return allowsChildren
    }


    fun displayFullStructure(): String {
        return "element " + this.name + " {\n" + properties.map { (key, value) -> "   $key: ${value.name}\n" }
            .joinToString("") + "}"
    }
}