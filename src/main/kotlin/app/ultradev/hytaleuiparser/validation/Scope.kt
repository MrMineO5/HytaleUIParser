package app.ultradev.hytaleuiparser.validation

import app.ultradev.hytaleuiparser.ValidatorException
import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.ast.NodeAssignReference
import app.ultradev.hytaleuiparser.ast.NodeAssignVariable

class Scope(
    val parent: Scope? = null,
    val variableAssignments: Map<String, NodeAssignVariable> = emptyMap(),
    val referenceAssignments: Map<String, NodeAssignReference> = emptyMap()
) {
    constructor(parent: Scope?, variables: List<NodeAssignVariable>) : this(
        parent,
        variables.associateBy { it.variable.identifier.identifier }
    ) {
        val seenKeys = mutableSetOf<String>()
        variables.forEach {
            if (seenKeys.contains(it.variable.identifier.identifier))
                throw ValidatorException("Duplicate variable: ${it.variable.identifier.identifier}", it)
            seenKeys.add(it.variable.identifier.identifier)
        }
    }

    constructor(references: List<NodeAssignReference>, variables: List<NodeAssignVariable>) : this(
        null,
        variables.associateBy { it.variable.identifier.identifier },
        references.associateBy { it.variable.identifier.identifier }
    ) {
        val seenKeys = mutableSetOf<String>()
        variables.forEach {
            if (seenKeys.contains(it.variable.identifier.identifier))
                throw ValidatorException("Duplicate variable: ${it.variable.identifier.identifier}", it)
            seenKeys.add(it.variable.identifier.identifier)
        }
    }

    fun lookupVariableAssignment(name: String): NodeAssignVariable? = variableAssignments[name] ?: parent?.lookupVariableAssignment(name)
    fun lookupReferenceAssignment(name: String): NodeAssignReference? = referenceAssignments[name] ?: parent?.lookupReferenceAssignment(name)

    fun lookupVariable(name: String): AstNode = lookupVariableAssignment(name)?.value ?: error("Variable $name not found, scope: $this")
    fun lookupReference(name: String): String = lookupReferenceAssignment(name)?.resolvedFilePath ?: error("Reference $name not found, scope: ")

    override fun toString(): String {
        return "Scope(\n" +
                "${parent.toString().prependIndent("  ")},\n" +
                "  variables=[${variableAssignments.keys.joinToString(", ")}],\n" +
                "  references=${referenceAssignments}\n" +
                ")"
    }
}
