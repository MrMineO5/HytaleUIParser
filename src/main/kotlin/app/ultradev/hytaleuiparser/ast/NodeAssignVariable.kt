package app.ultradev.hytaleuiparser.ast

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
}