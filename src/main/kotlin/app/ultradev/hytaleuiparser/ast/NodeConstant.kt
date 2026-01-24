package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.token.Token

data class NodeConstant(
    override val children: List<NodeToken>,
    val valueText: String
) : AstNode(), VariableValue {
    constructor(token: NodeToken, valueText: String) : this(
        listOf(token),
        valueText
    )

    constructor(valueText: String) : this(
        listOf(
            NodeToken(Token(Token.Type.IDENTIFIER, "\"$valueText\""))
        ),
        valueText
    )
}
