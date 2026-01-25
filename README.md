# Hytale UI Parser
A Kotlin parsing library for Hytale UI files.
The parser consists of 3 parts:
1. A tokenizer that reads a file and splits it into tokens.
2. A parser that converts tokens into a tree structure.
3. A validator that checks that all properties are valid and of the correct type.
The validator also populates some tree nodes with additional information such as their resolved scope.


## Usage
This parser is mainly intended as a library for other projects.

```kotlin
val uiFile = File("example.ui")
val root = Parser(Tokenizer(uiFile.reader())).finish()

println(root)
```


## Nodes
The AST nodes form a tree structure where all leaf nodes are `NodeToken`. These refer to a particular text token in the source file.

For parsed nodes, the tokens will always point to a specific line and column in the source file.

When creating nodes in code, the tokens will be created automatically, but will not point to a line/column.

### RootNode
The root node of a tree, corresponds to a single UI file.

#### Parsed:
- `references: List<NodeAssignReference>` a list of references to other UI files e.g. `$C = "../Common.ui";`
- `variables: List<NodeAssignVariable>` a list of declared variables e.g. `@ButtonWidth = 24;`
- `elements: List<NodeElement>` a list of declared elements e.g. `Label {}`

#### Resolved:
- `path: String` the path to the UI file within the assets e.g. `Pages/PrefabEditorSettings.ui`
- `variableValues: Map<String, AstNode>` a map of declared variable names to their values


### NodeAssignReference
Represents a reference to another UI file.

Example:
```ui
$C = "../Common.ui";
```

Parsed:
```kotlin
NodeAssignReference(
    variable = NodeReference(identifier = NodeIdentifier("C")),
    value = NodeConstant.quoted("../Common.ui")
)
```


# TODO

