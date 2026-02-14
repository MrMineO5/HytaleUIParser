package app.ultradev.hytaleuiparser.asttools

import app.ultradev.hytaleuiparser.ast.NodeColor
import app.ultradev.hytaleuiparser.ast.NodeConstant
import app.ultradev.hytaleuiparser.ast.NodeMathOperation
import app.ultradev.hytaleuiparser.ast.NodeNegate
import app.ultradev.hytaleuiparser.ast.NodeTranslation
import app.ultradev.hytaleuiparser.ast.NodeType
import app.ultradev.hytaleuiparser.ast.VariableValue
import app.ultradev.hytaleuiparser.validation.resolveNeighbour
import java.awt.Color

/*
 * PRIMITIVES
 */
private fun VariableValue.valueAsNumber(): Double {
    val finalValue = this.deepResolve()
    when (finalValue) {
        is NodeConstant -> return finalValue.valueText.toDouble()
        is NodeNegate -> return -finalValue.paramAsVariableValue.valueAsNumber()
        is NodeMathOperation -> {
            val p1 = finalValue.param1AsVariable.valueAsNumber()
            val p2 = finalValue.param2AsVariable.valueAsNumber()
            return when (finalValue.operator!!.text) {
                "+" -> p1 + p2
                "-" -> p1 - p2
                "*" -> p1 * p2
                "/" -> p1 / p2
                else -> error("Could not interpret math operation ${finalValue.operator!!.text}")
            }
        }
        else -> error("Could not interpret $finalValue as an integer.")
    }
}
fun VariableValue.valueAsInt32(): Int = valueAsNumber().toInt()
fun VariableValue.valueAsFloat(): Float = valueAsNumber().toFloat()
fun VariableValue.valueAsString(): String {
    val finalValue = this.deepResolve()
    when (finalValue) {
        is NodeConstant -> return finalValue.valueText
        is NodeTranslation -> return finalValue.value?.text ?: "null" // TODO: Get translation from lang file?
        else -> error("Could not interpret $finalValue as a string.")
    }
}
fun VariableValue.valueAsBoolean(): Boolean {
    val finalValue = this.deepResolve()
    when (finalValue) {
        is NodeConstant -> return finalValue.valueText.toBoolean()
        else -> error("Could not interpret $finalValue as a boolean.")
    }
}
fun VariableValue.valueAsColor(): Color {
    val finalValue = this.deepResolve()
    when (finalValue) {
        is NodeColor -> {
            val hexPart = finalValue.value!!.valueText.removePrefix("#")
            val color = Color(Integer.parseInt(hexPart, 16))
            if (finalValue.opacity != null) {
                val opacity = finalValue.opacity!!.value!!.valueAsFloat()
                return Color(color.red, color.green, color.blue, (opacity * 255).toInt())
            }
            return color
        }
        else -> error("Could not interpret $finalValue as a color.")
    }
}


fun VariableValue.valueAsPath(): String {
    return this.asAstNode.file.path.resolveNeighbour(this.valueAsString())
}


/*
 * TYPES
 */
fun VariableValue.valueAsEnum(): String {
    val finalValue = this.deepResolve()
    when (finalValue) {
        is NodeConstant -> return finalValue.valueText
        else -> error("Could not interpret $finalValue as an enum.")
    }
}
fun VariableValue.valueAsProperties(): Map<String, VariableValue> {
    val finalValue = this.deepResolve()
    when (finalValue) {
        is NodeType -> return finalValue.resolveValue()
        else -> error("Could not interpret $finalValue as a properties object.")
    }
}
