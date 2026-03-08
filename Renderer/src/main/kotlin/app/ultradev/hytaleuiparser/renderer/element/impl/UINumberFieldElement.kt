package app.ultradev.hytaleuiparser.renderer.element.impl

import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.generated.elements.ElementProperties
import app.ultradev.hytaleuiparser.generated.elements.NumberFieldProperties
import app.ultradev.hytaleuiparser.generated.types.InputFieldStyle
import app.ultradev.hytaleuiparser.renderer.type.BoxSize
import app.ultradev.hytaleuiparser.renderer.context.RenderContext
import app.ultradev.hytaleuiparser.renderer.element.AbstractUIElement
import app.ultradev.hytaleuiparser.renderer.text.TextRenderStyle
import java.awt.Cursor

class UINumberFieldElement(
    node: AstNode,
    override val properties: NumberFieldProperties,
) : AbstractUIElement(node) {
    var text = properties.value?.toString() ?: ""

    val textRenderInfo get() = TextRenderStyle.fromInputFieldStyle(properties.style ?: InputFieldStyle.EMPTY)
    val placeholderTextRenderInfo get() = TextRenderStyle.fromInputFieldStyle(properties.placeholderStyle ?: InputFieldStyle.EMPTY)

    override fun contentDesiredSize(available: BoxSize): BoxSize {
        return BoxSize(
            textRenderInfo.getHeight().toInt(),
            textRenderInfo.getWidth(text).toInt()
        )
    }

    override fun draw(context: RenderContext) {
        super.draw(context)
        if (text.isNotEmpty()) {
            context.draw.renderText(text, contentBox, textRenderInfo)
        } else {
            context.draw.renderText("0", contentBox, placeholderTextRenderInfo) // TODO: Placeholder value?
        }
    }

    override fun mouseMoved(context: RenderContext) {
        if (context.interactivity.movedInto(box)) {
            context.interactivity.setCursor(Cursor(Cursor.TEXT_CURSOR))
        } else if (context.interactivity.movedOutOf(box)) {
            context.interactivity.setCursor(Cursor(Cursor.DEFAULT_CURSOR))
        }
    }

    override fun withProperties(properties: ElementProperties) = UINumberFieldElement(node, properties as NumberFieldProperties)
}