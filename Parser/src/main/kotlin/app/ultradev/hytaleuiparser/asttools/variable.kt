package app.ultradev.hytaleuiparser.asttools

import app.ultradev.hytaleuiparser.ast.NodeColor
import app.ultradev.hytaleuiparser.ast.NodeConstant
import app.ultradev.hytaleuiparser.ast.NodeNegate
import app.ultradev.hytaleuiparser.ast.NodeTranslation
import app.ultradev.hytaleuiparser.ast.NodeType
import app.ultradev.hytaleuiparser.ast.VariableValue
import app.ultradev.hytaleuiparser.validation.resolveNeighbour
import java.awt.Color

/*
 * PRIMITIVES
 */
fun VariableValue.valueAsInt32(): Int {
    val finalValue = this.deepResolve()
    when (finalValue) {
        is NodeConstant -> return finalValue.valueText.toInt()
        is NodeNegate -> return -finalValue.paramAsVariableValue.valueAsInt32()
        else -> error("Could not interpret $finalValue as an integer.")
    }
}
fun VariableValue.valueAsFloat(): Float {
    val finalValue = this.deepResolve()
    when (finalValue) {
        is NodeConstant -> return finalValue.valueText.toFloat()
        else -> error("Could not interpret $finalValue as a float.")
    }
}
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
