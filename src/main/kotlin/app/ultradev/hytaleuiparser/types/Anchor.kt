package app.ultradev.hytaleuiparser.types

import app.ultradev.hytaleuiparser.ast.NodeType
import app.ultradev.hytaleuiparser.ast.VariableValue
import app.ultradev.hytaleuiparser.asttools.valueAsInteger

data class Anchor(
    val left: Int? = null,
    val right: Int? = null,
    val top: Int? = null,
    val bottom: Int? = null,
    val height: Int? = null,
    val width: Int? = null,
    val full: Int? = null,
    val horizontal: Int? = null,
    val vertical: Int? = null,
    val minWidth: Int? = null,
    val maxWidth: Int? = null,
) {
    companion object {
        val NONE = Anchor()

        fun fromVariable(variable: VariableValue): Anchor {
            val res = variable.deepResolve()
            if (res !is NodeType) error("Expected Type, got $res")
            return fromProperties(res.resolveValue())
        }
        fun fromProperties(properties: Map<String, VariableValue>): Anchor { // TODO: Maybe we could use kotlin serialization for this?
            return Anchor(
                left = properties["Left"]?.valueAsInteger(),
                right = properties["Right"]?.valueAsInteger(),
                top = properties["Top"]?.valueAsInteger(),
                bottom = properties["Bottom"]?.valueAsInteger(),
                height = properties["Height"]?.valueAsInteger(),
                width = properties["Width"]?.valueAsInteger(),
                full = properties["Full"]?.valueAsInteger(),
                horizontal = properties["Horizontal"]?.valueAsInteger(),
                vertical = properties["Vertical"]?.valueAsInteger(),
                minWidth = properties["MinWidth"]?.valueAsInteger(),
                maxWidth = properties["MaxWidth"]?.valueAsInteger(),
            )
        }
    }
}