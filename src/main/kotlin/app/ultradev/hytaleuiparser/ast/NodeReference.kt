package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.token.GeneratedTokens

data class NodeReference(
    val refMarker: NodeToken, val identifier: NodeIdentifier
) : AstNode() {
    override val children: List<AstNode>
        get() = listOf(refMarker, identifier)

    constructor(identifier: NodeIdentifier) : this(GeneratedTokens.referenceMarker(), identifier)

    val resolvedAssignment: NodeAssignReference? get() = resolvedScope.lookupReferenceAssignment(identifier.identifier)
}
