package app.ultradev.hytaleuiparser.validation

import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.ast.NodeAssignReference
import app.ultradev.hytaleuiparser.ast.NodeAssignVariable

class Scope private constructor(
    private val parent: Scope? = null,
    val variableAssignments: Map<String, NodeAssignVariable> = emptyMap(),
    val referenceAssignments: Map<String, NodeAssignReference> = emptyMap(),
    private val allowMissingVariables: Boolean = false, // In variable elements, we can have a missing variable that must be defined in the implementation
) {
    companion object {
        fun root(
            references: List<NodeAssignReference>,
            variables: List<NodeAssignVariable>,
            validationError: (String, AstNode) -> Unit
        ): Scope {
            val seenKeys = mutableSetOf<String>()
            variables.forEach {
                if (seenKeys.contains(it.variable.identifier))
                    return@forEach validationError("Duplicate variable: ${it.variable.identifier}", it)
                seenKeys.add(it.variable.identifier)
            }
            return Scope(
                null,
                variables.associateBy { it.variable.identifier },
                references.associateBy { it.variable.identifier }
            )
        }
    }

    fun childScope(
        variables: List<NodeAssignVariable>,
        validationError: (String, AstNode) -> Unit,
        allowMissingVariables: Boolean = false
    ): Scope {
        val seenKeys = mutableSetOf<String>()
        variables.forEach {
            if (seenKeys.contains(it.variable.identifier))
                return@forEach validationError("Duplicate variable: ${it.variable.identifier}", it)
            seenKeys.add(it.variable.identifier)
        }
        return Scope(this, variables.associateBy { it.variable.identifier }, emptyMap(), allowMissingVariables)
    }

    fun lookupVariableAssignment(name: String): NodeAssignVariable? =
        variableAssignments[name] ?: parent?.lookupVariableAssignment(name)

    fun lookupReferenceAssignment(name: String): NodeAssignReference? =
        referenceAssignments[name] ?: parent?.lookupReferenceAssignment(name)

    fun lookupVariable(name: String): AstNode? = lookupVariableAssignment(name)?.value

    fun variableKeys(): Set<String> = variableAssignments.keys + (parent?.variableKeys() ?: emptySet())
    fun referenceKeys(): Set<String> = referenceAssignments.keys + (parent?.referenceKeys() ?: emptySet())

    fun isAllowMissingVariables(): Boolean = allowMissingVariables || parent?.isAllowMissingVariables() ?: false

    override fun toString(): String {
        return "Scope(\n" +
                "${parent.toString().prependIndent("  ")},\n" +
                "  variables=[${variableAssignments.keys.joinToString(", ")}],\n" +
                "  references=${referenceAssignments}\n" +
                ")"
    }
}
