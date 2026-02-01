package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.token.GeneratedTokens
import app.ultradev.hytaleuiparser.validation.Scope
import app.ultradev.hytaleuiparser.validation.resolveNeighbour

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

    override fun setScope(scope: Scope) {
        super.setScope(scope)
        variable.setScope(scope)

        assignMarker.setScope(scope)
        endStatement.setScope(scope)
        filePath.setScope(scope)
    }

    val resolvedFilePath: String get() = file.path.resolveNeighbour(filePath.valueText)


    override fun computePath(): String = super.computePath() + variable.identifier
}
