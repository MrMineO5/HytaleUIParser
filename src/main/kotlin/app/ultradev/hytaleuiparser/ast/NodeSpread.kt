package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.validation.Scope

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

    val variableAsReference: VariableReference = variable as VariableReference

    override fun setScope(scope: Scope) {
        super.setScope(scope)
        variable.setScope(scope)

        spreadMarker.setScope(scope)
        endStatement?.setScope(scope)
    }

    override fun clone() = NodeSpread(spreadMarker.clone(), variable.clone(), endStatement?.clone())
}
