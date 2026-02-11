package app.ultradev.hytaleuiparser.asttools

import app.ultradev.hytaleuiparser.ast.NodeColor
import app.ultradev.hytaleuiparser.ast.NodeConstant
import app.ultradev.hytaleuiparser.ast.NodeNegate
import app.ultradev.hytaleuiparser.ast.NodeTranslation
import app.ultradev.hytaleuiparser.ast.VariableValue
import java.awt.Color

fun VariableValue.valueAsInteger(): Int {
    val finalValue = this.deepResolve()
    when (finalValue) {
        is NodeConstant -> return finalValue.valueText.toInt()
        is NodeNegate -> return -finalValue.paramAsVariableValue.valueAsInteger()
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