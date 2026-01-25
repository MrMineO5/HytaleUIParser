package app.ultradev.hytaleuiparser.validation

import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.ast.NodeAssignVariable
import app.ultradev.hytaleuiparser.ast.NodeVariable
import app.ultradev.hytaleuiparser.ast.RootNode
import java.nio.file.Path

class Scope(
    val parent: Scope? = null,
    val variables: MutableMap<String, AstNode> = mutableMapOf(),
    val references: MutableMap<String, String> = mutableMapOf()
) {
    constructor(parent: Scope?, variables: List<NodeAssignVariable>) : this(
        parent,
        variables.associate { it.variable.identifier.identifier to it.value }.toMutableMap()
    )

    fun lookupVariable(name: String): AstNode {
        val variable = lookupVariable0(name)
        if (variable is NodeVariable) return lookupVariable(variable.identifier.identifier)
        return variable
    }
    fun lookupReference(name: String): String = references[name] ?: parent?.lookupReference(name) ?: error("Reference $name not found")

    private fun lookupVariable0(name: String): AstNode = variables[name] ?: parent?.lookupVariable(name) ?: error("Variable $name not found")
}
