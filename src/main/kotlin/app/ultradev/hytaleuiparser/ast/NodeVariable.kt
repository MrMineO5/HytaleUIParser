package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.validation.Scope

data class NodeVariable(
    val variableMarker: NodeToken,
    val identifier: NodeIdentifier
) : AstNode(), VariableReference {
    override val children: List<AstNode>
        get() = listOf(variableMarker, identifier)

    override lateinit var resolvedScope: Scope

    override fun _initResolvedScope(scope: Scope) {
        resolvedScope = scope
    }
}
