package app.ultradev.hytaleuiparser.renderer.element.impl

import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.generated.elements.ElementProperties
import app.ultradev.hytaleuiparser.generated.elements.NumberFieldProperties
import app.ultradev.hytaleuiparser.generated.elements.TextButtonProperties
import app.ultradev.hytaleuiparser.generated.types.LabelStyle
import app.ultradev.hytaleuiparser.renderer.RenderContext
import app.ultradev.hytaleuiparser.renderer.element.AbstractUIElement
import app.ultradev.hytaleuiparser.renderer.render.drawPatchStyle
import app.ultradev.hytaleuiparser.renderer.target.RenderTarget
import app.ultradev.hytaleuiparser.renderer.text.TextRenderStyle
import java.awt.font.FontRenderContext

class UITextButtonElement(
    node: AstNode,
    override val properties: TextButtonProperties,
) : AbstractUIElement(node) {
    val text get() = properties.text ?: ""
    val textRenderInfo
        get() = TextRenderStyle.fromLabelStyle(
            properties.style?.default?.labelStyle ?: LabelStyle.EMPTY
        )

    override fun contentDesiredHeight(available: Int): Int {
        return textRenderInfo.getHeight(FontRenderContext(null, false, false), text)
    }

    override fun contentDesiredWidth(available: Int): Int {
        return textRenderInfo.getWidth(FontRenderContext(null, false, false), text)
    }

    override fun draw(target: RenderTarget, context: RenderContext) {
        super.draw(target, context)

        val style = if (context.active == this) {
            properties.style?.pressed
        } else if (context.mouseInside(box)) {
            properties.style?.hovered
        } else {
            null
        } ?: properties.style?.default

        drawPatchStyle(target, context, box, style?.background)
        target.renderText(text, box, textRenderInfo)
    }


    override fun mouseDown(context: RenderContext): Boolean {
        context.active = this
        return true
    }

    override fun mouseUp(context: RenderContext) {
        if (context.active == this) context.active = null
    }

    override fun withProperties(properties: ElementProperties) = UITextButtonElement(node, properties as TextButtonProperties)
}