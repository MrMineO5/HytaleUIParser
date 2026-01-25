package app.ultradev.hytaleuiparser

import app.ultradev.hytaleuiparser.ast.*
import app.ultradev.hytaleuiparser.validation.ElementType
import app.ultradev.hytaleuiparser.validation.Scope
import app.ultradev.hytaleuiparser.validation.types.TypeType
import java.nio.file.Path

class Validator(
    val files: Map<Path, RootNode>
) {
    private val validated: MutableSet<Path> = mutableSetOf()

    val types = mutableSetOf<String>()
    val properties = mutableMapOf<ElementType, MutableSet<String>>()
    val typeProperties = mutableMapOf<String, MutableSet<String>>()

    fun validate() {
        files.keys.forEach(::validateRoot)
    }

    fun validateRoot(path: Path) {
        if (validated.contains(path)) return
        val root = files[path] ?: error("No root node for $path")
        root.variableValues = root.variables.associate { it.variable.identifier.identifier to it.value }.toMap()
        val scope = Scope(
            variables = root.variableValues.toMutableMap(),
            references = root.references.associate { it.variable.identifier.identifier to path.parent.resolve(it.filePath.text).normalize() }.toMutableMap()
        )

        try {
            root.elements.forEach {
                validateElement(it, scope)
            }
        } catch(e: Exception) {
            throw ValidatorException("Failed to validate $path", root, e)
        }
    }

    private fun findElementType(node: NodeElement, scope: Scope): ElementType {
        val type = node.type
        return when(type) {
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
        val type = node.type

        val childScope = Scope(scope, node.localVariables)

        try {
            val elementType = findElementType(node, scope)

            if (!elementType.allowsChildren()) assert(node.childElements.isEmpty()) {
                "Element ${elementType.name} does not allow children"
            }

            node.properties.forEach {
                val typeType = elementType.properties[it.identifier.identifier] ?: error("Unknown property ${it.identifier.identifier} on $elementType for node $node")
                validateProperty(it.value, typeType, childScope)
            }
        } finally {
            node.childElements.forEach { validateElement(it, childScope) }
//            node.selectorElements.forEach { validateSelectorElement(it, childScope) }
        }
    }

//    fun validateSelectorElement(node: NodeSelectorElement, scope: Scope) {
//        node.childElements.forEach(::validateElement)
//        node.selectorElements.forEach(::validateSelectorElement)
//    }

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
        return rootNode.variableValues[ref.member.identifier.identifier] ?: error("No member ${ref.member.identifier.identifier} on ${ref.reference.identifier.identifier}")
    }
}
