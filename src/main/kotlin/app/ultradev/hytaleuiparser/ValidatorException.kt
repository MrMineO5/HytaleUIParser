package app.ultradev.hytaleuiparser

import app.ultradev.hytaleuiparser.ast.AstNode

class ValidatorException(
    message: String,
    val node: AstNode,
    cause: Throwable? = null,
) : Exception(message, cause) {
    override fun toString(): String {
        return "ValidatorException(message='$message', position=${node.file.path}:${node.tokens.first().let { "${it.startLine+1}:${it.startColumn+1}" }}, node=${node.text}, parsed=$node)"
    }
}
