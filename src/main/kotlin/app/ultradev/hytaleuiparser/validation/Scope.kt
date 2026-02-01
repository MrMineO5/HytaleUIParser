package app.ultradev.hytaleuiparser.validation

import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.ast.NodeAssignVariable

class Scope private constructor(
    private val parent: Scope? = null,
    val variableAssignments: Map<String, NodeAssignVariable> = emptyMap(),
    private val allowMissingVariables: Boolean = false, // In variable elements, we can have a missing variable that must be defined in the implementation
) {
//    private val missingTypeVariables: MutableMap<String, MutableSet<TypeType>>? = if (allowMissingVariables) mutableMapOf() else null
//    private val missingElementVariables: MutableMap<String, MutableSet<ElementType>>? = if (allowMissingVariables) mutableMapOf() else null

    companion object {
        fun root(
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
                variables.associateBy { it.variable.identifier }
            )
        }
    }

    fun childScope(
        variables: Iterable<NodeAssignVariable>,
        validationError: (String, AstNode) -> Unit,
        allowMissingVariables: Boolean = false
    ): Scope {
        val seenKeys = mutableSetOf<String>()
        variables.forEach {
            if (seenKeys.contains(it.variable.identifier))
                return@forEach validationError("Duplicate variable: ${it.variable.identifier}", it)
            seenKeys.add(it.variable.identifier)
        }
        return Scope(this, variables.associateBy { it.variable.identifier }, allowMissingVariables)
    }

    fun childScope(other: Scope): Scope {
        return Scope(this, other.flatten().variableAssignments)
    }

    fun lookupVariableAssignment(name: String): NodeAssignVariable? =
        variableAssignments[name] ?: parent?.lookupVariableAssignment(name)

    fun lookupVariable(name: String): AstNode? = lookupVariableAssignment(name)?.value

    fun variableKeys(): Set<String> = variableAssignments.keys + (parent?.variableKeys() ?: emptySet())

    fun isAllowMissingVariables(): Boolean = allowMissingVariables || parent?.isAllowMissingVariables() ?: false
//    fun addMissingTypeVariable(name: String, type: TypeType) {
//        if (allowMissingVariables) {
//            this.missingTypeVariables!!.computeIfAbsent(name) { mutableSetOf() }.add(type)
//        } else {
//            if (parent == null) error("Could not find scope that allows missing variables")
//            parent.addMissingTypeVariable(name, type)
//        }
//    }
//    fun addMissingElementVariable(name: String, allowed: Set<ElementType>) {
//        if (allowMissingVariables) {
//            val set = this.missingElementVariables!![name]
//            if (set == null) {
//                this.missingElementVariables[name] = allowed.toMutableSet()
//            } else {
//                set.removeIf { it !in allowed }
//            }
//        } else {
//            if (parent == null) error("Could not find scope that allows missing variables")
//            parent.addMissingElementVariable(name, allowed)
//        }
//    }

    fun flatten(): Scope {
        if (parent == null) return this
        val pf = parent.flatten()
        val varAsns = pf.variableAssignments + variableAssignments
        return Scope(null, varAsns, isAllowMissingVariables())
    }

    override fun toString(): String {
        return "Scope(\n" +
                "${parent.toString().prependIndent("  ")},\n" +
                "  variables=[${variableAssignments.keys.joinToString(", ")}],\n" +
                ")"
    }
}
