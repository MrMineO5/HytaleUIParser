package app.ultradev.hytaleuiparser.ast

data class NodeConstant(
    override val children: List<NodeToken>,
    val valueText: String
) : AstNode(), VariableValue {
    constructor(token: NodeToken, valueText: String) : this(
        listOf(token),
        valueText
    )
}
