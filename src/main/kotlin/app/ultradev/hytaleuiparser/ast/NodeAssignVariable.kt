package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.clone

class NodeAssignVariable(
    children: List<AstNode>,
    valid: Boolean = true
) : AstNode(children, valid) {
    val variable by child<NodeVariable>(0)
    val assignment by child<NodeToken>(1)
    val value by child<AstNode>(2)
    val endStatement by child<NodeToken>(3)

    val valueAsVariable: VariableValue = value as VariableValue

    override fun validate(validationError: (String, AstNode) -> Unit) {
        if (value !is VariableValue) validationError("Expected variable value after assignment operator", findClosestChild(2))
    }

    override fun computePath(): String = super.computePath() + ":${variable?.identifier}"

    override fun clone() = NodeAssignVariable(children.clone(), valid)
}