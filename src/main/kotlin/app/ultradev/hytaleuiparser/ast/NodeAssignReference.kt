package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.token.GeneratedTokens

data class NodeAssignReference(
    val variable: NodeReference,
    val assignMarker: NodeToken,
    val filePath: NodeConstant,
    val endStatement: NodeToken
) : AstNode() {
    override val children: List<AstNode>
        get() = listOf(variable, assignMarker, filePath, endStatement)

    constructor(
        variable: NodeReference,
        filePath: NodeConstant
    ) : this(
        variable,
        GeneratedTokens.assignment(),
        filePath,
        GeneratedTokens.endStatement()
    )
}
