package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.token.Token

abstract class AstNode {
    abstract val children: List<AstNode>
    open val tokens: List<Token>
        get() = children.flatMap { it.tokens }
}
