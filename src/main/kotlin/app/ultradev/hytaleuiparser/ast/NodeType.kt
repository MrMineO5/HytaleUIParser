package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.validation.Scope
import app.ultradev.hytaleuiparser.validation.types.TypeType

data class NodeType(
    val type: NodeIdentifier?,
    val body: NodeBody
) : AstNode(), VariableValue {
    override val children: List<AstNode>
        get() = listOfNotNull(type) + listOf(body)

    init {
        body.elements.forEach {
            if (it is NodeField || it is NodeSpread) return@forEach
            error(
                "Unexpected node in element body: $it. Expected NodeField or NodeSpread."
            )
        }
    }

    val spreads: List<NodeSpread> get() = body.elements.filterIsInstance<NodeSpread>()
    val fields: List<NodeField> get() = body.elements.filterIsInstance<NodeField>()

    lateinit var resolvedScope: Scope
    lateinit var derivedType: TypeType

    override fun _initResolvedScope(scope: Scope) {
        resolvedScope = scope
    }
}
