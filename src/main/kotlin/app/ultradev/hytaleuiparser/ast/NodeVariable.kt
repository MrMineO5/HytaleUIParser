package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.validation.Scope

data class NodeVariable(
    val variable: NodeToken
) : AstNode(), VariableReference {
    override val children: List<AstNode>
        get() = listOf(variable)

    override fun setScope(scope: Scope) {
        super.setScope(scope)
        variable.setScope(scope)
    }

    val identifier get() = variable.text

    override fun validate(validationError: (String, AstNode) -> Unit) {
        if (identifier == "@") validationError("Variable name cannot be empty", this)
    }

    val resolvedAssignment: NodeAssignVariable? get() = resolvedScope.lookupVariableAssignment(identifier)
    override val resolvedValue: VariableValue? get() = resolvedAssignment?.valueAsVariable
}
