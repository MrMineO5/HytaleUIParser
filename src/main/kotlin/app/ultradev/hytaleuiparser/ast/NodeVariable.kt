package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.ValidatorException

data class NodeVariable(
    val variable: NodeToken
) : AstNode(), VariableReference {
    override val children: List<AstNode>
        get() = listOf(variable)

    val identifier get() = variable.text

    override fun validate() {
        if (identifier == "@") throw ValidatorException("Variable name cannot be empty", this)
    }

    val resolvedAssignment: NodeAssignVariable? get() = resolvedScope.lookupVariableAssignment(identifier)
    val resolvedValue: AstNode? get() = resolvedAssignment?.value
}
