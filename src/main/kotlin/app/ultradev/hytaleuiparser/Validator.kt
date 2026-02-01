package app.ultradev.hytaleuiparser

import app.ultradev.hytaleuiparser.ast.*
import app.ultradev.hytaleuiparser.validation.ElementType
import app.ultradev.hytaleuiparser.validation.Scope
import app.ultradev.hytaleuiparser.validation.types.TypeType
import java.util.*

class Validator(
    val files: Map<String, RootNode>,
    val validateUnusedVariables: Boolean = false,
) {
    private val validated: MutableSet<String> = mutableSetOf()
    val validationErrors: MutableList<ValidatorError> = mutableListOf()

    fun validationError(message: String, astNode: AstNode) {
        validationErrors.add(ValidatorError(message, astNode))
    }

    fun validate() {
        validationErrors.clear()

        files.keys.forEach(::validateRoot)
    }

    fun validateRoot(path: String, revalidate: Boolean = false): RootNode? {
        if (validated.contains(path) && !revalidate) return files[path]!!
        validated.add(path)

        val root = files[path] ?: return null

        // Recursively run static validations on all children
        root.validate0(::validationError)
        // Initialize the file path
        root.path = path
        // Initialize the file pointer in all children
        root.initFile(root)

        // Ensure no duplicate references
        val seenReferences = mutableSetOf<String>()
        root.references.forEach {
            if (!seenReferences.add(it.variable.identifier))
                validationError("Duplicate reference: ${it.variable.identifier}", it)
        }

        // Create root variable scope
        val scope = Scope.root(
            root.variables, ::validationError
        )
        root.setScope(scope)

        if (validateUnusedVariables) {
            // Validate all variables
            root.variables.forEach { validateAssignVariable(it) }
        }

        // Validate elements
        root.elements.forEach {
            validateElement(it)
        }

        return root
    }

    private fun findElementType(node: NodeElement): ElementType? {
        return when (val type = node.type) {
            is NodeIdentifier -> {
                try {
                    ElementType.valueOf(type.identifier)
                } catch (_: IllegalArgumentException) {
                    validationError("Unknown element type: ${type.identifier}", type)
                    return null
                }
            }

            is NodeVariable, is NodeRefMember -> {
                val element = deepLookupReference(type) ?: return null
                if (element !is NodeElement) {
                    validationError("Expected element, got ${element::class.simpleName}", type)
                    return null
                }
                findElementType(element)
            }

            else -> {
                validationError("Unknown element type: $type", type)
                null
            }
        }
    }

    fun validateElement(node: NodeElement, isInVariable: Boolean = false) {
        if (node.type is NodeVariable) {
            val variable = deepLookupReference(node.type) ?: return

            if (variable is NodeVariable) {

                return
            } else {
                if (variable !is NodeElement) {
                    validationError("Expected element, got ${variable::class.simpleName}", variable)
                    return
                }

                val type = findElementType(variable)!!
                node.resolvedType = type

                val definitionCopy = variable.clone()
                definitionCopy.initFile(variable.file)
                definitionCopy.applyParent(variable.parent)

                val internalVariables = (definitionCopy.localVariables + node.localVariables)
                    .associateBy { it.variable.identifier }
                    .values

                val variableScope = node.resolvedScope!!.childScope(variable.resolvedScope!!)
                    .childScope(internalVariables, ::validationError)

                definitionCopy.body.setScope(variableScope)
                node.body.setScope(variableScope)

                definitionCopy.childElements.forEach { validateElement(it) }
                definitionCopy.selectorElements.forEach { validateSelectorElement(node, it) }

                node.childElements.forEach { validateElement(it) }
                node.selectorElements.forEach { validateSelectorElement(node, it) }

                definitionCopy.properties.forEach {
                    validateProperty(
                        it.value,
                        type.properties[it.identifier.identifier]!!
                    )
                }
                node.properties.forEach { validateProperty(it.value, type.properties[it.identifier.identifier]!!) }

                return
            }
        }
        // TODO: If this element is an @Variable {} element, we need to validate both its own properties and the parent's
        // The parent may have undefined variables, we need to add our local variables to the parent's scope for the parent's element validation
        // This is currently not possible since each element can have only one scope
        // We'll see how we do this at some point

        val childScope =
            node.resolvedScope!!.childScope(
                node.localVariables,
                ::validationError,
                allowMissingVariables = isInVariable
            )
        node.body.setScope(childScope)

        if (validateUnusedVariables) {
            node.localVariables.forEach { validateAssignVariable(it) }
        }

        val elementType = findElementType(node)

        node.childElements.forEach { validateElement(it) }
        node.selectorElements.forEach { validateSelectorElement(node, it) }

        if (elementType == null) return

        if (!elementType.allowsChildren() && node.childElements.isNotEmpty())
            validationErrors.add(ValidatorError("Element ${elementType.name} does not allow children", node))

        node.resolvedType = elementType

        val seenProperties = mutableSetOf<String>()
        node.properties.forEach {
            val typeType = elementType.properties[it.identifier.identifier]
                ?: return@forEach validationError("Unknown property ${it.identifier.identifier} on $elementType", it)
            if (!seenProperties.add(it.identifier.identifier))
                return@forEach validationError("Duplicate property ${it.identifier.identifier} on $elementType", it)
            validateProperty(it.value, typeType)
        }
    }


    fun validateSelectorElement(parent: NodeElement, node: NodeSelectorElement) {
        val parentDef = when (parent.type) {
            is NodeIdentifier -> return validationError("Cannot have selector elements inside plain elements", parent)
            is NodeVariable, is NodeRefMember -> deepLookupReference(parent.type) as NodeElement
            else -> return validationError("Unknown parent type: ${parent.type}", parent)
        }

        validateElement(parentDef)

        val source = parentDef.childElements.find { it.selector?.selector?.text == node.selector.selector.text }
            ?: error("No selector element $node on ${parentDef.type}") // THEY CANNOT BE TRANSITIVE!
        validateElement(source)
        val elementType = source.resolvedType

        val childScope = node.resolvedScope!!.childScope(node.localVariables, ::validationError)
        node.body.setScope(childScope)

        if (!elementType.allowsChildren()) assert(node.childElements.isEmpty()) {
            "Element ${elementType.name} does not allow children"
        }

        node.properties.forEach {
            val typeType = elementType.properties[it.identifier.identifier]
                ?: return@forEach validationError("Unknown property ${it.identifier.identifier} on $elementType", node)
            validateProperty(it.value, typeType)
        }

        node.childElements.forEach { validateElement(it) }
    }

    fun validateProperty(node: AstNode, type: TypeType) {
        val value = when (node) {
            is VariableReference -> deepLookupReference(node)
            is VariableValue -> node
            else -> return validationError("Unknown property type: ${node::class.simpleName}", node)
        }

        if (value == null) {
            return
        }

        if (value is NodeVariable) {
//            value.resolvedScope.addMissingTypeVariable(value.identifier, type)
            return
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
                    if (type != TypeType.Color) return validationError(
                        "Expected $type, got ${value::class.simpleName}",
                        value
                    )
                }

                is NodeNegate -> {
                    if (!type.canNegate()) return validationError("Got negation for non-negatable type $type", node)
                    validateProperty(value.param, type)
                }

                is NodeTranslation -> {
                    if (type != TypeType.String) return validationError(
                        "Expected $type, got ${value::class.simpleName}",
                        value
                    )
                }

                else -> return validationError("Expected ${type}, got ${value::class.simpleName}", node)
            }
        } else if (type.isEnum) {
            if (value !is NodeConstant) return validationError(
                "Expected constant, got ${value::class.simpleName}",
                value
            )
            if (value.valueText !in type.enum) return validationError("Invalid enum value: ${value.valueText}", value)
        } else {
            if (type == TypeType.PatchStyle) {
                // Special case for patch styles, they can be replaced by a color or texture path
                if (value is NodeType) return validateType(value, type, node)
                if (value is NodeColor || value is NodeConstant) return
                return validationError(
                    "Expected PatchStyle, color or texture path, got ${value::class.simpleName}",
                    value
                )
            }
            if (type == TypeType.Padding) {
                // Special case for padding, a single number represents Full
                if (value is NodeConstant) return validatePrimitive(TypeType.Padding.allowedFields["Full"]!!, value)
            }

            if (value !is NodeType) return validationError("Expected type, got ${value::class.simpleName}", value)
            validateType(value, type, node)
        }
    }

    fun validateType(node: NodeType, type: TypeType, usagePoint: AstNode) {
        if (node.type != null) {
            val referredType = try {
                TypeType.valueOf(node.type.identifier)
            } catch (_: IllegalArgumentException) {
                return validationError("Unknown type ${node.type.identifier}", node.type)
            }

            if (referredType != type)
                return validationError("Expected type ${type.name}, got ${node.type.identifier}", usagePoint)
        }
        node.resolvedTypes.add(type)
        node.spreads.forEach { spread ->
            val value = deepLookupReference(spread.variableAsReference) ?: return@forEach
            if (value !is NodeType) return@forEach validationError(
                "Expected type, got ${value::class.simpleName}",
                value
            )
            validateType(value, type, usagePoint)
        }
        node.fields.forEach { field ->
            val reqType = type.allowedFields[field.identifier.identifier]
                ?: return@forEach validationError(
                    "Unknown field ${field.identifier.identifier} on $type, allowed fields: ${
                        type.allowedFields.keys.joinToString(
                            ", "
                        )
                    }", field
                )
            validateProperty(field.value, reqType)
        }
    }

    fun lookupRefMember(ref: NodeRefMember): AstNode? {
        val referenceAssignment = ref.file.referenceMap[ref.reference.identifier]
        if (referenceAssignment == null) {
            validationError("No reference ${ref.reference.identifier} found", ref)
            return null
        }

        val reference = referenceAssignment.resolvedFilePath
        val rootNode = validateRoot(reference)
        if (rootNode == null) {
            validationError("Failed to resolve reference $reference", referenceAssignment)
            return null
        }
        ref.member.setScope(rootNode.resolvedScope!!)
        if (ref.member.resolvedValue == null) {
            validationError(
                "No member ${ref.member.identifier} on ${ref.reference.identifier}", ref
            )
            return null
        }
        return ref.member.resolvedValue!!.asAstNode
    }

    fun deepLookupReference(reference: VariableReference): AstNode? {
//        println("Looking up ${(reference as AstNode).text} in $scope")
        val result = when (reference) {
            is NodeRefMember -> lookupRefMember(reference)
            is NodeVariable -> {
                val something = reference.resolvedScope!!.lookupVariable(reference.identifier)
                if (something == null) {
                    if (reference.resolvedScope!!.isAllowMissingVariables()) {
//                        return reference
                        return null
                    }
                    validationError("Variable not found in local scope", reference)
                }
                something
            }

            is NodeMemberField -> {
                val stack = Stack<NodeMemberField>()
                var curr: AstNode = reference
                while (curr is NodeMemberField) {
                    stack.push(curr)
                    curr = curr.owner
                }

                val ownerValue = if (curr is VariableReference) deepLookupReference(curr) else curr
                if (ownerValue == null)
                    return null

                if (ownerValue !is NodeType) {
                    validationError("Expected type, got ${ownerValue::class.simpleName}", ownerValue)
                    return null
                }

                var currentType: AstNode = ownerValue
                while (stack.isNotEmpty()) {
                    if (currentType !is NodeType) {
                        validationError("Tried to look up field on non-type", currentType)
                        return null
                    }
                    val fieldName = stack.pop().member.identifier
                    currentType = lookupTypeField(currentType, fieldName) ?: return null
                }
                currentType
            }
        }
        if (result is VariableReference) return deepLookupReference(result)
        return result
    }

    fun validatePrimitive(type: TypeType, value: NodeConstant) {
        when (type) {
            TypeType.String -> {
                if (!value.isQuoted) validationError("Expected string to be quoted", value)
            } // All constants are strings

            TypeType.Int32, TypeType.Float -> {
                if (value.isQuoted) validationError("$type cannot be quoted", value)
                value.valueText.toFloatOrNull()
                    ?: validationError("Invalid number value: ${value.valueText}", value)
            }

            TypeType.Boolean -> {
                if (value.isQuoted) validationError("$type cannot be quoted", value)
                value.valueText.toBooleanStrictOrNull()
                    ?: validationError("Invalid boolean value: ${value.valueText}", value)
            }

            else -> validationError("Unknown primitive type: $type", value)
        }
    }

    fun lookupTypeField(type: NodeType, fieldName: String): AstNode? {
        val declaredField = type.fields.find { it.identifier.identifier == fieldName }
        if (declaredField != null) return declaredField.value

        val inheritedField = type.spreads.mapNotNull {
            val source = deepLookupReference(it.variableAsReference) ?: return null
            if (source !is NodeType) {
                validationError("Cannot spread non-type, got ${source::class.simpleName}", source)
                return null
            }
            lookupTypeField(source, fieldName)
        }
        return inheritedField.firstOrNull()
    }


    fun validateAssignVariable(node: NodeAssignVariable) {
        if (node.value is NodeElement) { // TODO: Should we have a separate PropertyValue since elements are the only thing that doesn't have a type
            validateElement(node.value, isInVariable = true)
        } else {
            validateUnknownProperty(node.valueAsVariable)
        }
    }

    fun validateUnknownProperty(node: VariableValue) {
        if (node is VariableReference) {
            deepLookupReference(node)
        }

        if (node is NodeType) {
            validateUnknownType(node)
        }
    }

    fun validateUnknownType(node: NodeType) {
        if (node.type != null) {
            val referredType = try {
                TypeType.valueOf(node.type.identifier)
            } catch (_: IllegalArgumentException) {
                return validationError("Unknown type ${node.type.identifier}", node.type)
            }
            return validateType(node, referredType, node)
        }

        val allFields = node.resolveValue()
        val matchingTypes = TypeType.entries.filter { type ->
            allFields.keys.all { it in type.allowedFields }
        }
        if (matchingTypes.isEmpty()) {
            return validationError("No type matches all fields", node)
        }

        if (matchingTypes.size == 1) {
            validateType(node, matchingTypes.first(), node)
            return
        }

        val types = matchingTypes.toMutableSet()
        for ((key, value) in allFields) {
            validateUnknownProperty(value)
            types.removeIf {
                it.allowedFields[key] !in value.resolvedTypes
            }
        }
        node.resolvedTypes.addAll(types)
    }
}
