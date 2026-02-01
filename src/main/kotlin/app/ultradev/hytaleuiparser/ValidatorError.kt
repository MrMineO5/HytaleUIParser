package app.ultradev.hytaleuiparser

import app.ultradev.hytaleuiparser.ast.AstNode

class ValidatorError(
    message: String,
    val node: AstNode,
    cause: Throwable? = null,
) {
    override fun toString(): String {
        return "ValidatorException(position=${node.file.path}:${node.tokens.first().let { "${it.startLine+1}:${it.startColumn+1}" }}, node=${node.text}, parsed=$node)"
    }
}
