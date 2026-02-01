package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.validation.Scope
import app.ultradev.hytaleuiparser.validation.types.TypeType
import app.ultradev.hytaleuiparser.validation.types.unifyStruct

data class NodeField(
    val identifier: NodeIdentifier,
    val fieldMarker: NodeToken,
    val value: AstNode,
    val endStatement: NodeToken? = null
) : AstNode() {
    override fun validate(validationError: (String, AstNode) -> Unit) {
        if (value !is VariableValue) validationError("Expected variable value after assignment operator", value)
        if (parent !is NodeBody) validationError("Field must be contained in a NodeBody", this)
    }

    override val children: List<AstNode>
        get() = listOf(identifier, fieldMarker, value) + listOfNotNull(endStatement)

    override fun setScope(scope: Scope) {
        super.setScope(scope)
        value.setScope(scope)

        identifier.setScope(scope)
        fieldMarker.setScope(scope)
        endStatement?.setScope(scope)
    }

    val valueAsVariableValue: VariableValue = value as VariableValue

    val resolvedParentFields: Map<String, TypeType>
        get() {
            return when (val parent = parent.parent) {
                is NodeType -> parent.resolvedTypes.unifyStruct()
                is NodeElement -> parent.resolvedType.properties
                else -> error("Field contained in body of parent type ${parent.javaClass.simpleName}")
            }
        }

    val resolvedType: TypeType
        get() {
            return resolvedParentFields[identifier.identifier] ?: error("Field not found in parent type")
        }

    override fun computePath(): String = super.computePath() + "." + identifier.identifier
}
