package app.ultradev.hytaleuiparser.ast

data class NodeSpread(
    val spreadMarker: NodeToken,
    val variable: AstNode,
    val endStatement: NodeToken? = null
) : AstNode() {
    init {
        if (variable !is VariableReference) {
            error("Expected variable reference after spread operator: $variable")
        }
    }

    override val children: List<AstNode>
        get() = listOf(spreadMarker, variable) + listOfNotNull(endStatement)
}
