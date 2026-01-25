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
    constructor(token: Token) : this(NodeToken(token), token.text)

    companion object {
        fun quoted(valueText: String) = NodeConstant(NodeToken(Token(Token.Type.STRING, "\"$valueText\"")), valueText)

    }
}
