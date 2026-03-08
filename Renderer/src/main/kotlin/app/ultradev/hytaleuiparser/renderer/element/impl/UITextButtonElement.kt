package app.ultradev.hytaleuiparser.renderer.element.impl

import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.generated.elements.ElementProperties
import app.ultradev.hytaleuiparser.generated.elements.TextButtonProperties
import app.ultradev.hytaleuiparser.generated.types.LabelStyle
import app.ultradev.hytaleuiparser.renderer.type.BoxSize
import app.ultradev.hytaleuiparser.renderer.context.RenderContext
import app.ultradev.hytaleuiparser.renderer.element.AbstractUIElement
import app.ultradev.hytaleuiparser.renderer.render.drawPatchStyle
import app.ultradev.hytaleuiparser.renderer.text.TextRenderStyle

class UITextButtonElement(
    node: AstNode,
    override val properties: TextButtonProperties,
) : AbstractUIElement(node) {
    val text get() = properties.text ?: ""
    val textRenderInfo
        get() = TextRenderStyle.fromLabelStyle(
            properties.style?.default?.labelStyle ?: LabelStyle.EMPTY
        )

    override fun contentDesiredSize(available: BoxSize): BoxSize {
        return BoxSize(
            textRenderInfo.getHeight().toInt(),
            textRenderInfo.getWidth(text).toInt()
        )
    }

    override fun draw(context: RenderContext) {
        super.draw(context)

        val style = if (context.interactivity.active == this) {
            properties.style?.pressed
        } else if (context.interactivity.mouseInside(box)) {
            properties.style?.hovered
        } else {
            null
        } ?: properties.style?.default

        drawPatchStyle(context, box, style?.background)
        context.draw.renderText(text, box, textRenderInfo)
    }


    override fun mouseDown(context: RenderContext): Boolean {
        context.interactivity.active = this
        return true
    }

    override fun mouseUp(context: RenderContext) {
        if (context.interactivity.active == this) context.interactivity.active = null
    }

    override fun withProperties(properties: ElementProperties) = UITextButtonElement(node, properties as TextButtonProperties)
}