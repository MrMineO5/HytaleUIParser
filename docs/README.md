# Hytale UI Parser Documentation

## Overview

The Hytale UI Parser is a Kotlin library for parsing and validating Hytale UI files. It consists of three main components:

1. **Tokenizer** - Lexical analysis that converts raw text into tokens
2. **Parser** - Syntax analysis that constructs an Abstract Syntax Tree (AST)
3. **Validator** - Semantic analysis that validates element types, properties, and type correctness

## Documentation Files

- **[Parser Specification](PARSER.md)** - Tokenizer, parser grammar, and AST node types
- **[Element Reference](ELEMENTS.md)** - Complete element type reference with properties
- **[Type Reference](TYPES.md)** - Type system reference including primitives, enums, and structs
- **[Validator Specification](VALIDATOR.md)** - Validation rules and semantic analysis

## Quick Start

### Basic Parsing

```kotlin
import app.ultradev.hytaleuiparser.*
import java.io.File

val uiFile = File("example.ui")
val root = Parser(Tokenizer(uiFile.reader())).finish()
println(root)
```

### With Validation

```kotlin
val files = mapOf(
    "Main.ui" to parseFile("Main.ui"),
    "Common.ui" to parseFile("Common.ui")
)

val validator = Validator(files)
validator.validate()

validator.validationErrors.forEach { error ->
    println(error)
}
```

### Example UI File

```ui
// Import common definitions
$C = "../Common.ui";

// Define variables
@ButtonWidth = 100;
@ButtonHeight = 24;
@PrimaryColor = #FF0000;

// Define reusable button
@StyledButton = Button {
    Style: $C.@ButtonStyle;
    Anchor: (Width: @ButtonWidth, Height: @ButtonHeight);

    #Hovered {
        Background: @PrimaryColor;
    }
}

// Main UI structure
Group {
    LayoutMode: TopScrolling;
    Padding: (Full: 10);

    @StyledButton {
        Text: "Click Me";
    }

    Label {
        Text: "Hello World";
        Style: (
            FontSize: 14,
            TextColor: #000000
        );
    }
}
```

## File Format

**Extension:** `.ui`

**Encoding:** UTF-8 (BOM supported)

**Line Endings:** Any (LF, CRLF, CR)

**Comments:** Supported (`//` and `/* */`)

## Best Practices

### File Organization

1. **Use references for shared definitions:**
   ```
   $Common = "../Common.ui";
   $Styles = "../Styles.ui";
   ```

2. **Define variables at the top:**
   ```
   @Width = 100;
   @Height = 50;
   @Color = #FF0000;
   ```

3. **Create reusable components:**
   ```
   @StyledButton = Button {
       Style: $Common.@ButtonStyle;
   }
   ```

### Naming Conventions

1. **Variables:** Use PascalCase
   ```
   @ButtonWidth
   @PrimaryColor
   ```

2. **References:** Use single uppercase letters or short names
   ```
   $C  // Common
   $S  // Styles
   ```

3. **Element types:** Use PascalCase
   ```
   Label
   TextButton
   ```

### Common Pitfalls

1. **Unquoted strings:**
   ```
   // Wrong
   Text: Hello;

   // Correct
   Text: "Hello";
   ```

2. **Quoted numbers:**
   ```
   // Wrong
   Width: "100";

   // Correct
   Width: 100;
   ```

3. **Missing semicolons:**
   ```
   // Wrong
   @Width = 100

   // Correct
   @Width = 100;
   ```

4. **Invalid selector usage:**
   ```
   // Wrong - Selector elements only in variable elements
   Label {
       #Hovered {
           Background: #FF0000;
       }
   }

   // Correct
   @MyLabel = Label {
       #Hovered {
           Background: #FF0000;
       }
   }
   ```

## Implementation

**Parser:**
- Location: `src/main/kotlin/app/ultradev/hytaleuiparser/Parser.kt`
- Recursive descent parser
- Single-pass construction
- Error recovery: Throws `ParserException` on error

**Tokenizer:**
- Location: `src/main/kotlin/app/ultradev/hytaleuiparser/Tokenizer.kt`
- Streaming tokenizer
- Supports line/column tracking
- Lazy evaluation

**Validator:**
- Location: `src/main/kotlin/app/ultradev/hytaleuiparser/Validator.kt`
- Multi-pass validation
- Scope resolution
- Type inference
- Error accumulation

## License

See LICENSE file in the root directory.
