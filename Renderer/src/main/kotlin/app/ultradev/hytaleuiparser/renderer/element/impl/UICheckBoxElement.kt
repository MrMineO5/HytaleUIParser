package app.ultradev.hytaleuiparser.renderer.element.impl

import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.generated.elements.ButtonProperties
import app.ultradev.hytaleuiparser.generated.elements.CheckBoxProperties
import app.ultradev.hytaleuiparser.generated.elements.ElementProperties
import app.ultradev.hytaleuiparser.generated.elements.LabelProperties
import app.ultradev.hytaleuiparser.generated.elements.NumberFieldProperties
import app.ultradev.hytaleuiparser.generated.types.InputFieldStyle
import app.ultradev.hytaleuiparser.generated.types.LabelStyle
import app.ultradev.hytaleuiparser.renderer.RenderContext
import app.ultradev.hytaleuiparser.renderer.element.AbstractUIElement
import app.ultradev.hytaleuiparser.renderer.render.drawPatchStyle
import app.ultradev.hytaleuiparser.renderer.target.RenderTarget
import app.ultradev.hytaleuiparser.renderer.text.TextRenderStyle
import java.awt.Color
import java.awt.Cursor
import java.awt.font.FontRenderContext

class UICheckBoxElement(
    node: AstNode,
    override val properties: CheckBoxProperties,
) : AbstractUIElement(node) {
    var state: Boolean = properties.value ?: false

    override fun draw(target: RenderTarget, context: RenderContext) {
        super.draw(target, context)
        val style = if (state) properties.style?.checked else properties.style?.unchecked
        drawPatchStyle(target, context, contentBox, style?.defaultBackground)
    }

    override fun mouseDown(context: RenderContext): Boolean {
        context.active = this
        return true
    }

    override fun mouseUp(context: RenderContext) {
        if (context.active == this && context.mouseInside(box)) state = !state
    }

    override fun withProperties(properties: ElementProperties) = UICheckBoxElement(node, properties as CheckBoxProperties)
}