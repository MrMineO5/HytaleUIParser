# Element Reference

This document provides a complete reference of all element types supported by the Hytale UI Parser.

## Table of Contents

- [Common Properties](#common-properties)
- [Container Elements](#container-elements)
  - [Group](#group)
  - [Panel](#panel)
- [Button Elements](#button-elements)
  - [Button](#button)
  - [TextButton](#textbutton)
  - [ToggleButton](#togglebutton)
  - [ActionButton](#actionbutton)
  - [BackButton](#backbutton)
  - [ItemSlotButton](#itemslotbutton)
  - [TabButton](#tabbutton)
- [Text Elements](#text-elements)
  - [Label](#label)
  - [TimerLabel](#timerlabel)
  - [HotkeyLabel](#hotkeylabel)
- [Input Elements](#input-elements)
  - [TextField](#textfield)
  - [MultilineTextField](#multilinetextfield)
  - [CompactTextField](#compacttextfield)
  - [NumberField](#numberfield)
- [Selection Elements](#selection-elements)
  - [CheckBox](#checkbox)
  - [LabeledCheckBox](#labeledcheckbox)
  - [DropdownBox](#dropdownbox)
  - [TabNavigation](#tabnavigation)
- [Slider Elements](#slider-elements)
  - [Slider](#slider)
  - [FloatSlider](#floatslider)
  - [SliderNumberField](#slidernumberfield)
  - [FloatSliderNumberField](#floatslidernumberfield)
- [Progress Elements](#progress-elements)
  - [ProgressBar](#progressbar)
  - [CircularProgressBar](#circularprogressbar)
- [Visual Elements](#visual-elements)
  - [Sprite](#sprite)
  - [AssetImage](#assetimage)
  - [BackgroundImage](#backgroundimage)
  - [SceneBlur](#sceneblur)
- [Item Elements](#item-elements)
  - [ItemSlot](#itemslot)
  - [ItemIcon](#itemicon)
  - [ItemGrid](#itemgrid)
- [Color Elements](#color-elements)
  - [ColorPicker](#colorpicker)
  - [ColorPickerDropdownBox](#colorpickerdropdownbox)
- [Special Elements](#special-elements)
  - [BlockSelector](#blockselector)
  - [ReorderableListGrip](#reorderablelistgrip)
  - [MenuItem](#menuitem)
- [Preview Elements (Limited Mod Support)](#preview-elements-limited-mod-support)
  - [ItemPreviewComponent](#itempreviewcomponent)
  - [CharacterPreviewComponent](#characterpreviewcomponent)
  - [PlayerPreviewComponent](#playerpreviewcomponent)

---

## Common Properties

All elements support these common properties:

| Property               | Type               | Description                    |
|------------------------|--------------------|--------------------------------|
| `Anchor`               | `Anchor`           | Element positioning and sizing |
| `Padding`              | `Padding`          | Internal spacing               |
| `Background`           | `PatchStyle`       | Background styling             |
| `FlexWeight`           | `Int32`            | Flex layout weight             |
| `HitTestVisible`       | `Boolean`          | Whether element receives input |
| `Visible`              | `Boolean`          | Whether element is visible     |
| `LayoutMode`           | `LayoutMode`       | Layout behavior                |
| `TooltipText`          | `String`           | Tooltip text                   |
| `TextTooltipStyle`     | `TextTooltipStyle` | Tooltip styling                |
| `TooltipTextSpans`     | `String`           | Rich tooltip text              |
| `TextTooltipShowDelay` | `Float`            | Tooltip show delay             |

---

## Container Elements

### Group
Container for multiple child elements.

**Additional Properties:**

| Property | Type | Description |
|----------|------|-------------|
| `ScrollbarStyle` | `ScrollbarStyle` | Scrollbar appearance |
| `MaskTexturePath` | `String` | Texture for masking |
| `AutoScrollDown` | `Boolean` | Auto-scroll to bottom |
| `KeepScrollPosition` | `Boolean` | Maintain scroll position |

**Allows Children:** Yes

### Panel
Basic container element.

**Additional Properties:** None

**Allows Children:** Yes

---

## Button Elements

### Button
Basic button element.

**Additional Properties:**

| Property | Type | Description |
|----------|------|-------------|
| `Disabled` | `Boolean` | Whether button is disabled |
| `Style` | `ButtonStyle` | Button appearance |
| `MaskTexturePath` | `String` | Texture for masking |

**Allows Children:** Yes

### TextButton
Button with text label.

**Additional Properties:**

| Property | Type | Description |
|----------|------|-------------|
| `Disabled` | `Boolean` | Whether button is disabled |
| `Text` | `String` | Button text |
| `Style` | `TextButtonStyle` | Button appearance |
| `MaskTexturePath` | `String` | Texture for masking |

**Allows Children:** Yes

### ToggleButton
Button with toggle state.

**Additional Properties:**

| Property | Type | Description |
|----------|------|-------------|
| `Style` | `ButtonStyle` | Normal state style |
| `CheckedStyle` | `ButtonStyle` | Checked state style |

**Allows Children:** Yes

### ActionButton
Button bound to an action.

**Additional Properties:**

| Property | Type | Description |
|----------|------|-------------|
| `Disabled` | `Boolean` | Whether button is disabled |
| `KeyBindingLabel` | `String` | Key binding label |
| `Alignment` | `Alignment` | Text alignment |
| `ActionName` | `String` | Action identifier |

**Allows Children:** Yes

### BackButton
Navigation back button.

**Additional Properties:** Common button properties only

**Allows Children:** Yes

### ItemSlotButton
Button displaying an item slot.

**Additional Properties:**

| Property | Type | Description |
|----------|------|-------------|
| `Style` | `ButtonStyle` | Button appearance |
| `ItemId` | `String` | Item identifier |

**Allows Children:** Yes

### TabButton
Button for tab navigation.

**Additional Properties:**

| Property | Type | Description |
|----------|------|-------------|
| `Id` | `String` | Tab identifier |
| `Icon` | `PatchStyle` | Normal icon |
| `IconSelected` | `PatchStyle` | Selected icon |

**Note:** Must be child of `TabNavigation` element

**Allows Children:** Yes

---

## Text Elements

### Label
Static text display.

**Additional Properties:**

| Property | Type | Description |
|----------|------|-------------|
| `Style` | `LabelStyle` | Text styling |
| `Text` | `String` | Text content |
| `MaskTexturePath` | `String` | Texture for masking |
| `TextSpans` | `String` | Localization string key |

**Allows Children:** Yes

### TimerLabel
Label that displays a countdown timer.

**Additional Properties:**

| Property | Type | Description |
|----------|------|-------------|
| `Style` | `LabelStyle` | Text styling |
| `Seconds` | `Int32` | Timer duration |

**Allows Children:** Yes

### HotkeyLabel
Label displaying a key binding.

**Additional Properties:**

| Property | Type | Description |
|----------|------|-------------|
| `InputBindingKey` | `String` | Key binding identifier |
| `InputBindingKeyPrefix` | `String` | Key display prefix |

**Allows Children:** Yes

---

## Input Elements

### TextField
Single-line text input.

**Additional Properties:**

| Property | Type | Description |
|----------|------|-------------|
| `Style` | `InputFieldStyle` | Text styling |
| `PlaceholderStyle` | `InputFieldStyle` | Placeholder styling |
| `PlaceholderText` | `String` | Placeholder text |
| `MaxLength` | `Int32` | Maximum characters |
| `IsReadOnly` | `Boolean` | Read-only mode |
| `PasswordChar` | `String` | Password masking char |
| `Decoration` | `InputFieldDecorationStyle` | Field decoration |

**Allows Children:** Yes

### MultilineTextField
Multi-line text input.

**Additional Properties:**

| Property | Type | Description |
|----------|------|-------------|
| `Style` | `InputFieldStyle` | Text styling |
| `PlaceholderStyle` | `InputFieldStyle` | Placeholder styling |
| `PlaceholderText` | `String` | Placeholder text |
| `MaxVisibleLines` | `Int32` | Visible line count |
| `MaxLength` | `Int32` | Maximum characters |
| `AutoGrow` | `Boolean` | Auto-expand height |
| `ContentPadding` | `Padding` | Content padding |
| `ScrollbarStyle` | `ScrollbarStyle` | Scrollbar appearance |

**Allows Children:** Yes

### CompactTextField
Text field that expands on focus.

**Additional Properties:**

| Property | Type | Description |
|----------|------|-------------|
| `CollapsedWidth` | `Int32` | Width when collapsed |
| `ExpandedWidth` | `Int32` | Width when expanded |
| `PlaceholderText` | `String` | Placeholder text |
| `Style` | `InputFieldStyle` | Text styling |
| `PlaceholderStyle` | `InputFieldStyle` | Placeholder styling |
| `Decoration` | `InputFieldDecorationStyle` | Field decoration |

**Allows Children:** Yes

### NumberField
Numeric input field.

**Additional Properties:**

| Property | Type | Description |
|----------|------|-------------|
| `Format` | `NumberFieldFormat` | Number formatting |
| `Value` | `Float` | Current value |
| `Style` | `InputFieldStyle` | Text styling |
| `PlaceholderStyle` | `InputFieldStyle` | Placeholder styling |

**Allows Children:** Yes

---

## Selection Elements

### CheckBox
Toggle checkbox.

**Additional Properties:**

| Property | Type | Description |
|----------|------|-------------|
| `Style` | `CheckBoxStyle` | Checkbox styling |
| `Value` | `Boolean` | Checked state |

**Allows Children:** Yes

### LabeledCheckBox
Checkbox with integrated label.

**Additional Properties:**

| Property | Type | Description |
|----------|------|-------------|
| `Style` | `LabeledCheckBoxStyle` | Combined styling |

**Allows Children:** Yes

### DropdownBox
Dropdown selection menu.

**Additional Properties:**

| Property | Type | Description |
|----------|------|-------------|
| `Style` | `DropdownBoxStyle` | Dropdown styling |
| `NoItemsText` | `String` | Empty state text |
| `PanelTitleText` | `String` | Panel title |
| `MaxSelection` | `Int32` | Max selected items |
| `ShowLabel` | `Boolean` | Show label |

**Allows Children:** Yes

### TabNavigation
Tab navigation container.

**Additional Properties:**

| Property | Type | Description |
|----------|------|-------------|
| `Style` | `TabNavigationStyle` | Tab styling |
| `SelectedTab` | `String` | Selected tab ID |
| `AllowUnselection` | `Boolean` | Allow no selection |

**Allows Children:** Yes (should contain `TabButton` elements)

---

## Slider Elements

### Slider
Integer slider.

**Additional Properties:**

| Property | Type | Description |
|----------|------|-------------|
| `Style` | `SliderStyle` | Slider styling |
| `Min` | `Float` | Minimum value |
| `Max` | `Float` | Maximum value |
| `Step` | `Float` | Step increment |
| `Value` | `Float` | Current value |

**Allows Children:** Yes

### FloatSlider
Floating-point slider.

**Additional Properties:**

| Property | Type | Description |
|----------|------|-------------|
| `Style` | `SliderStyle` | Slider styling |
| `Min` | `Float` | Minimum value |
| `Max` | `Float` | Maximum value |
| `Step` | `Float` | Step increment |
| `Value` | `Float` | Current value |

**Allows Children:** Yes

### SliderNumberField
Slider with numeric input field.

**Additional Properties:**

| Property | Type | Description |
|----------|------|-------------|
| `SliderStyle` | `SliderStyle` | Slider styling |
| `Style` | `InputFieldStyle` | Field styling |
| `NumberFieldContainerAnchor` | `Anchor` | Field positioning |
| `NumberFieldStyle` | `InputFieldStyle` | Field styling |
| `Min` | `Float` | Minimum value |
| `Max` | `Float` | Maximum value |
| `Step` | `Float` | Step increment |
| `Value` | `Float` | Current value |

**Note:** Min must be less than Max, or client crashes

**Allows Children:** Yes

### FloatSliderNumberField
Float slider with numeric input.

**Additional Properties:**

| Property | Type | Description |
|----------|------|-------------|
| `SliderStyle` | `SliderStyle` | Slider styling |
| `NumberFieldContainerAnchor` | `Anchor` | Field positioning |
| `NumberFieldStyle` | `InputFieldStyle` | Field styling |
| `NumberFieldMaxDecimalPlaces` | `Int32` | Decimal precision |
| `Min` | `Float` | Minimum value |
| `Max` | `Float` | Maximum value |
| `Step` | `Float` | Step increment |
| `Value` | `Float` | Current value |

**Note:** Min must be less than Max, or client crashes

**Allows Children:** Yes

---

## Progress Elements

### ProgressBar
Linear progress indicator.

**Additional Properties:**

| Property | Type | Description |
|----------|------|-------------|
| `Value` | `Float` | Progress value (0-1) |
| `Bar` | `PatchStyle` | Bar appearance |
| `BarTexturePath` | `String` | Bar texture (not PatchStyle) |
| `EffectTexturePath` | `String` | Effect texture (not PatchStyle) |
| `MaskTexturePath` | `String` | Mask texture |
| `EffectWidth` | `Float` | Effect width |
| `EffectHeight` | `Float` | Effect height |
| `EffectOffset` | `Float` | Effect offset |
| `Direction` | `ProgressBarDirection` | Fill direction |
| `Alignment` | `ProgressBarAlignment` | Bar alignment |

**Allows Children:** Yes

### CircularProgressBar
Circular progress indicator.

**Additional Properties:**

| Property | Type | Description |
|----------|------|-------------|
| `Value` | `Float` | Progress value (0-1) |
| `MaskTexturePath` | `String` | Mask texture |
| `Color` | `Color` | Progress color |

**Allows Children:** Yes

---

## Visual Elements

### Sprite
Animated sprite display.

**Additional Properties:**

| Property | Type | Description |
|----------|------|-------------|
| `TexturePath` | `String` | Sprite sheet path |
| `Frame` | `SpriteFrame` | Frame configuration |
| `FramesPerSecond` | `Int32` | Animation speed |

**Allows Children:** Yes

### AssetImage
Static image display.

**Additional Properties:**

| Property | Type | Description |
|----------|------|-------------|
| `LayoutMode` | `LayoutMode` | Layout mode |
| `AssetPath` | `String` | Image asset path |

**Allows Children:** Yes

### BackgroundImage
Full-screen background image.

**Additional Properties:**

| Property | Type | Description |
|----------|------|-------------|
| `Image` | `String` | Standard image path |
| `ImageUW` | `String` | Ultrawide image path |

**Note:** Not working on mods

**Allows Children:** Yes

### SceneBlur
Background blur effect.

**Additional Properties:** None

**Allows Children:** No

---

## Item Elements

### ItemSlot
Single item display slot.

**Additional Properties:**

| Property | Type | Description |
|----------|------|-------------|
| `ShowQualityBackground` | `Boolean` | Show quality bg |
| `ShowQuantity` | `Boolean` | Show item count |
| `ItemId` | `String` | Item identifier |

**Allows Children:** Yes

### ItemIcon
Item icon display.

**Additional Properties:**

| Property | Type | Description |
|----------|------|-------------|
| `ItemId` | `String` | Item identifier |

**Allows Children:** Yes

### ItemGrid
Grid of item slots.

**Additional Properties:**

| Property | Type | Description |
|----------|------|-------------|
| `Style` | `ItemGridStyle` | Grid styling |
| `SlotsPerRow` | `Int32` | Slots per row |
| `RenderItemQualityBackground` | `Boolean` | Show quality backgrounds |
| `AreItemsDraggable` | `Boolean` | Allow dragging |
| `KeepScrollPosition` | `Boolean` | Maintain scroll |
| `ShowScrollbar` | `Boolean` | Show scrollbar |
| `ScrollbarStyle` | `ScrollbarStyle` | Scrollbar styling |
| `InfoDisplay` | `ItemGridInfoDisplayMode` | Info display mode |

**Allows Children:** Yes

---

## Color Elements

### ColorPicker
Color selection widget.

**Additional Properties:**

| Property | Type | Description |
|----------|------|-------------|
| `Style` | `ColorPickerStyle` | Picker styling |
| `Format` | `ColorFormat` | Color format |

**Allows Children:** Yes

### ColorPickerDropdownBox
Dropdown color picker.

**Additional Properties:**

| Property | Type | Description |
|----------|------|-------------|
| `Style` | `ColorPickerDropdownBoxStyle` | Dropdown styling |
| `Format` | `ColorFormat` | Color format |
| `DisplayTextField` | `Boolean` | Show text field |

**Allows Children:** Yes

---

## Special Elements

### BlockSelector
Block selection interface.

**Additional Properties:**

| Property | Type | Description |
|----------|------|-------------|
| `Capacity` | `Float` | Max blocks (typically Int32) |
| `Style` | `BlockSelectorStyle` | Selector styling |

**Allows Children:** Yes

### ReorderableListGrip
Drag handle for reorderable lists.

**Additional Properties:** None

**Allows Children:** Yes

### MenuItem
Menu item element.

**Additional Properties:**

| Property | Type | Description |
|----------|------|-------------|
| `Text` | `String` | Item text |
| `PopupStyle` | `PopupStyle` | Popup styling |
| `Style` | `MenuItemStyle` | Normal styling |
| `SelectedStyle` | `MenuItemStyle` | Selected styling |
| `Icon` | `PatchStyle` | Item icon |
| `IconAnchor` | `Anchor` | Icon positioning |

**Allows Children:** Yes

---

## Preview Elements (Limited Mod Support)

### ItemPreviewComponent
3D item preview.

**Additional Properties:**

| Property | Type | Description |
|----------|------|-------------|
| `ItemScale` | `Float` | Preview scale |

**Note:** Not working on mods - crashes client

**Allows Children:** Yes

### CharacterPreviewComponent
3D character preview.

**Additional Properties:** None

**Note:** Not working on mods - crashes client

**Allows Children:** Yes

### PlayerPreviewComponent
Player preview component.

**Additional Properties:**

| Property | Type | Description |
|----------|------|-------------|
| `Scale` | `Float` | Preview scale |

**Note:** Not working on mods - unknown node type

**Allows Children:** Yes
