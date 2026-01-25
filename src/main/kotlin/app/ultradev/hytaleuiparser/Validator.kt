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
        root.path = path
        root.initFile(root)
        try {
            val scope = Scope(
                root.references.associate {
                    it.variable.identifier.identifier to path.resolveNeighbour(it.filePath.valueText)
                }.toMutableMap(), root.variables
            )
            root.variables.forEach {
                it.valueAsVariable._initResolvedScope(scope)
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
            is NodeVariable, is NodeRefMember -> {
                val element = deepLookupReference(type, scope)
                if (element !is NodeElement) throw ValidatorException("Expected element, got ${element::class.simpleName}", type)
                findElementType(element, scope)
            }

            else -> error("Unknown element type: $type")
        }
    }

    fun validateElement(node: NodeElement, scope: Scope) {
        node.resolvedScope = scope

        val childScope = Scope(scope, node.localVariables)
        node.localVariables.forEach {
            it.valueAsVariable._initResolvedScope(childScope)
        }

        val elementType = findElementType(node, scope)
        node.resolvedType = elementType

        if (!elementType.allowsChildren()) assert(node.childElements.isEmpty()) {
            "Element ${elementType.name} does not allow children"
        }

        node.properties.forEach {
            val typeType = elementType.properties[it.identifier.identifier]
                ?: throw ValidatorException("Unknown property ${it.identifier.identifier} on $elementType", it)
            validateProperty(it.value, typeType, childScope)
        }

        node.childElements.forEach { validateElement(it, childScope) }
        node.selectorElements.forEach { validateSelectorElement(node, it, childScope) }
    }

    private fun lookupSelectorElementSource(parent: NodeElement, selector: String, scope: Scope): NodeElement {
        val parentDef = when (parent.type) {
            is NodeIdentifier -> error("Cannot have selector elements inside plain elements")
            is NodeVariable, is NodeRefMember -> deepLookupReference(parent.type, scope) as NodeElement
            else -> error("Unknown parent type: ${parent.type}")
        }
        validateElement(parentDef, parentDef.resolvedScope)
        val sel = parentDef.childElements.find { it.selector?.identifier?.identifier == selector }
            ?: error("No selector element $selector on ${parentDef.type}") // TODO: Can selectors be transitive?
        return sel
    }

    fun validateSelectorElement(parent: NodeElement, node: NodeSelectorElement, scope: Scope) {
        val source = lookupSelectorElementSource(parent, node.selector.identifier.identifier, scope)
        validateElement(source, source.resolvedScope)
        val elementType = source.resolvedType

        val childScope = Scope(scope, node.localVariables)
        node.localVariables.forEach {
            it.valueAsVariable._initResolvedScope(childScope)
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
            is NodeVariable, is NodeRefMember -> {
                val value = deepLookupReference(node, scope)
                if (type.isPrimitive) {
                    when (value) {
                        is NodeConstant -> {
                            validatePrimitive(type, value)
                        }
                        is NodeMathOperation -> {
                            validateProperty(value.param1, type, scope)
                            validateProperty(value.param2, type, scope)
                        }

                        else -> throw ValidatorException("Expected primitive value, got ${value::class.simpleName}", value)
                    }
                } else if (type.isEnum) {
                    if (value !is NodeConstant) throw ValidatorException("Expected constant, got ${value::class.simpleName}", value)
                    if (value.valueText !in type.enum) throw ValidatorException("Invalid enum value: ${value.valueText}", value)
                } else {
                    if (value !is NodeType) throw ValidatorException("Expected type, got ${value::class.simpleName}", value)
                    validateType(value, type, value.resolvedScope)
                }
            }
        }
    }

    fun validateType(node: NodeType, type: TypeType, scope: Scope) {
        node.spreads.forEach { spread ->
            val value = deepLookupReference(spread.variableAsReference, scope)
            if (value !is NodeType) throw ValidatorException("Expected type, got ${value::class.simpleName}", value)
            validateType(value, type, value.resolvedScope)
        }
        node.fields.forEach { field ->
            val reqType = type.allowedFields[field.identifier.identifier]
                ?: throw ValidatorException("Unknown field: ${field.identifier.identifier} on $type", field)
            validateProperty(field.value, reqType, scope)
        }
    }

    fun lookupRefMember(ref: NodeRefMember, scope: Scope): AstNode {
        val reference = scope.lookupReference(ref.reference.identifier.identifier)
        validateRoot(reference)
        val rootNode = files[reference]!!
        return rootNode.variableValues[ref.member.identifier.identifier] ?: throw ValidatorException(
            "No member ${ref.member.identifier.identifier} on ${ref.reference.identifier.identifier}", ref
        )
    }

    fun deepLookupReference(reference: VariableReference, scope: Scope): AstNode {
//        println("Looking up ${(reference as AstNode).text} in $scope")
        val result = when (reference) {
            is NodeRefMember -> lookupRefMember(reference, scope)
            is NodeVariable -> {
                try {
                    scope.lookupVariable(reference.identifier.identifier)
                } catch(e: Exception) {
                    throw ValidatorException("Failed to lookup variable ${reference.identifier.identifier} in $scope", reference, e)
                }
            }
        }
        if (result is VariableReference) return deepLookupReference(result, result.resolvedScope)
        return result
    }

    fun validatePrimitive(type: TypeType, value: NodeConstant) {
        when (type) {
            TypeType.String -> {} // All constants are strings
            TypeType.Integer -> value.valueText.toIntOrNull() ?: throw ValidatorException("Invalid integer value: ${value.valueText}", value)
            TypeType.Boolean -> value.valueText.toBooleanStrictOrNull() ?: throw ValidatorException("Invalid boolean value: ${value.valueText}", value)

            else -> error("Unknown primitive type: $type")
        }
    }
}
