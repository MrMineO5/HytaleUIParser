package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.token.GeneratedTokens
import app.ultradev.hytaleuiparser.validation.Scope
import app.ultradev.hytaleuiparser.validation.types.TypeType

data class NodeAssignVariable(
    val variable: NodeVariable,
    val assignment: NodeToken,
    val value: AstNode,
    val endStatement: NodeToken
) : AstNode() {
    init {
        if (value !is VariableValue) {
            error("Expected variable value after assignment operator: $value")
        }
    }

    val valueAsVariable: VariableValue = value as VariableValue

    override val children: List<AstNode>
        get() = listOf(variable, assignment, value, endStatement)

    constructor(variable: NodeVariable, value: AstNode) : this(variable, GeneratedTokens.assignment(), value, GeneratedTokens.endStatement())

    override fun setScope(scope: Scope) {
        super.setScope(scope)
        variable.setScope(scope)
        value.setScope(scope)

        assignment.setScope(scope)
        endStatement.setScope(scope)
    }

    override fun computePath(): String = super.computePath() + ":${variable.identifier}"

    override fun clone() = NodeAssignVariable(variable.clone(), assignment.clone(), value.clone(), endStatement.clone())
}