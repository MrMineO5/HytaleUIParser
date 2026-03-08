package app.ultradev.hytaleuiparser.renderer.element.impl

import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.generated.elements.CheckBoxProperties
import app.ultradev.hytaleuiparser.generated.elements.ElementProperties
import app.ultradev.hytaleuiparser.renderer.context.RenderContext
import app.ultradev.hytaleuiparser.renderer.element.AbstractUIElement
import app.ultradev.hytaleuiparser.renderer.render.drawPatchStyle

class UICheckBoxElement(
    node: AstNode,
    override val properties: CheckBoxProperties,
) : AbstractUIElement(node) {
    var state: Boolean = properties.value ?: false

    override fun draw(context: RenderContext) {
        super.draw(context)
        val style = if (state) properties.style?.checked else properties.style?.unchecked
        drawPatchStyle(context, contentBox, style?.defaultBackground)
    }

    override fun mouseDown(context: RenderContext): Boolean {
        context.interactivity.active = this
        return true
    }

    override fun mouseUp(context: RenderContext) {
        if (context.interactivity.active == this && context.interactivity.mouseInside(box)) state = !state
    }

    override fun withProperties(properties: ElementProperties) = UICheckBoxElement(node, properties as CheckBoxProperties)
}