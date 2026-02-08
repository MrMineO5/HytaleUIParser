package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.clone
import app.ultradev.hytaleuiparser.validation.types.TypeType

class NodeMathOperation(
    children: List<AstNode>,
    valid: Boolean = true
) : AstNode(children, valid), VariableValue {
    val param1 by child<AstNode>(0)
    val operator by child<NodeToken>(1)
    val param2 by child<AstNode>(2)

    val param1AsVariable: VariableValue get() = param1 as VariableValue
    val param2AsVariable: VariableValue get() = param2 as VariableValue

    override fun validate(validationError: (String, AstNode) -> Unit) {
        if (param1 !is VariableValue) validationError("Expected variable before math operation", findClosestChild(0))
        if (param2 !is VariableValue) validationError("Expected variable after math operation", findClosestChild(2))
    }

    override val resolvedTypes: Set<TypeType>?
        get() = param1AsVariable.resolvedTypes

    override fun clone() = NodeMathOperation(children.clone(), valid)
}
