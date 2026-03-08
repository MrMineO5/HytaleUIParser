package app.ultradev.hytaleuiparser.renderer.element.impl

import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.generated.elements.ButtonProperties
import app.ultradev.hytaleuiparser.generated.elements.ElementProperties
import app.ultradev.hytaleuiparser.generated.types.LayoutMode
import app.ultradev.hytaleuiparser.renderer.context.RenderContext
import app.ultradev.hytaleuiparser.renderer.element.AbstractUIElement
import app.ultradev.hytaleuiparser.renderer.element.BranchUIElement
import app.ultradev.hytaleuiparser.renderer.render.drawPatchStyle

class UIButtonElement(
    node: AstNode,
    children: List<AbstractUIElement>,
    override val properties: ButtonProperties,
) : BranchUIElement(node, children) {
    override val layoutMode: LayoutMode
        get() = properties.layoutMode ?: super.layoutMode

    override fun draw(context: RenderContext) {
        super.draw(context)

        val style = (if (context.interactivity.active == this) {
            properties.style?.pressed
        } else if (context.interactivity.mouseInside(box)) {
            properties.style?.hovered
        } else null) ?: properties.style?.default

        drawPatchStyle(context, box, style?.background)
    }

    override fun mouseDown(context: RenderContext): Boolean {
        context.interactivity.active = this
        return true
    }

    override fun mouseUp(context: RenderContext) {
        if (context.interactivity.active == this) context.interactivity.active = null
    }

    override fun withChildren(children: List<AbstractUIElement>) = UIButtonElement(node, children, properties)
    override fun withProperties(properties: ElementProperties) = UIButtonElement(node, children, properties as ButtonProperties)
}
