package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.validation.Scope
import app.ultradev.hytaleuiparser.validation.types.TypeType

data class NodeMathOperation(
    val param1: AstNode,
    val operator: NodeToken,
    val param2: AstNode
) : AstNode(), VariableValue {
    override val children: List<AstNode>
        get() = listOf(param1, operator, param2)

    val param1AsVariable: VariableValue get() = param1 as VariableValue
    val param2AsVariable: VariableValue get() = param2 as VariableValue

    override fun validate(validationError: (String, AstNode) -> Unit) {
        if (param1 !is VariableValue) validationError("Expected variable before math operation", param1)
        if (param2 !is VariableValue) validationError("Expected variable after math operation", param2)
    }

    override fun setScope(scope: Scope) {
        super.setScope(scope)
        param1.setScope(scope)
        param2.setScope(scope)

        operator.setScope(scope)
    }

    override val resolvedTypes: Set<TypeType>?
        get() = param1AsVariable.resolvedTypes

    override fun clone() = NodeMathOperation(param1.clone(), operator.clone(), param2.clone())
}
