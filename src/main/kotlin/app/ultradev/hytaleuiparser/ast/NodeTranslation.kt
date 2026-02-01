package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.validation.Scope
import app.ultradev.hytaleuiparser.validation.types.TypeType

data class NodeTranslation(
    val translationMarker: NodeToken,
    val value: NodeConstant
) : AstNode(), VariableValue {
    override val children: List<AstNode>
        get() = listOf(translationMarker, value)

    override val resolvedTypes: Set<TypeType>
        get() = setOf(TypeType.String)

    override fun setScope(scope: Scope) {
        super.setScope(scope)
        translationMarker.setScope(scope)
        value.setScope(scope)
    }
}
