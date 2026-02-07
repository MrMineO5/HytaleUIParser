package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.clone
import app.ultradev.hytaleuiparser.validation.types.TypeType

class NodeTranslation(
    children: List<AstNode>,
    valid: Boolean = true
) : AstNode(children, valid), VariableValue {
    val translationMarker by child<NodeToken>(0)
    val value by child<NodeConstant>(1)

    override val resolvedTypes: Set<TypeType>
        get() = setOf(TypeType.String)

    override fun clone() = NodeTranslation(children.clone(), valid)
}
