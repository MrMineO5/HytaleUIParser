package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.validation.Scope

data class NodeField(
    val identifier: NodeIdentifier,
    val fieldMarker: NodeToken,
    val value: AstNode,
    val endStatement: NodeToken? = null
) : AstNode() {
    init {
        if (value !is VariableValue) {
            error("Expected variable value after assignment operator: $value")
        }
    }

    override val children: List<AstNode>
        get() = listOf(identifier, fieldMarker, value) + listOfNotNull(endStatement)

    override fun setScope(scope: Scope) {
        super.setScope(scope)
        value.setScope(scope)
    }
}
