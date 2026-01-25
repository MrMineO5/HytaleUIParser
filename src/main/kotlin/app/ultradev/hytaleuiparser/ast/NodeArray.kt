package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.token.GeneratedTokens

data class NodeArray(
    val startToken: NodeToken,
    val elements: List<AstNode>,
    val endToken: NodeToken
) : AstNode(), VariableValue {
    override val children: List<AstNode>
        get() = listOf(startToken) + elements + listOf(endToken)

    constructor(elements: List<AstNode>) : this(GeneratedTokens.arrayStart(), elements, GeneratedTokens.arrayEnd())

    // Skip delimiters
    val entries: List<AstNode> get() = elements.filter { it !is NodeToken }
}
