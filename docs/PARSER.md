# Parser Specification

This document covers the tokenizer and parser components of the Hytale UI Parser.

## Table of Contents

1. [Tokenizer](#1-tokenizer)
2. [Parser](#2-parser)
3. [Grammar Summary](#3-grammar-summary)

---

## 1. Tokenizer

The tokenizer performs lexical analysis, converting raw text into a stream of tokens.

### 1.1 Token Types

| Token Type           | Description                                | Example                        |
|----------------------|--------------------------------------------|--------------------------------|
| `IDENTIFIER`         | Alphanumeric identifiers                   | `Label`, `Button`, `MyElement` |
| `VARIABLE`           | Variable references (prefixed with `@`)    | `@ButtonWidth`, `@Color`       |
| `REFERENCE`          | File references (prefixed with `$`)        | `$C`, `$Common`                |
| `ASSIGNMENT`         | Assignment operator                        | `=`                            |
| `MEMBER_MARKER`      | Member access operator                     | `.`                            |
| `END_STATEMENT`      | Statement terminator                       | `;`                            |
| `SPREAD`             | Spread operator for types                  | `...`                          |
| `START_ELEMENT`      | Element body start                         | `{`                            |
| `END_ELEMENT`        | Element body end                           | `}`                            |
| `STRING`             | Quoted string literals                     | `"Hello"`, `"../Common.ui"`    |
| `NUMBER`             | Numeric literals (int or float)            | `24`, `3.14`, `0.5`            |
| `START_PARENTHESIS`  | Type/expression start                      | `(`                            |
| `END_PARENTHESIS`    | Type/expression end                        | `)`                            |
| `SELECTOR`           | CSS-like selectors and colors              | `#Hovered`, `#FF0000`          |
| `FIELD_MARKER`       | Field assignment operator                  | `:`                            |
| `FIELD_DELIMITER`    | Field separator                            | `,`                            |
| `COMMENT`            | Single-line (`//`) or multi-line (`/* */`) | `// comment`, `/* block */`    |
| `START_ARRAY`        | Array start                                | `[`                            |
| `END_ARRAY`          | Array end                                  | `]`                            |
| `MATH_ADD`           | Addition operator                          | `+`                            |
| `MATH_SUBTRACT`      | Subtraction operator                       | `-`                            |
| `MATH_MULTIPLY`      | Multiplication operator                    | `*`                            |
| `MATH_DIVIDE`        | Division operator                          | `/`                            |
| `TRANSLATION_MARKER` | Translation string marker                  | `%`                            |
| `WHITESPACE`         | Whitespace characters                      | Space, tab, newline            |
| `UNKNOWN`            | Unrecognized tokens                        | -                              |

### 1.2 Tokenization Rules

#### Identifiers
- **Pattern**: Starts with a letter, followed by zero or more letters or digits
- **Examples**: `Label`, `Button1`, `MyCustomElement`

#### Numbers
- **Pattern**: One or more digits, optionally containing a single decimal point
- **Examples**: `42`, `3.14`, `0.5`
- **Note**: Cannot start with a letter

#### Strings
- **Pattern**: Enclosed in double quotes `"`
- **Examples**: `"Hello World"`, `"../Common.ui"`
- **Unclosed strings**: Result in `UNKNOWN` token

#### Variables
- **Pattern**: `@` followed by alphanumeric characters
- **Examples**: `@Width`, `@MyVariable`, `@Color1`

#### References
- **Pattern**: `$` followed by alphanumeric characters
- **Examples**: `$C`, `$Common`, `$Styles`

#### Selectors
- **Pattern**: `#` followed by alphanumeric characters
- **Examples**: `#Hovered`, `#Selected`, `#FF0000`
- **Usage**: CSS-like selectors for element states, or color values

#### Translation Strings
- **Pattern**: `%` followed by identifiers separated by `.`
- **Examples**: `%UI.Button.Click`, `%Game.Title`

#### Comments
- **Single-line**: `//` until end of line
- **Multi-line**: `/* ... */`
- **Note**: Comments are skipped during parsing by default

#### Spread Operator
- **Pattern**: Three consecutive dots `...`
- **Usage**: Type spreading in type definitions

#### Whitespace
- **Characters**: Space, tab, newline, BOM (`\uFEFF`)
- **Note**: Whitespace tokens are generated but typically ignored during parsing

---

## 2. Parser

The parser constructs an Abstract Syntax Tree (AST) from the token stream.

### 2.1 Grammar Overview

```
RootNode ::= (Reference | Variable | Element)*

Reference ::= REFERENCE ASSIGNMENT STRING END_STATEMENT

Variable ::= VARIABLE ASSIGNMENT Value END_STATEMENT

Element ::= Identifier Selector? ElementBody
         | VARIABLE Selector? ElementBody
         | RefMember Selector? ElementBody

ElementBody ::= START_ELEMENT (Field | Element | SelectorElement)* END_ELEMENT

Field ::= IDENTIFIER FIELD_MARKER Value (FIELD_DELIMITER | END_STATEMENT)?

Value ::= Constant | Variable | RefMember | Type | Element | Color | Array
       | Translation | MathOperation | Negation
```

### 2.2 AST Node Types

#### 2.2.1 RootNode
Represents a complete UI file.

**Properties:**
- `references: List<NodeAssignReference>` - File references
- `variables: List<NodeAssignVariable>` - Variable declarations
- `elements: List<NodeElement>` - Top-level elements
- `path: String` - File path within assets (resolved during validation)
- `variableValues: Map<String, AstNode>` - Variable values map (resolved during validation)

**Example:**
```
$C = "../Common.ui";
@ButtonWidth = 24;
Label {}
```

#### 2.2.2 NodeAssignReference
Assigns a reference variable to a file path.

**Syntax:** `$<identifier> = "<path>";`

**Example:**
```
$C = "../Common.ui";
```

#### 2.2.3 NodeAssignVariable
Assigns a variable to a value.

**Syntax:** `@<identifier> = <value>;`

**Examples:**
```
@Width = 100;
@Color = #FF0000;
@Style = (FontSize: 14);
@MyButton = Button { Text: "Click" };
```

#### 2.2.4 NodeElement
Represents a UI element.

**Syntax:** `<type> <selector>? { <body> }`

**Properties:**
- `type` - Element type (identifier, variable, or reference member)
- `selector` - Optional selector for element state
- `body` - Element body containing fields and child elements

**Examples:**
```
Label {
    Text: "Hello";
}

Button #Hovered {
    Background: #00FF00;
}

@MyButton {
    Text: "Custom";
}
```

#### 2.2.5 NodeField
Represents a property assignment within an element or type.

**Syntax:** `<identifier>: <value>,` or `<identifier>: <value>;`

**Examples:**
```
Text: "Hello";
Width: 100,
Background: #FF0000(0.5)
```

#### 2.2.6 NodeType
Represents a type/struct value.

**Syntax:** `<TypeName>? ( <fields> )`

**Examples:**
```
(Left: 10, Right: 10)
Anchor(Width: 100, Height: 50)
(...@Base, Color: #FF0000)
```

**Fields can be:**
- Named fields: `Name: value`
- Spread operators: `...@variable` or `...$Ref.@variable`

#### 2.2.7 NodeConstant
Represents a constant value (string, number, or unquoted identifier).

**Examples:**
```
"Hello"           // Quoted string
42                // Number
3.14              // Float
TopCenter         // Unquoted identifier
```

#### 2.2.8 NodeColor
Represents a color value with optional opacity.

**Syntax:** `#<hex> ( <opacity> )?`

**Examples:**
```
#FF0000           // Red
#00FF00(0.5)      // Green with 50% opacity
```

#### 2.2.9 NodeVariable
References a variable.

**Syntax:** `@<identifier>`

**Example:** `@ButtonWidth`

#### 2.2.10 NodeRefMember
References a variable from another file.

**Syntax:** `$<reference>.@<variable>`

**Example:** `$C.@ButtonStyle`

#### 2.2.11 NodeMemberField
Accesses a field of a type.

**Syntax:** `<value>.<field>.<field>...`

**Example:** `@Style.Background.Color`

#### 2.2.12 NodeArray
Represents an array of values.

**Syntax:** `[ <value>, <value>, ... ]`

**Example:** `[1, 2, 3, 4]`

#### 2.2.13 NodeMathOperation
Represents a mathematical operation.

**Operators:** `+`, `-`, `*`, `/`

**Examples:**
```
@Width + 10
@Height / 2
@Base * 1.5
```

**Operator Precedence:**
The parser uses right-to-left evaluation. Use parentheses for explicit precedence:
```
(@Width + 10) * 2
```

#### 2.2.14 NodeNegate
Represents a negated value.

**Syntax:** `-<value>`

**Example:** `-@Offset`

#### 2.2.15 NodeTranslation
Represents a translatable string reference.

**Syntax:** `%<path.to.key>`

**Example:** `%UI.Button.Click`

#### 2.2.16 NodeSelector
Represents a selector for element state.

**Syntax:** `#<identifier>`

**Examples:** `#Hovered`, `#Selected`, `#Default`

#### 2.2.17 NodeSelectorElement
Represents an element definition within a variable element for a specific selector state.

**Syntax:** `#<selector> { <body> }`

**Example:**
```
@MyButton = Button {
    Text: "Click";
    #Hovered {
        Background: #00FF00;
    }
}
```

#### 2.2.18 NodeSpread
Spreads fields from another type/variable.

**Syntax:** `...<variable>`

**Example:**
```
(...@BaseStyle, Color: #FF0000)
```

### 2.3 Parsing Rules

#### 2.3.1 Root Level
At the root level, only these constructs are allowed:
- Reference assignments: `$<id> = "<path>";`
- Variable assignments: `@<id> = <value>;`
- Element declarations: `<Type> { ... }`

**Invalid:** Fields, selectors, or bare values at root level

#### 2.3.2 Element Bodies
Element bodies can contain:
- Fields: `Name: value;` or `Name: value,`
- Child elements: `Label { ... }`
- Variable assignments: `@var = value;`
- Selector elements: `#Hovered { ... }` (only valid in variable elements)

#### 2.3.3 Type Definitions
Type definitions can contain:
- Fields: `Name: value,` or `Name: value`
- Spread operators: `...@variable,`
- Reference members: `$Ref.@member`

**Note:** Types must end with `)`, not require statement terminators

#### 2.3.4 Context-Dependent Parsing
The parser uses lookahead to disambiguate:

1. **Identifier after identifier:**
   - If followed by `(`: Type definition
   - If followed by `{` or `#`: Element
   - Otherwise: Unquoted constant

2. **Parenthesis after value:**
   - If contains fields or spreads: Type
   - Otherwise: Grouped math expression

3. **Variable at root:**
   - If followed by `=`: Variable assignment
   - If followed by `{` or `#`: Variable element

---

## 3. Grammar Summary

### 3.1 Token Symbols

```
=    ASSIGNMENT
:    FIELD_MARKER
;    END_STATEMENT
,    FIELD_DELIMITER
.    MEMBER_MARKER
...  SPREAD
{    START_ELEMENT
}    END_ELEMENT
(    START_PARENTHESIS
)    END_PARENTHESIS
[    START_ARRAY
]    END_ARRAY
+    MATH_ADD
-    MATH_SUBTRACT
*    MATH_MULTIPLY
/    MATH_DIVIDE
@    VARIABLE prefix
$    REFERENCE prefix
#    SELECTOR/COLOR prefix
%    TRANSLATION_MARKER prefix
"    STRING delimiter
```

### 3.2 Complete EBNF Grammar

```ebnf
Root ::= (Reference | VariableAssignment | Element)*

Reference ::= REFERENCE ASSIGNMENT STRING END_STATEMENT

VariableAssignment ::= VARIABLE ASSIGNMENT Value END_STATEMENT

Element ::= ElementType Selector? ElementBody

ElementType ::= IDENTIFIER | VARIABLE | RefMember

Selector ::= SELECTOR

ElementBody ::= START_ELEMENT ElementBodyItem* END_ELEMENT

ElementBodyItem ::= Field
                  | Element
                  | VariableAssignment
                  | SelectorElement

SelectorElement ::= SELECTOR ElementBody

Field ::= IDENTIFIER FIELD_MARKER Value (FIELD_DELIMITER | END_STATEMENT)?

Value ::= Constant
        | VARIABLE
        | RefMember
        | Type
        | Element
        | Color
        | Array
        | Translation
        | MathOperation
        | Negation
        | MemberAccess
        | GroupedExpression

Type ::= IDENTIFIER? START_PARENTHESIS TypeItem* END_PARENTHESIS

TypeItem ::= Field | Spread | RefMember

Spread ::= SPREAD (VARIABLE | RefMember) FIELD_DELIMITER?

RefMember ::= REFERENCE MEMBER_MARKER VARIABLE

Color ::= SELECTOR Opacity?

Opacity ::= START_PARENTHESIS NUMBER END_PARENTHESIS

Array ::= START_ARRAY (Value (FIELD_DELIMITER Value)*)? END_ARRAY

Translation ::= TRANSLATION_MARKER (IDENTIFIER | MEMBER_MARKER)+

Constant ::= STRING | NUMBER | IDENTIFIER

MathOperation ::= Value MathOperator Value

MathOperator ::= MATH_ADD | MATH_SUBTRACT | MATH_MULTIPLY | MATH_DIVIDE

Negation ::= MATH_SUBTRACT Value

MemberAccess ::= Value (MEMBER_MARKER IDENTIFIER)+

GroupedExpression ::= START_PARENTHESIS Value END_PARENTHESIS
```
