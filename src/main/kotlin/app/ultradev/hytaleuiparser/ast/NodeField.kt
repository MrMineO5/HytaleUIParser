package app.ultradev.hytaleuiparser.ast

data class NodeField(
    val identifier: NodeIdentifier,
    val fieldMarker: NodeToken,
    val value: AstNode
) : AstNode() {
    init {
        if (value !is VariableValue) {
            error("Expected variable value after assignment operator: $value")
        }
    }

    override val children: List<AstNode>
        get() = listOf(identifier, fieldMarker, value)
}
