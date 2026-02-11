package app.ultradev.hytaleuiparser.types

import app.ultradev.hytaleuiparser.ast.NodeType
import app.ultradev.hytaleuiparser.ast.VariableValue
import app.ultradev.hytaleuiparser.asttools.valueAsInteger

data class Padding(
    val left: Int? = null,
    val right: Int? = null,
    val top: Int? = null,
    val bottom: Int? = null,
    val horizontal: Int? = null,
    val vertical: Int? = null,
    val full: Int? = null,
) {
    companion object {
        val NONE = Padding()

        fun fromVariable(variable: VariableValue): Padding {
            val res = variable.deepResolve()
            if (res !is NodeType) error("Expected Type, got $res")
            return fromProperties(res.resolveValue())
        }
        fun fromProperties(properties: Map<String, VariableValue>): Padding { // TODO: Maybe we could use kotlin serialization for this?
            return Padding(
                left = properties["Left"]?.valueAsInteger(),
                right = properties["Right"]?.valueAsInteger(),
                top = properties["Top"]?.valueAsInteger(),
                bottom = properties["Bottom"]?.valueAsInteger(),
                horizontal = properties["Horizontal"]?.valueAsInteger(),
                vertical = properties["Vertical"]?.valueAsInteger(),
                full = properties["Full"]?.valueAsInteger(),
            )
        }
    }
}