# Validator Specification

This document describes the validation process and rules used by the Hytale UI Parser validator.

## Table of Contents

- [Validation Process](#validation-process)
- [Validation Rules](#validation-rules)
  - [Variable Scope](#341-variable-scope)
  - [Reference Resolution](#342-reference-resolution)
  - [Type Inference](#343-type-inference)
  - [Variable Elements](#344-variable-elements)
  - [Type Spreading](#345-type-spreading)
  - [Property Validation](#346-property-validation)
  - [Error Reporting](#347-error-reporting)

---

## Validation Process

The validator performs semantic analysis in multiple passes:

1. **Static validation**: Check AST structure and constraints
2. **Scope initialization**: Create variable scopes
3. **Reference validation**: Verify file references exist
4. **Element validation**: Check element types and properties
5. **Type validation**: Verify type correctness

---

## Validation Rules

### 3.4.1 Variable Scope

Variables are scoped hierarchically:
- Root scope: File-level variables
- Element scope: Variables within element bodies
- Child scopes: Inherit from parent scopes

**Variable Resolution:**
1. Check current scope
2. Check parent scopes recursively
3. If not found, report error

**Example:**
```
@Width = 100;

Label {
    @Height = 50;

    // Can access @Width and @Height
    Anchor: (Width: @Width, Height: @Height);

    Button {
        // Can access @Width and @Height from parent scopes
        Anchor: (Width: @Width);
    }
}
```

### 3.4.2 Reference Resolution

References to other files are resolved:
1. Parse reference assignment: `$C = "../Common.ui";`
2. Resolve relative path
3. Load and validate referenced file
4. Make variables accessible via `$C.@variable`

**Example:**
```
// File: MyUI.ui
$C = "../Common.ui";

Label {
    // Access variable from Common.ui
    Style: $C.@DefaultLabelStyle;
}
```

### 3.4.3 Type Inference

For untyped values, the validator attempts to infer types:

1. **Explicit types:** `Anchor(Width: 100)`
2. **Field-based inference:** `(Width: 100, Height: 50)` matches types with those fields
3. **Single match:** If only one type matches, use it
4. **Multiple matches:** Narrow down by validating field types
5. **No matches:** Report error

**Example:**
```
// Explicit type
Anchor: Anchor(Width: 100);

// Inferred as Anchor (only type with Width and Height)
Anchor: (Width: 100, Height: 50);

// Error: Multiple types match
SomeField: (Full: 10);  // Could be Padding or Anchor
```

### 3.4.4 Variable Elements

Variable elements allow defining reusable element templates:

```
@MyButton = Button {
    Text: "Default";
    Background: #FF0000;

    #Hovered {
        Background: #00FF00;
    }
}

// Use variable element
@MyButton {
    Text: "Click Me";  // Override Text
}
```

**Validation:**
1. Resolve variable to element definition
2. Clone element structure
3. Merge properties from usage with definition
4. Validate selector elements match definition
5. Validate all properties

### 3.4.5 Type Spreading

Types can spread fields from other types:

```
@BaseStyle = (FontSize: 14, Color: #000000);
@CustomStyle = (...@BaseStyle, Color: #FF0000);
// Results in: (FontSize: 14, Color: #FF0000)
```

**Validation:**
1. Resolve spread source
2. Must be a type
3. Merge fields (later fields override earlier)
4. Validate merged type

### 3.4.6 Property Validation

Each property is validated against its expected type:

1. **Primitive types:** Check value can be parsed
2. **Enum types:** Check value is in enum set
3. **Struct types:** Recursively validate fields
4. **Special cases:** `PatchStyle` can be color or string

**Math Operations:**
- Validate both operands match expected type
- No operator type checking (assumes valid)

**Member Access:**
- Can access fields of type values
- `@Style.Background.Color`

### 3.4.7 Error Reporting

Validation errors include:
- Error message
- AST node location
- File path and line/column (if available)
- Parent error context (for nested errors)

**Common Errors:**
- Unknown element type
- Unknown property
- Type mismatch
- Variable not found
- Reference not found
- Duplicate property
- Invalid enum value
- Unquoted string
- Invalid number
