package app.ultradev.hytaleuiparser.validation

import app.ultradev.hytaleuiparser.ValidatorError
import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.ast.NodeAssignReference
import app.ultradev.hytaleuiparser.ast.NodeAssignVariable

class Scope(
    val parent: Scope? = null,
    val variableAssignments: Map<String, NodeAssignVariable> = emptyMap(),
    val referenceAssignments: Map<String, NodeAssignReference> = emptyMap()
) {
    constructor(parent: Scope?, variables: List<NodeAssignVariable>, validationError: (String, AstNode) -> Unit) : this(
        parent,
        variables.associateBy { it.variable.identifier }
    ) {
        val seenKeys = mutableSetOf<String>()
        variables.forEach {
            if (seenKeys.contains(it.variable.identifier)) 
                return@forEach validationError("Duplicate variable: ${it.variable.identifier}", it)
            seenKeys.add(it.variable.identifier)
        }
    }

    constructor(references: List<NodeAssignReference>, variables: List<NodeAssignVariable>, validationError: (String, AstNode) -> Unit) : this(
        null,
        variables.associateBy { it.variable.identifier },
        references.associateBy { it.variable.identifier }
    ) {
        val seenKeys = mutableSetOf<String>()
        variables.forEach {
            if (seenKeys.contains(it.variable.identifier))
                return@forEach validationError("Duplicate variable: ${it.variable.identifier}", it)
            seenKeys.add(it.variable.identifier)
        }
    }

    fun lookupVariableAssignment(name: String): NodeAssignVariable? = variableAssignments[name] ?: parent?.lookupVariableAssignment(name)
    fun lookupReferenceAssignment(name: String): NodeAssignReference? = referenceAssignments[name] ?: parent?.lookupReferenceAssignment(name)

    fun lookupVariable(name: String): AstNode? = lookupVariableAssignment(name)?.value

    fun variableKeys(): Set<String> = variableAssignments.keys + (parent?.variableKeys() ?: emptySet())
    fun referenceKeys(): Set<String> = referenceAssignments.keys + (parent?.referenceKeys() ?: emptySet())

    override fun toString(): String {
        return "Scope(\n" +
                "${parent.toString().prependIndent("  ")},\n" +
                "  variables=[${variableAssignments.keys.joinToString(", ")}],\n" +
                "  references=${referenceAssignments}\n" +
                ")"
    }
}
