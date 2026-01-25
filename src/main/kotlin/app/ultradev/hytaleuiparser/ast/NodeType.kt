package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.validation.types.TypeType

data class NodeType(
    val type: NodeIdentifier?,
    val body: NodeBody
) : AstNode(), VariableValue {
    override val children: List<AstNode>
        get() = listOfNotNull(type) + listOf(body)

    init {
        body.elements.forEach {
            if (it is NodeField || it is NodeSpread || it is NodeRefMember) return@forEach
            error(
                "Unexpected node in element body: $it. Expected NodeField or NodeSpread."
            )
        }
    }

    lateinit var derivedType: TypeType
}
