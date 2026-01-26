package app.ultradev.hytaleuiparser

import app.ultradev.hytaleuiparser.ast.*
import app.ultradev.hytaleuiparser.validation.ElementType
import app.ultradev.hytaleuiparser.validation.Scope
import app.ultradev.hytaleuiparser.validation.resolveNeighbour
import app.ultradev.hytaleuiparser.validation.types.TypeType
import java.util.Stack

class Validator(
    val files: Map<String, RootNode>
) {
    private val validated: MutableSet<String> = mutableSetOf()

    fun validate() {
        files.keys.forEach(::validateRoot)
    }

    fun validateRoot(path: String) {
        if (validated.contains(path)) return
        validated.add(path)

        val root = files[path] ?: error("No root node for $path")
        root.path = path
        root.initFile(root)
        try {
            val scope = Scope(
                root.references.associate {
                    it.variable.identifier.identifier to path.resolveNeighbour(it.filePath.valueText)
                }.toMutableMap(), root.variables
            )
            root.setScope(scope)

            root.variableValues = scope.variables

            root.elements.forEach {
                validateElement(it)
            }
        } catch (e: Exception) {
            throw ValidatorException("Failed to validate $path", root, e)
        }
    }

    private fun findElementType(node: NodeElement): ElementType {
        return when (val type = node.type) {
            is NodeIdentifier -> ElementType.valueOf(type.identifier)
            is NodeVariable, is NodeRefMember -> {
                val element = deepLookupReference(type)
                if (element !is NodeElement) throw ValidatorException("Expected element, got ${element::class.simpleName}", type)
                findElementType(element)
            }

            else -> error("Unknown element type: $type")
        }
    }

    fun validateElement(node: NodeElement) {
        val childScope = Scope(node.resolvedScope, node.localVariables)
        node.body.setScope(childScope)

        val elementType = findElementType(node)
        node.resolvedType = elementType

        if (!elementType.allowsChildren() && node.childElements.isNotEmpty())
            throw ValidatorException("Element ${elementType.name} does not allow children", node)

        node.properties.forEach {
            val typeType = elementType.properties[it.identifier.identifier]
                ?: throw ValidatorException("Unknown property ${it.identifier.identifier} on $elementType", it)
            validateProperty(it.value, typeType)
        }

        node.childElements.forEach { validateElement(it) }
        node.selectorElements.forEach { validateSelectorElement(node, it) }
    }

    private fun lookupSelectorElementSource(parent: NodeElement, selector: NodeSelectorElement): NodeElement {
        val parentDef = when (parent.type) {
            is NodeIdentifier -> throw ValidatorException("Cannot have selector elements inside plain elements", parent)
            is NodeVariable, is NodeRefMember -> deepLookupReference(parent.type) as NodeElement
            else -> throw ValidatorException("Unknown parent type: ${parent.type}", parent)
        }
        validateElement(parentDef)
        val sel = parentDef.childElements.find { it.selector?.selector?.text == selector.selector?.selector?.text }
            ?: error("No selector element $selector on ${parentDef.type}") // TODO: Can selectors be transitive?
        return sel
    }
    fun validateSelectorElement(parent: NodeElement, node: NodeSelectorElement) {
        val source = lookupSelectorElementSource(parent, node)
        validateElement(source)
        val elementType = source.resolvedType

        val childScope = Scope(node.resolvedScope, node.localVariables)
        node.body.setScope(childScope)

        if (!elementType.allowsChildren()) assert(node.childElements.isEmpty()) {
            "Element ${elementType.name} does not allow children"
        }

        node.properties.forEach {
            val typeType = elementType.properties[it.identifier.identifier]
                ?: error("Unknown property ${it.identifier.identifier} on $elementType for node $node")
            validateProperty(it.value, typeType)
        }

        node.childElements.forEach { validateElement(it) }
    }

    fun validateProperty(node: AstNode, type: TypeType) {
        val value = when (node) {
            is VariableReference -> deepLookupReference(node)
            is VariableValue -> node
            else -> throw ValidatorException("Unknown property type: ${node::class.simpleName}", node)
        }

        if (type.isPrimitive) {
            when (value) {
                is NodeConstant -> {
                    validatePrimitive(type, value)
                }
                is NodeMathOperation -> {
                    validateProperty(value.param1, type)
                    validateProperty(value.param2, type)

                    // TODO: Validate the operation is allowed on operands
                }
                is NodeColor -> {
                    if (type != TypeType.Color) throw ValidatorException("Expected $type, got ${value::class.simpleName}", value)
                }
                is NodeNegate -> {
                    if (!type.canNegate()) throw ValidatorException("Got negation for non-negatable type $type", value)
                    validateProperty(value.param, type)
                }

                is NodeTranslation -> {
                    if (type != TypeType.String) throw ValidatorException("Expected $type, got ${value::class.simpleName}", value)
                }

                else -> throw ValidatorException("Expected primitive value, got ${value::class.simpleName}", value)
            }
        } else if (type.isEnum) {
            if (value !is NodeConstant) throw ValidatorException("Expected constant, got ${value::class.simpleName}", value)
            if (value.valueText !in type.enum) throw ValidatorException("Invalid enum value: ${value.valueText}", value)
        } else {
            if (type == TypeType.PatchStyle) {
                // Special case for patch styles, they can be replaced by a color or texture path
                if (value is NodeType) return validateType(value, type)
                if (value is NodeColor || value is NodeConstant) return
                throw ValidatorException("Expected PatchStyle, color or texture path, got ${value::class.simpleName}", value)
            }
            if (type == TypeType.Padding) {
                // Special case for padding, a single number represents Full
                if (value is NodeConstant) return validatePrimitive(TypeType.Padding.allowedFields["Full"]!!, value)
            }

            if (value !is NodeType) throw ValidatorException("Expected type, got ${value::class.simpleName}", value)
            validateType(value, type)
        }
    }

    fun validateType(node: NodeType, type: TypeType) {
        node.spreads.forEach { spread ->
            val value = deepLookupReference(spread.variableAsReference)
            if (value !is NodeType) throw ValidatorException("Expected type, got ${value::class.simpleName}", value)
            validateType(value, type)
        }
        node.fields.forEach { field ->
            val reqType = type.allowedFields[field.identifier.identifier]
                ?: throw ValidatorException("Unknown field ${field.identifier.identifier} on $type", field)
            validateProperty(field.value, reqType)
        }
    }

    fun lookupRefMember(ref: NodeRefMember): AstNode {
        try {
            val reference = ref.resolvedScope.lookupReference(ref.reference.identifier.identifier)
            validateRoot(reference)
            val rootNode = files[reference]!!
            return rootNode.variableValues[ref.member.identifier.identifier] ?: throw ValidatorException(
                "No member ${ref.member.identifier.identifier} on ${ref.reference.identifier.identifier}", ref
            )
        } catch(e: UninitializedPropertyAccessException) {
            throw ValidatorException("Variable reference lookup before scope init", ref)
        }
    }

    fun deepLookupReference(reference: VariableReference): AstNode {
//        println("Looking up ${(reference as AstNode).text} in $scope")
        val result = when (reference) {
            is NodeRefMember -> lookupRefMember(reference)
            is NodeVariable -> {
                try {
                    reference.resolvedScope.lookupVariable(reference.identifier.identifier)
                } catch (e: UninitializedPropertyAccessException) {
                    throw ValidatorException("Variable reference lookup before scope init", reference)
                } catch(e: Exception) {
                    throw ValidatorException("Failed to lookup variable ${reference.identifier.identifier} in ${reference.resolvedScope}", reference, e)
                }
            }
            is NodeMemberField -> {
                var stack = Stack<NodeMemberField>()
                var curr: AstNode = reference
                while (curr is NodeMemberField) {
                    stack.push(curr)
                    curr = curr.parent
                }

                val ownerValue = if (curr is VariableReference) deepLookupReference(curr) else curr
                if (ownerValue !is NodeType)
                    throw ValidatorException("Expected type, got ${ownerValue::class.simpleName}", ownerValue)

                var currentType: AstNode = ownerValue
                while (stack.isNotEmpty()) {
                    if (currentType !is NodeType)
                        throw ValidatorException("Tried to look up field on non-type", currentType)
                    val fieldName = stack.pop().member.identifier
                    currentType = lookupTypeField(currentType, fieldName)
                        ?: throw ValidatorException("No field $fieldName", currentType)
                }
                currentType
            }
        }
        if (result is VariableReference) return deepLookupReference(result)
        return result
    }

    fun validatePrimitive(type: TypeType, value: NodeConstant) {
        when (type) {
            TypeType.String -> {} // All constants are strings
            TypeType.Integer -> value.valueText.toIntOrNull() ?: throw ValidatorException("Invalid integer value: ${value.valueText}", value)
            TypeType.Float -> value.valueText.toFloatOrNull() ?: throw ValidatorException("Invalid float value: ${value.valueText}", value)
            TypeType.Double -> value.valueText.toDoubleOrNull() ?: throw ValidatorException("Invalid double value: ${value.valueText}", value)
            TypeType.Boolean -> value.valueText.toBooleanStrictOrNull() ?: throw ValidatorException("Invalid boolean value: ${value.valueText}", value)

            else -> error("Unknown primitive type: $type")
        }
    }

    fun lookupTypeField(type: NodeType, fieldName: String): AstNode? {
        val declaredField = type.fields.find { it.identifier.identifier == fieldName }
        if (declaredField != null) return declaredField.value

        val inheritedField = type.spreads.mapNotNull {
            val source = deepLookupReference(it.variableAsReference)
            if (source !is NodeType)
                throw ValidatorException("Cannot spread non-type, got ${source::class.simpleName}", source)
            lookupTypeField(source, fieldName)
        }
        return inheritedField.firstOrNull()
    }
}
