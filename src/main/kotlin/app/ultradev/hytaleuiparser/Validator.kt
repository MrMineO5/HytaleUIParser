package app.ultradev.hytaleuiparser

import app.ultradev.hytaleuiparser.ast.*
import app.ultradev.hytaleuiparser.validation.ElementType
import app.ultradev.hytaleuiparser.validation.Scope
import app.ultradev.hytaleuiparser.validation.resolveNeighbour
import app.ultradev.hytaleuiparser.validation.types.TypeType

class Validator(
    val files: Map<String, RootNode>
) {
    private val validated: MutableSet<String> = mutableSetOf()

    fun validate() {
        files.keys.forEach(::validateRoot)
    }

    fun validateRoot(path: String) {
        if (validated.contains(path)) return
        val root = files[path] ?: error("No root node for $path")
        try {
            val scope = Scope(references = root.references.associate {
                it.variable.identifier.identifier to path.resolveNeighbour(it.filePath.valueText)
            }.toMutableMap())

            root.variables.forEach {
                validateAndAppendVariable(it, scope)
            }

            root.variableValues = scope.variables

            root.elements.forEach {
                validateElement(it, scope)
            }
        } catch (e: Exception) {
            throw ValidatorException("Failed to validate $path", root, e)
        }
    }

    private fun findElementType(node: NodeElement, scope: Scope): ElementType {
        return when (val type = node.type) {
            is NodeIdentifier -> ElementType.valueOf(type.identifier)
            is NodeVariable -> {
                val element = scope.lookupVariable(type.identifier.identifier)
                if (element !is NodeElement) error("Expected element, got ${element::class.simpleName}")
                findElementType(element, scope)
            }

            is NodeRefMember -> {
                val element = lookupRefMember(type, scope)
                if (element !is NodeElement) error("Expected element, got ${element::class.simpleName}")
                findElementType(element, scope)
            }

            else -> error("Unknown element type: $type")
        }
    }

    fun validateElement(node: NodeElement, scope: Scope) {
        val childScope = Scope(scope)
        node.localVariables.forEach {
            validateAndAppendVariable(it, childScope)
        }

        val elementType = findElementType(node, scope)
        node.resolvedType = elementType

        if (!elementType.allowsChildren()) assert(node.childElements.isEmpty()) {
            "Element ${elementType.name} does not allow children"
        }

        node.properties.forEach {
            val typeType = elementType.properties[it.identifier.identifier]
                ?: error("Unknown property ${it.identifier.identifier} on $elementType for node $node")
            validateProperty(it.value, typeType, childScope)
        }

        node.childElements.forEach { validateElement(it, childScope) }
        node.selectorElements.forEach { validateSelectorElement(node, it, childScope) }
    }

    fun validateAndAppendVariable(assignment: NodeAssignVariable, scope: Scope) {
        validateVariableAssignment(assignment, scope)
        scope.variables[assignment.variable.identifier.identifier] = assignment.value
    }

    fun validateVariableAssignment(assignment: NodeAssignVariable, scope: Scope) {
        if (scope.variables[assignment.variable.identifier.identifier] != null) throw ValidatorException(
            "Variable ${assignment.variable.identifier.identifier} already defined",
            assignment
        )

        val value = assignment.value

        when (value) {
            is NodeElement -> validateElement(value, scope)
            is NodeVariable -> {
                try {
                    scope.lookupVariable(value.identifier.identifier)
                } catch (e: Exception) {
                    throw ValidatorException("Variable ${value.identifier.identifier} not found", assignment, e)
                }
            }

            is NodeRefMember -> {
                try {
                    lookupRefMember(value, scope)
                } catch (e: Exception) {
                    throw ValidatorException(
                        "Reference ${value.text} not found",
                        assignment,
                        e
                    )
                }
            }

            is NodeType -> {} // Cannot validate here, need to validate on usage
            is NodeColor, is NodeConstant -> {} // No validation needed

            else -> throw ValidatorException("Unknown variable value type: ${value::class.simpleName}", assignment)
        }
    }

    private fun lookupSelectorElementSource(parent: NodeElement, selector: String, scope: Scope): NodeElement {
        val parentDef = when (parent.type) {
            is NodeIdentifier -> error("Cannot have selector elements inside plain elements")
            is NodeVariable -> scope.lookupVariable(parent.type.identifier.identifier) as NodeElement
            is NodeRefMember -> lookupRefMember(parent.type, scope) as NodeElement
            else -> error("Unknown parent type: ${parent.type}")
        }
        val sel = parentDef.childElements.find { it.selector?.identifier?.identifier == selector }
            ?: error("No selector element $selector on ${parentDef.type}") // TODO: Can selectors be transitive?
        return sel
    }

    fun validateSelectorElement(parent: NodeElement, node: NodeSelectorElement, scope: Scope) {
        val source = lookupSelectorElementSource(parent, node.selector.identifier.identifier, scope)
        val elementType = source.resolvedType

        val childScope = Scope(scope)
        node.localVariables.forEach {
            validateAndAppendVariable(it, childScope)
        }

        if (!elementType.allowsChildren()) assert(node.childElements.isEmpty()) {
            "Element ${elementType.name} does not allow children"
        }

        node.properties.forEach {
            val typeType = elementType.properties[it.identifier.identifier]
                ?: error("Unknown property ${it.identifier.identifier} on $elementType for node $node")
            validateProperty(it.value, typeType, childScope)
        }

        node.childElements.forEach { validateElement(it, childScope) }
    }

    fun validateProperty(node: AstNode, type: TypeType, scope: Scope) {
        when (node) {
            is NodeType -> validateType(node, type, scope)
            is NodeVariable -> {
                val value = scope.lookupVariable(node.identifier.identifier)
                if (value !is NodeType) error("Expected type, got ${value::class.simpleName}")
                validateType(value, type, scope)
            }
        }
    }

    fun validateType(node: NodeType, type: TypeType, scope: Scope) {
        node.spreads.forEach { spread ->
            val value = when (spread.variable) {
                is NodeVariable -> scope.lookupVariable(spread.variable.identifier.identifier)
                is NodeRefMember -> lookupRefMember(spread.variable, scope)
                else -> error("Expected variable or reference")
            }
            if (value !is NodeType) error("Expected type, got ${value::class.simpleName}")
            validateType(value, type, scope)
        }
        node.fields.forEach { field ->
            val reqType = type.allowedFields[field.identifier.identifier]
                ?: error("Unknown field: ${field.identifier.identifier} on $type for node $node")
            validateProperty(field.value, reqType, scope)
        }
    }

    fun lookupRefMember(ref: NodeRefMember, scope: Scope): AstNode {
        val reference = scope.lookupReference(ref.reference.identifier.identifier)
        validateRoot(reference)
        val rootNode = files[reference]!!
        return rootNode.variableValues[ref.member.identifier.identifier] ?: throw ValidatorException(
            "No member ${ref.member.identifier.identifier} on ${ref.reference.identifier.identifier}",
            ref
        )
    }
}
