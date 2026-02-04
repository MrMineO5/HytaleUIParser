# Type Reference

This document provides a complete reference of all type types supported by the Hytale UI Parser.

## Table of Contents

- [Primitive Types](#primitive-types)
  - [String](#string)
  - [Int32](#int32)
  - [Float](#float)
  - [Boolean](#boolean)
  - [Color](#color)
- [Enum Types](#enum-types)
  - [LayoutMode](#layoutmode)
  - [Alignment](#alignment)
  - [Direction](#direction)
  - [ProgressBarDirection](#progressbardirection)
  - [ProgressBarAlignment](#progressbaralignment)
  - [ColorFormat](#colorformat)
  - [ItemGridInfoDisplayMode](#itemgridinfodisplaymode)
  - [InputFieldButtonSide](#inputfieldbuttonside)
- [Struct Types](#struct-types)
  - [Anchor](#anchor)
  - [Padding](#padding)
  - [PatchStyle](#patchstyle)
  - [LabelStyle](#labelstyle)
  - [ButtonStyle](#buttonstyle)
  - [ButtonStyleState](#buttonstylestate)
  - [TextButtonStyle](#textbuttonstyle)
  - [TextButtonStyleState](#textbuttonstylestate)
  - [CheckBoxStyle](#checkboxstyle)
  - [CheckBoxStyleState](#checkboxstylestate)
  - [SliderStyle](#sliderstyle)
  - [InputFieldStyle](#inputfieldstyle)
  - [InputFieldDecorationStyle](#inputfielddecorationstyle)
  - [InputFieldDecorationStyleState](#inputfielddecorationstylestate)
  - [InputFieldIcon](#inputfieldicon)
  - [InputFieldButtonStyle](#inputfieldbuttonstyle)
  - [ScrollbarStyle](#scrollbarstyle)
  - [TextTooltipStyle](#texttooltipstyle)
  - [TabNavigationStyle](#tabnavigationstyle)
  - [TabStyle](#tabstyle)
  - [TabStateStyle](#tabstatestyle)
  - [DropdownBoxSounds](#dropdownboxsounds)
  - [DropdownBoxStyle](#dropdownboxstyle)
  - [ItemGridStyle](#itemgridstyle)
  - [ColorPickerStyle](#colorpickerstyle)
  - [ColorPickerDropdownBoxStateBackground](#colorpickerdropdownboxstatebackground)
  - [ColorPickerDropdownBoxStyle](#colorpickerdropdownboxstyle)
  - [SoundStyle](#soundstyle)
  - [SoundsStyle](#soundsstyle)
  - [ButtonSounds](#buttonsounds)
  - [SpriteFrame](#spriteframe)
  - [NumberFieldFormat](#numberfieldformat)
  - [LabeledCheckBoxStyle](#labeledcheckboxstyle)
  - [LabeledCheckBoxStyleState](#labeledcheckboxstylestate)
  - [BlockSelectorStyle](#blockselectorstyle)
  - [MenuItemStyle](#menuitemstyle)
  - [PopupStyle](#popupstyle)
  - [CheckedStyle](#checkedstyle)
  - [SubMenuItemStyleState](#submenuitemstylestate)
  - [SubMenuItemStyle](#submenuitemstyle)
  - [FileDropdownBoxStyle](#filedropdownboxstyle)
  - [PopupMenuLayerStyle](#popupmenulayerstyle)

---

## Primitive Types

### String
Text value, must be quoted.

**Examples:**
```
Text: "Hello World";
Path: "../Common.ui";
```

**Validation:**
- Must be enclosed in double quotes
- Can be empty: `""`

### Int32
32-bit integer value.

**Examples:**
```
Width: 100;
Height: 50;
FontSize: 14;
```

**Validation:**
- Must be unquoted
- Must be parseable as integer or float
- Can be negative

### Float
Floating-point value.

**Examples:**
```
Opacity: 0.5;
Scale: 1.5;
Volume: 0.75;
```

**Validation:**
- Must be unquoted
- Must be parseable as float
- Can be negative

### Boolean
Boolean value.

**Examples:**
```
Visible: true;
Disabled: false;
```

**Validation:**
- Must be unquoted
- Must be exactly `true` or `false`

### Color
Hexadecimal color value with optional opacity.

**Syntax:** `#<hex>` or `#<hex>(<opacity>)`

**Examples:**
```
TextColor: #FF0000;
Background: #00FF00(0.5);
BorderColor: #0000FF(1.0);
```

**Validation:**
- Must start with `#`
- Hex value must be alphanumeric
- Opacity is float between 0.0 and 1.0

---

## Enum Types

### LayoutMode
Defines layout behavior.

**Valid Values:**
- `TopScrolling`
- `MiddleCenter`
- `Left`
- `Right`
- `Full`
- `Middle`
- `Bottom`
- `BottomScrolling`
- `CenterMiddle`
- `Top`
- `LeftCenterWrap`
- `Center`

### Alignment
Alignment direction.

**Valid Values:**
- `Top`
- `Bottom`
- `Left`
- `Right`
- `TopLeft`

### Direction
Generic direction.

**Valid Values:**
- `Center`
- `Start`
- `End`

### ProgressBarDirection
Progress bar fill direction.

**Valid Values:** Same as `Direction`

### ProgressBarAlignment
Progress bar alignment.

**Valid Values:**
- `Vertical`
- `Horizontal`

### ColorFormat
Color format specification.

**Valid Values:**
- `Rgb`
- `Rgba`

### ItemGridInfoDisplayMode
Item grid info display mode.

**Valid Values:**
- `None`

### InputFieldButtonSide
Input field button positioning.

**Valid Values:**
- `Left`
- `Right`

---

## Struct Types

### Anchor
Defines element positioning and sizing.

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `Left` | `Int32` | Left offset |
| `Right` | `Int32` | Right offset |
| `Top` | `Int32` | Top offset |
| `Bottom` | `Int32` | Bottom offset |
| `Width` | `Int32` | Fixed width |
| `Height` | `Int32` | Fixed height |
| `MinWidth` | `Int32` | Minimum width |
| `MaxWidth` | `Int32` | Maximum width |
| `Full` | `Int32` | All sides |
| `Horizontal` | `Int32` | Left and right |
| `Vertical` | `Int32` | Top and bottom |

**Note:** Fields can accept floats but may be rounded/cast

**Examples:**
```
Anchor: (Left: 10, Top: 20, Width: 100, Height: 50);
Anchor: (Full: 0);
```

### Padding
Defines internal spacing.

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `Left` | `Int32` | Left padding |
| `Right` | `Int32` | Right padding |
| `Top` | `Int32` | Top padding |
| `Bottom` | `Int32` | Bottom padding |
| `Horizontal` | `Int32` | Left and right |
| `Vertical` | `Int32` | Top and bottom |
| `Full` | `Int32` | All sides |

**Special Case:** A single number represents `Full`:
```
Padding: 10;  // Same as Padding: (Full: 10);
```

### PatchStyle
9-patch style for backgrounds and images.

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `TexturePath` | `String` | Texture file path |
| `VerticalBorder` | `Int32` | Vertical border size |
| `HorizontalBorder` | `Int32` | Horizontal border size |
| `Border` | `Int32` | All border sizes |
| `Color` | `Color` | Tint color |
| `Anchor` | `Anchor` | Positioning |

**Special Case:** Can be replaced by:
- A color value: `Background: #FF0000;`
- A texture path: `Background: "path/to/texture.png";`

**Example:**
```
Background: (
    TexturePath: "UI/button.png",
    Border: 4,
    Color: #FFFFFF
);
```

### LabelStyle
Text styling properties.

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `FontSize` | `Int32` | Font size |
| `FontName` | `String` | Font family |
| `LetterSpacing` | `Float` | Letter spacing |
| `TextColor` | `Color` | Text color |
| `RenderBold` | `Boolean` | Bold rendering |
| `RenderUppercase` | `Boolean` | Uppercase transform |
| `RenderItalics` | `Boolean` | Italic rendering |
| `Alignment` | `Direction` | Text alignment |
| `HorizontalAlignment` | `Direction` | Horizontal alignment |
| `VerticalAlignment` | `Direction` | Vertical alignment |
| `OutlineColor` | `Color` | Text outline color |
| `Wrap` | `Boolean` | Text wrapping |

### ButtonStyle
Button appearance for all states.

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `Default` | `ButtonStyleState` | Normal state |
| `Hovered` | `ButtonStyleState` | Hovered state |
| `Pressed` | `ButtonStyleState` | Pressed state |
| `Disabled` | `ButtonStyleState` | Disabled state |
| `Sounds` | `ButtonSounds` | Button sounds |

### ButtonStyleState
Single button state styling.

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `Background` | `PatchStyle` | Background style |
| `LabelStyle` | `LabelStyle` | Text style |

### TextButtonStyle
Text button styling (same structure as `ButtonStyle`).

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `Default` | `TextButtonStyleState` | Normal state |
| `Hovered` | `TextButtonStyleState` | Hovered state |
| `Pressed` | `TextButtonStyleState` | Pressed state |
| `Disabled` | `TextButtonStyleState` | Disabled state |
| `Sounds` | `ButtonSounds` | Button sounds |

### TextButtonStyleState
Same as `ButtonStyleState`.

### CheckBoxStyle
Checkbox styling.

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `Checked` | `CheckBoxStyleState` | Checked state |
| `Unchecked` | `CheckBoxStyleState` | Unchecked state |

### CheckBoxStyleState
Single checkbox state.

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `DefaultBackground` | `PatchStyle` | Normal background |
| `HoveredBackground` | `PatchStyle` | Hovered background |
| `PressedBackground` | `PatchStyle` | Pressed background |
| `DisabledBackground` | `PatchStyle` | Disabled background |
| `ChangedSound` | `SoundStyle` | State change sound |

### SliderStyle
Slider appearance.

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `Background` | `PatchStyle` | Track background |
| `Handle` | `String` | Handle texture path |
| `HandleWidth` | `Int32` | Handle width |
| `HandleHeight` | `Int32` | Handle height |
| `Sounds` | `ButtonSounds` | Slider sounds |

### InputFieldStyle
Text input styling.

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `TextColor` | `Color` | Text color |
| `FontSize` | `Int32` | Font size |
| `RenderBold` | `Boolean` | Bold rendering |
| `RenderItalics` | `Boolean` | Italic rendering |
| `RenderUppercase` | `Boolean` | Uppercase transform |

### InputFieldDecorationStyle
Input field decoration states.

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `Default` | `InputFieldDecorationStyleState` | Normal state |
| `Focused` | `InputFieldDecorationStyleState` | Focused state |

### InputFieldDecorationStyleState
Single decoration state.

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `Background` | `PatchStyle` | Background style |
| `Icon` | `InputFieldIcon` | Field icon |
| `ClearButtonStyle` | `InputFieldButtonStyle` | Clear button |

### InputFieldIcon
Icon configuration for input fields.

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `Texture` | `PatchStyle` | Icon texture |
| `Width` | `Int32` | Icon width |
| `Height` | `Int32` | Icon height |
| `Offset` | `Int32` | Icon offset |
| `Side` | `InputFieldButtonSide` | Icon side |

### InputFieldButtonStyle
Input field button styling.

**Extends:** `InputFieldIcon`

**Additional Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `HoveredTexture` | `PatchStyle` | Hovered texture |
| `PressedTexture` | `PatchStyle` | Pressed texture |

### ScrollbarStyle
Scrollbar appearance.

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `OnlyVisibleWhenHovered` | `Boolean` | Auto-hide scrollbar |
| `Spacing` | `Int32` | Handle spacing |
| `Size` | `Int32` | Scrollbar size |
| `Background` | `PatchStyle` | Track background |
| `Handle` | `PatchStyle` | Handle style |
| `HoveredHandle` | `PatchStyle` | Hovered handle |
| `DraggedHandle` | `PatchStyle` | Dragged handle |

### TextTooltipStyle
Tooltip appearance.

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `Background` | `PatchStyle` | Tooltip background |
| `MaxWidth` | `Int32` | Maximum width |
| `LabelStyle` | `LabelStyle` | Text style |
| `Padding` | `Int32` | Internal padding |
| `Alignment` | `Alignment` | Tooltip alignment |

### TabNavigationStyle
Tab navigation styling.

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `TabStyle` | `TabStyle` | Normal tab style |
| `SelectedTabStyle` | `TabStyle` | Selected tab style |
| `TabSounds` | `ButtonSounds` | Tab sounds |
| `SeparatorAnchor` | `Anchor` | Separator positioning |
| `SeparatorBackground` | `PatchStyle` | Separator style |

### TabStyle
Tab button styling.

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `Default` | `TabStateStyle` | Normal state |
| `Hovered` | `TabStateStyle` | Hovered state |
| `Pressed` | `TabStateStyle` | Pressed state |

### TabStateStyle
Single tab state.

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `LabelStyle` | `LabelStyle` | Text style |
| `Padding` | `Padding` | Internal padding |
| `Background` | `PatchStyle` | Background style |
| `Overlay` | `PatchStyle` | Overlay style |
| `IconAnchor` | `Anchor` | Icon positioning |
| `Anchor` | `Anchor` | Tab positioning |
| `TooltipStyle` | `TextTooltipStyle` | Tooltip style |
| `IconOpacity` | `Float` | Icon opacity |
| `ContentMaskTexturePath` | `String` | Mask texture |

### DropdownBoxSounds
Sound effects for dropdown boxes.

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `Activate` | `SoundStyle` | Activation sound |
| `MouseHover` | `SoundStyle` | Hover sound |
| `Close` | `SoundStyle` | Close sound |

### DropdownBoxStyle
Dropdown menu styling.

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `DefaultBackground` | `PatchStyle` | Normal background |
| `HoveredBackground` | `PatchStyle` | Hovered background |
| `PressedBackground` | `PatchStyle` | Pressed background |
| `DefaultArrowTexturePath` | `String` | Normal arrow texture |
| `HoveredArrowTexturePath` | `String` | Hovered arrow texture |
| `PressedArrowTexturePath` | `String` | Pressed arrow texture |
| `ArrowWidth` | `Int32` | Arrow width |
| `ArrowHeight` | `Int32` | Arrow height |
| `LabelStyle` | `LabelStyle` | Main label style |
| `EntryLabelStyle` | `LabelStyle` | Entry text style |
| `NoItemsLabelStyle` | `LabelStyle` | Empty state text style |
| `SelectedEntryLabelStyle` | `LabelStyle` | Selected entry text style |
| `HorizontalPadding` | `Int32` | Horizontal padding |
| `PanelScrollbarStyle` | `ScrollbarStyle` | Panel scrollbar style |
| `PanelBackground` | `PatchStyle` | Panel background |
| `PanelPadding` | `Int32` | Panel padding |
| `PanelWidth` | `Int32` | Panel width |
| `PanelAlign` | `Alignment` | Panel alignment |
| `PanelOffset` | `Int32` | Panel offset |
| `EntryHeight` | `Int32` | Entry height |
| `EntryIconWidth` | `Int32` | Entry icon width |
| `EntryIconHeight` | `Int32` | Entry icon height |
| `EntriesInViewport` | `Int32` | Visible entries count |
| `HorizontalEntryPadding` | `Int32` | Entry horizontal padding |
| `HoveredEntryBackground` | `PatchStyle` | Hovered entry background |
| `PressedEntryBackground` | `PatchStyle` | Pressed entry background |
| `Sounds` | `DropdownBoxSounds` | Dropdown sounds |
| `EntrySounds` | `ButtonSounds` | Entry sounds |
| `FocusOutlineSize` | `Int32` | Focus outline size |
| `FocusOutlineColor` | `Color` | Focus outline color |
| `PanelTitleLabelStyle` | `LabelStyle` | Panel title style |
| `SelectedEntryIconBackground` | `PatchStyle` | Selected entry icon bg |
| `IconTexturePath` | `String` | Icon texture path |
| `IconWidth` | `Int32` | Icon width |
| `IconHeight` | `Int32` | Icon height |

### ItemGridStyle
Item grid appearance.

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `SlotSize` | `Int32` | Size of each slot |
| `SlotIconSize` | `Int32` | Size of item icons |
| `SlotSpacing` | `Int32` | Spacing between slots |
| `SlotBackground` | `PatchStyle` | Slot background |
| `QuantityPopupSlotOverlay` | `PatchStyle` | Quantity popup overlay |
| `BrokenSlotBackgroundOverlay` | `PatchStyle` | Broken item background |
| `BrokenSlotIconOverlay` | `PatchStyle` | Broken item icon overlay |
| `DefaultItemIcon` | `PatchStyle` | Default item icon |
| `DurabilityBar` | `String` | Durability bar UI path |
| `DurabilityBarBackground` | `PatchStyle` | Durability bar background |
| `DurabilityBarAnchor` | `Anchor` | Durability bar positioning |
| `DurabilityBarColorStart` | `Color` | Durability bar start color |
| `DurabilityBarColorEnd` | `Color` | Durability bar end color |
| `CursedIconPatch` | `PatchStyle` | Cursed item icon |
| `CursedIconAnchor` | `Anchor` | Cursed icon positioning |
| `ItemStackHoveredSound` | `SoundStyle` | Item hover sound |
| `ItemStackActivateSound` | `SoundStyle` | Item activate sound |

### ColorPickerStyle
Color picker widget styling.

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `OpacitySelectorBackground` | `String` | Opacity selector bg |
| `ButtonBackground` | `String` | Button background |
| `ButtonFill` | `String` | Button fill |
| `TextFieldDecoration` | `InputFieldDecorationStyle` | Field decoration |
| `TextFieldPadding` | `Padding` | Field padding |
| `TextFieldHeight` | `Int32` | Field height |

### ColorPickerDropdownBoxStateBackground
Background states for color picker dropdown.

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `Default` | `PatchStyle` | Normal background |
| `Hovered` | `PatchStyle` | Hovered background |
| `Pressed` | `PatchStyle` | Pressed background |

### ColorPickerDropdownBoxStyle
Dropdown color picker styling.

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `ColorPickerStyle` | `ColorPickerStyle` | Color picker widget style |
| `Background` | `ColorPickerDropdownBoxStateBackground` | Dropdown background states |
| `ArrowBackground` | `ColorPickerDropdownBoxStateBackground` | Arrow background states |
| `Overlay` | `ColorPickerDropdownBoxStateBackground` | Overlay states |
| `PanelBackground` | `PatchStyle` | Panel background |
| `PanelPadding` | `Padding` | Panel padding |
| `PanelWidth` | `Int32` | Panel width |
| `PanelOffset` | `Int32` | Panel offset |
| `ArrowAnchor` | `Anchor` | Arrow positioning |
| `Sounds` | `ButtonSounds` | Dropdown sounds |

### SoundStyle
Sound effect configuration.

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `SoundPath` | `String` | Sound file path |
| `MinPitch` | `Float` | Minimum pitch |
| `MaxPitch` | `Float` | Maximum pitch |
| `Volume` | `Float` | Volume level |

### SoundsStyle
Collection of UI sounds.

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `Activate` | `SoundStyle` | Activation sound |
| `MouseHover` | `SoundStyle` | Hover sound |
| `Close` | `SoundStyle` | Close sound |
| `Context` | `SoundStyle` | Context sound |

### ButtonSounds
Button-specific sounds (same as `SoundsStyle`).

### SpriteFrame
Sprite sheet frame configuration.

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `Width` | `Int32` | Frame width |
| `Height` | `Int32` | Frame height |
| `PerRow` | `Int32` | Frames per row |
| `Count` | `Int32` | Total frames |

### NumberFieldFormat
Number field formatting.

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `MaxDecimalPlaces` | `Int32` | Decimal precision |
| `Step` | `Float` | Step increment |
| `MinValue` | `Float` | Minimum value |
| `MaxValue` | `Float` | Maximum value |

### LabeledCheckBoxStyle
Labeled checkbox styling.

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `Checked` | `LabeledCheckBoxStyleState` | Checked state |
| `Unchecked` | `LabeledCheckBoxStyleState` | Unchecked state |

### LabeledCheckBoxStyleState
Single labeled checkbox state.

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `DefaultBackground` | `PatchStyle` | Normal background |
| `HoveredBackground` | `PatchStyle` | Hovered background |
| `PressedBackground` | `PatchStyle` | Pressed background |
| `Text` | `String` | Label text |
| `DefaultLabelStyle` | `LabelStyle` | Normal text style |
| `HoveredLabelStyle` | `LabelStyle` | Hovered text style |
| `PressedLabelStyle` | `LabelStyle` | Pressed text style |
| `ChangedSound` | `SoundStyle` | Change sound |

### BlockSelectorStyle
Block selector styling.

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `ItemGridStyle` | `ItemGridStyle` | Grid styling |
| `SlotDropIcon` | `PatchStyle` | Drop icon |
| `SlotDeleteIcon` | `PatchStyle` | Delete icon |
| `SlotHoverOverlay` | `PatchStyle` | Hover overlay |

### MenuItemStyle
Menu item styling.

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `Default` | `PatchStyle` | Normal style |
| `Hovered` | `PatchStyle` | Hovered style |

### PopupStyle
Popup menu styling.

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `Background` | `Color` | Background color |
| `ButtonPadding` | `Padding` | Button padding |
| `Padding` | `Padding` | Content padding |
| `TooltipStyle` | `TextTooltipStyle` | Tooltip style |
| `ButtonStyle` | `ButtonStyle` | Button style |
| `SelectedButtonStyle` | `ButtonStyle` | Selected button style |

### CheckedStyle
Checkbox checked state styling (used internally).

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `Default` | `CheckBoxStyle` | Normal state |
| `Hovered` | `CheckBoxStyle` | Hovered state |
| `Pressed` | `CheckBoxStyle` | Pressed state |
| `Sounds` | `SoundsStyle` | Checkbox sounds |

### SubMenuItemStyleState
Single submenu item state.

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `Background` | `PatchStyle` | Background style |
| `LabelStyle` | `LabelStyle` | Label text style |
| `BindingLabelStyle` | `LabelStyle` | Key binding label style |

### SubMenuItemStyle
Submenu item styling.

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `Default` | `SubMenuItemStyleState` | Normal state |
| `Hovered` | `SubMenuItemStyleState` | Hovered state |
| `Pressed` | `SubMenuItemStyleState` | Pressed state |
| `Disabled` | `SubMenuItemStyleState` | Disabled state |
| `Sounds` | `ButtonSounds` | Button sounds |

### FileDropdownBoxStyle
File dropdown box styling.

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `DefaultBackground` | `PatchStyle` | Normal background |
| `HoveredBackground` | `PatchStyle` | Hovered background |
| `PressedBackground` | `PatchStyle` | Pressed background |
| `DefaultArrowTexturePath` | `String` | Normal arrow texture |
| `HoveredArrowTexturePath` | `String` | Hovered arrow texture |
| `PressedArrowTexturePath` | `String` | Pressed arrow texture |
| `ArrowWidth` | `Int32` | Arrow width |
| `ArrowHeight` | `Int32` | Arrow height |
| `LabelStyle` | `LabelStyle` | Label style |
| `HorizontalPadding` | `Int32` | Horizontal padding |
| `PanelAlign` | `Alignment` | Panel alignment |
| `PanelOffset` | `Int32` | Panel offset |
| `Sounds` | `DropdownBoxSounds` | Dropdown sounds |

### PopupMenuLayerStyle
Popup menu layer styling.

**Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `Background` | `PatchStyle` | Menu background |
| `Padding` | `Int32` | Internal padding (integer only) |
| `BaseHeight` | `Int32` | Base menu height |
| `MaxWidth` | `Int32` | Maximum width |
| `TitleStyle` | `LabelStyle` | Title text style |
| `TitleBackground` | `PatchStyle` | Title background |
| `RowHeight` | `Int32` | Menu row height |
| `ItemLabelStyle` | `LabelStyle` | Item text style |
| `ItemPadding` | `Padding` | Item padding |
| `ItemBackground` | `PatchStyle` | Item background |
| `ItemIconSize` | `Int32` | Item icon size |
| `HoveredItemBackground` | `PatchStyle` | Hovered item background |
| `PressedItemBackground` | `PatchStyle` | Pressed item background |
| `ItemSounds` | `SoundsStyle` | Item sounds |
