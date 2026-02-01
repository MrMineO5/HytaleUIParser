package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.token.Token
import app.ultradev.hytaleuiparser.validation.Scope

data class NodeIdentifier(
    val token: NodeToken
) : AstNode() {
    override val children: List<AstNode>
        get() = listOf(token)

    val identifier get() = token.text

    constructor(identifier: String) : this(
        NodeToken(Token(Token.Type.IDENTIFIER, identifier)),
    )

    override fun setScope(scope: Scope) {
        super.setScope(scope)
        token.setScope(scope)
    }

    override fun clone() = NodeIdentifier(token.clone())
}