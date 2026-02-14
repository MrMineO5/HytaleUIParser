package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.clone
import app.ultradev.hytaleuiparser.validation.types.TypeType
import app.ultradev.hytaleuiparser.validation.types.unifyStruct

class NodeField(
    children: List<AstNode>,
    valid: Boolean = true
) : AstNode(children, valid) {
    val identifier by child<NodeIdentifier>(0)
    val fieldMarker by child<NodeToken>(1)
    val value by child<AstNode>(2)
    val endStatement by optionalChild<NodeToken>(3)

    override fun validate(validationError: (String, AstNode) -> Unit) {
        if (value !is VariableValue) validationError("Expected variable value after assignment operator", findClosestChild(2))
        if (parent !is NodeBody) validationError("Field must be contained in a NodeBody", this)
    }

    val valueAsVariableValue: VariableValue get() = value as VariableValue

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
            val id = identifier?.identifier ?: error("Field has no identifier")
            return resolvedParentFields[id] ?: error("Field not found in parent type")
        }

    override fun computePath(): String = super.computePath() + "." + identifier?.identifier

    override fun clone() = NodeField(children.clone(), valid)
}
