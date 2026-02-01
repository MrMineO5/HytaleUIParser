package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.token.Token
import app.ultradev.hytaleuiparser.validation.Scope

data class NodeIdentifier(
    val token: NodeToken,
    val identifier: String
) : AstNode() {
    override val children: List<AstNode>
        get() = listOf(token)

    constructor(identifier: String) : this(
        NodeToken(Token(Token.Type.IDENTIFIER, identifier)),
        identifier
    )

    override fun setScope(scope: Scope) {
        super.setScope(scope)
        token.setScope(scope)
    }
}