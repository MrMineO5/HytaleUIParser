package app.ultradev.hytaleuiparser.validation

import app.ultradev.hytaleuiparser.ValidatorException
import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.ast.NodeAssignVariable
import app.ultradev.hytaleuiparser.ast.NodeVariable

class Scope(
    val parent: Scope? = null,
    val variables: MutableMap<String, AstNode> = mutableMapOf(),
    val references: Map<String, String> = emptyMap()
) {
    constructor(parent: Scope?, variables: List<NodeAssignVariable>) : this(
        parent,
        variables.associate { it.variable.identifier.identifier to it.value }.toMutableMap()
    ) {
        val seenKeys = mutableSetOf<String>()
        variables.forEach {
            if (seenKeys.contains(it.variable.identifier.identifier))
                throw ValidatorException("Duplicate variable: ${it.variable.identifier.identifier}", it)
            seenKeys.add(it.variable.identifier.identifier)
        }
    }

    constructor(references: Map<String, String>, variables: List<NodeAssignVariable>) : this(
        null,
        variables.associate { it.variable.identifier.identifier to it.value }.toMutableMap(),
        references
    ) {
        val seenKeys = mutableSetOf<String>()
        variables.forEach {
            if (seenKeys.contains(it.variable.identifier.identifier))
                throw ValidatorException("Duplicate variable: ${it.variable.identifier.identifier}", it)
            seenKeys.add(it.variable.identifier.identifier)
        }
    }

    fun lookupVariable(name: String): AstNode = variables[name] ?: parent?.lookupVariable(name) ?: error("Variable $name not found, scope: $this")
    fun lookupReference(name: String): String = references[name] ?: parent?.lookupReference(name) ?: error("Reference $name not found, scope: ")

    override fun toString(): String {
        return "Scope(\n" +
                "${parent.toString().prependIndent("  ")},\n" +
                "  variables=[${variables.keys.joinToString(", ")}],\n" +
                "  references=${references}\n" +
                ")"
    }
}
