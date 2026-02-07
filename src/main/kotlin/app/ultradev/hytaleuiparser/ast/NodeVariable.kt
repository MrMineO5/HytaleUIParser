package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.clone

class NodeVariable(
    children: List<AstNode>,
    valid: Boolean = true,
) : AstNode(children, valid), VariableReference {
    val variable by child<NodeToken>(0)

    val identifier get() = variable.text

    override fun validate(validationError: (String, AstNode) -> Unit) {
        if (identifier == "@") validationError("Variable name cannot be empty", this)
    }

    val resolvedAssignment: NodeAssignVariable? get() = resolvedScope?.lookupVariableAssignment(identifier)
    override val resolvedValue: VariableValue? get() = resolvedAssignment?.valueAsVariable

    override fun clone() = NodeVariable(children.clone(), valid)
}
