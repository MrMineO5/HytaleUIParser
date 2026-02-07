package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.clone
import app.ultradev.hytaleuiparser.validation.types.TypeType

class NodeColor(
    children: List<AstNode>,
    valid: Boolean = true,
) : AstNode(children, valid), VariableValue {
    val value by child<NodeConstant>(0)
    val opacity by optionalChild<NodeOpacity>(1)

    override val resolvedTypes: Set<TypeType>
        get() = setOf(TypeType.Color)

    override fun clone() = NodeColor(children.clone(), valid)
}
