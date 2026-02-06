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

    private fun validationError(message: String, astNode: AstNode) {
        validationErrors.add(ValidatorError(message, astNode))
    }

    private fun reemitErrors(message: String, newNode: AstNode, block: () -> Unit) {
        val oldIndex = validationErrors.size
        block()
        val diff = validationErrors.subList(oldIndex, validationErrors.size)
        diff.forEach {
            validationErrors.add(ValidatorError(message, newNode, it))
        }
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

            root.references.forEach {
                validateReference(it)
            }
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


    fun validateVariableElement(node: NodeElement) {
        node.type as VariableReference

        val variable = deepLookupReference(node.type) ?: return

        if (variable is VariableReference) {
            // TODO: We may want to reimplement "missing" variable scopes to allow for better completion?
        } else {
            if (variable !is NodeElement) {
                validationError("Expected element, got ${variable::class.simpleName}", variable)
                return
            }

            val type = findElementType(variable)!!
            node.resolvedType = type
            variable.resolvedType = type

            val variableCopy = variable.clone()
            variableCopy.initFile(variable.file)
            variableCopy.applyParent(variable.parent)

            val internalVariables = (variableCopy.localVariables + node.localVariables)
                .associateBy { it.variable.identifier }
                .values

            val variableScope = node.resolvedScope!!.childScope(variable.resolvedScope!!)
                .childScope(internalVariables, ::validationError)

            variableCopy.body.setScope(variableScope)
            node.body.setScope(variableScope)

            variableCopy.selectorElements.forEach {
                validationError(
                    "Selector elements must be used within a variable element",
                    it
                )
            }

            val seenSelectors = mutableSetOf<String>()
            node.selectorElements.forEach { sel ->
                if (!seenSelectors.add(sel.selector.identifier))
                    return@forEach validationError("Duplicate selector ${sel.selector.identifier} in definition", sel)

                val definitionSel =
                    variableCopy.childElements.firstOrNull { it.selector?.identifier == sel.selector.identifier }
                        ?: return@forEach validationError("Selector element ${sel.selector.identifier} not found in ${node.type.text}", sel)

                val selInternalVariables = (definitionSel.localVariables + sel.localVariables)
                    .associateBy { it.variable.identifier }
                    .values
                val selScope = variableScope.childScope(selInternalVariables, ::validationError)
                sel.body.setScope(selScope)
                definitionSel.body.setScope(selScope)

                sel.childElements.forEach { validateElement(it) }
                definitionSel.childElements.forEach { validateElement(it) }
            }

            reemitErrors("Could not validate variable element", node) {
                variableCopy.childElements.forEach { validateElement(it) }
            }

            node.childElements.forEach { validateElement(it) }

            val seenProperties = mutableSetOf<String>()
            node.properties.forEach {
                if (!seenProperties.add(it.identifier.identifier))
                    return@forEach validationError("Duplicate property ${it.identifier.identifier} on $type", it)
                validateProperty(it.value, type.properties[it.identifier.identifier]!!)
            }
            val varSeenProperties = mutableSetOf<String>()
            reemitErrors("Could not validate variable element", node) {
                variableCopy.properties.filter { it.identifier.identifier !in seenProperties }.forEach {
                    if (!varSeenProperties.add(it.identifier.identifier))
                        return@forEach validationError("Duplicate property ${it.identifier.identifier} on $type", it)
                    validateProperty(
                        it.value,
                        type.properties[it.identifier.identifier]!!
                    )
                }
            }
        }
    }

    fun validateElement(node: NodeElement, isInVariable: Boolean = false) {
        if (node.type is VariableReference) {
            return validateVariableElement(node)
        }

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
        node.selectorElements.forEach {
            validationError(
                "Selector elements must be used within a variable element",
                it
            )
        }

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
                return validationError("Unknown type ${node.type.identifier}, expected $type", node.type)
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
                    validationError("Variable ${reference.identifier} not found in local scope", reference)
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
        } else if (node is NodeConstant) {
            if (node.resolvedTypes.isEmpty()) {
                validationError("No matching type for value", node)
            }
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

        val fieldNames = node.fields.map { it.identifier.identifier }
        val matchingTypes = TypeType.entries.filter { type ->
            type.isStruct && fieldNames.all { it in type.allowedFields }
        }
        if (matchingTypes.isEmpty()) {
            return validationError("No type can contain all present fields", node)
        }

        if (matchingTypes.size == 1) {
            validateType(node, matchingTypes.first(), node)
            return
        }

        val types = matchingTypes.toMutableSet()
        for (field in node.fields) {
            val value = field.valueAsVariableValue
            validateUnknownProperty(value)
            types.removeIf {
                it.allowedFields[field.identifier.identifier] !in value.resolvedTypes!!
            }
        }
        for (spread in node.spreads) {
            val resolved = deepLookupReference(spread.variableAsReference)
            if (resolved == null) {
                validationError("Could not resolve spread variable", spread.variable)
                continue
            }
            if (resolved !is NodeType) {
                validationError("Expected reference to type, got ${resolved::class.simpleName}", spread)
                continue
            }
            validateUnknownType(resolved)
            types.removeIf { it !in resolved.resolvedTypes }
        }

        node.resolvedTypes.addAll(types)
    }


    fun validateReference(node: NodeAssignReference) {
        if (node.resolvedFilePath !in files)
            validationError("Import not found: ${node.resolvedFilePath}", node)
    }
}
