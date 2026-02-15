package app.ultradev.hytaleuiparser.renderer.element.impl

import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.generated.elements.ButtonProperties
import app.ultradev.hytaleuiparser.generated.types.LayoutMode
import app.ultradev.hytaleuiparser.generated.types.PatchStyle
import app.ultradev.hytaleuiparser.renderer.element.AbstractUIElement
import app.ultradev.hytaleuiparser.renderer.element.BranchUIElement
import app.ultradev.hytaleuiparser.renderer.element.UIElementBackground
import app.ultradev.hytaleuiparser.renderer.target.RenderTarget
import java.awt.Point

class UIButtonElement(
    node: AstNode,
    children: List<AbstractUIElement>,
    override val properties: ButtonProperties,
) : BranchUIElement(node, children) {
    override val layoutMode: LayoutMode
        get() = properties.layoutMode ?: super.layoutMode
    val buttonBackground by lazy {
        UIElementBackground.fromPatchStyle(
            properties.style?.default?.background ?: PatchStyle.EMPTY
        )
    }
    val hoveredBackground by lazy {
        UIElementBackground.fromPatchStyle(
            properties.style?.hovered?.background ?: PatchStyle.EMPTY
        )
    }

    override fun draw(target: RenderTarget, mousePosition: Point) {
        super.draw(target, mousePosition)
        if (box.contains(mousePosition.x, mousePosition.y)) {
            hoveredBackground.draw(target, box)
        } else {
            buttonBackground.draw(target, box)
        }
    }
}
