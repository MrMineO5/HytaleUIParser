package app.ultradev.hytaleuiparser.types

import app.ultradev.hytaleuiparser.ast.NodeColor
import app.ultradev.hytaleuiparser.ast.NodeConstant
import app.ultradev.hytaleuiparser.ast.NodeType
import app.ultradev.hytaleuiparser.ast.VariableValue
import app.ultradev.hytaleuiparser.asttools.valueAsColor
import app.ultradev.hytaleuiparser.asttools.valueAsInteger
import app.ultradev.hytaleuiparser.asttools.valueAsString
import app.ultradev.hytaleuiparser.validation.resolveNeighbour
import java.awt.Color

data class PatchStyle(
//    val area: Padding? = null, // TODO
    val color: Color? = null,
    val anchor: Anchor? = null,
    val horizontalBorder: Int? = null,
    val verticalBorder: Int? = null,
    val texturePath: String? = null,
    val border: Int? = null,
) {
    companion object {
        val NONE = PatchStyle()

        fun fromVariable(variable: VariableValue): PatchStyle {
            val res = variable.deepResolve()
            return when (res) {
                is NodeConstant -> PatchStyle(texturePath = res.file.path.resolveNeighbour(res.valueAsString()))
                is NodeColor -> PatchStyle(color = res.valueAsColor())
                is NodeType -> fromProperties(res.resolveValue())

                else -> error("Cannot assign ${res?.javaClass?.simpleName} to PatchStyle. $res")
            }
        }

        fun fromProperties(properties: Map<String, VariableValue>): PatchStyle { // TODO: Maybe we could use kotlin serialization for this?
            return PatchStyle(
                color = properties["Color"]?.valueAsColor(),
                anchor = properties["Anchor"]?.let(Anchor::fromVariable),
                horizontalBorder = properties["HorizontalBorder"]?.valueAsInteger(),
                verticalBorder = properties["VerticalBorder"]?.valueAsInteger(),
                texturePath = properties["TexturePath"]?.let { it.asAstNode.file.path.resolveNeighbour(it.valueAsString()) },
                border = properties["Border"]?.valueAsInteger(),
            )
        }
    }
}