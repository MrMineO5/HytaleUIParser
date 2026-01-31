package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.ValidatorException

data class NodeReference(
    val reference: NodeToken
) : AstNode() {
    override val children: List<AstNode>
        get() = listOf(reference)

    val identifier get() = reference.text

    override fun validate() {
        if (identifier == "$") throw ValidatorException("Reference name cannot be empty", this)
    }

    val resolvedAssignment: NodeAssignReference? get() = resolvedScope.lookupReferenceAssignment(identifier)
}
