package app.ultradev.hytaleuiparser.renderer.element.impl

import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.generated.elements.ButtonProperties
import app.ultradev.hytaleuiparser.generated.types.LayoutMode
import app.ultradev.hytaleuiparser.renderer.RenderContext
import app.ultradev.hytaleuiparser.renderer.element.AbstractUIElement
import app.ultradev.hytaleuiparser.renderer.element.BranchUIElement
import app.ultradev.hytaleuiparser.renderer.render.drawPatchStyle
import app.ultradev.hytaleuiparser.renderer.target.RenderTarget

class UIButtonElement(
    node: AstNode,
    children: List<AbstractUIElement>,
    override val properties: ButtonProperties,
) : BranchUIElement(node, children) {
    override val layoutMode: LayoutMode
        get() = properties.layoutMode ?: super.layoutMode

    override fun draw(target: RenderTarget, context: RenderContext) {
        super.draw(target, context)

        val style = if (context.mouseInside(box)) {
            properties.style?.hovered
        } else null
            ?: properties.style?.default

        drawPatchStyle(target, context, box, style?.background)
    }
}
