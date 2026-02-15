package app.ultradev.hytaleuiparser.warning

import app.ultradev.hytaleuiparser.ast.AstNode

sealed interface ValidatorWarning {
    val node: AstNode
    val message: String
    val description: String
}