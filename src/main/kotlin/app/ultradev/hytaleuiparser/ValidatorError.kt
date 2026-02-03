package app.ultradev.hytaleuiparser

import app.ultradev.hytaleuiparser.ast.AstNode

data class ValidatorError(
    val message: String,
    val node: AstNode,
    val cause: ValidatorError? = null
) {
    override fun toString(): String {
        return "ValidatorError $message at ${node.file.path}:${node.tokens.first().let { "${it.startLine+1}:${it.startColumn+1}" }}, node=${node.text}, parsed=$node)"
    }
}
