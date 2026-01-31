package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.ValidatorException
import app.ultradev.hytaleuiparser.validation.Scope

data class NodeType(
    val type: NodeIdentifier?,
    val body: NodeBody
) : AstNode(), VariableValue {
    override val children: List<AstNode>
        get() = listOfNotNull(type) + listOf(body)

    fun validate() {
        body.elements.forEach {
            if (it is NodeField || it is NodeSpread) return@forEach
            throw ValidatorException(
                "Unexpected node in element body. Expected NodeField or NodeSpread.",
                it
            )
        }
    }

    val spreads: List<NodeSpread> get() = body.elements.filterIsInstance<NodeSpread>()
    val fields: List<NodeField> get() = body.elements.filterIsInstance<NodeField>()

    override fun setScope(scope: Scope) {
        super.setScope(scope)
        body.setScope(scope)
    }
}
