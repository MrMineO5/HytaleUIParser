package app.ultradev.hytaleuiparser.ast

sealed interface VariableValue {
    val asAstNode: AstNode get() = this as AstNode
}
