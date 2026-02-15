package app.ultradev.hytaleuiparser.renderer.element.impl

import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.generated.elements.LabelProperties
import app.ultradev.hytaleuiparser.generated.elements.NumberFieldProperties
import app.ultradev.hytaleuiparser.generated.types.InputFieldStyle
import app.ultradev.hytaleuiparser.generated.types.LabelStyle
import app.ultradev.hytaleuiparser.renderer.RenderContext
import app.ultradev.hytaleuiparser.renderer.element.AbstractUIElement
import app.ultradev.hytaleuiparser.renderer.target.RenderTarget
import app.ultradev.hytaleuiparser.renderer.text.TextRenderStyle
import java.awt.Color
import java.awt.Cursor
import java.awt.font.FontRenderContext

class UINumberFieldElement(
    node: AstNode,
    override val properties: NumberFieldProperties,
) : AbstractUIElement(node) {
    var text = properties.value?.toString() ?: ""

    val textRenderInfo get() = TextRenderStyle.fromInputFieldStyle(properties.style ?: InputFieldStyle.EMPTY)
    val placeholderTextRenderInfo get() = TextRenderStyle.fromInputFieldStyle(properties.placeholderStyle ?: InputFieldStyle.EMPTY)

    override fun contentDesiredHeight(available: Int): Int {
        return textRenderInfo.getBounds(FontRenderContext(null, false, false), text).height.toInt()
    }

    override fun contentDesiredWidth(available: Int): Int {
        return textRenderInfo.getWidth(FontRenderContext(null, false, false), text)
    }

    override fun draw(target: RenderTarget, context: RenderContext) {
        super.draw(target, context)
        if (text.isEmpty()) {
            target.renderText(text, box, textRenderInfo)
        } else {
            target.renderText("0", box, placeholderTextRenderInfo) // TODO: Placeholder value?
        }
    }

    override fun mouseMoved(context: RenderContext) {
        if (context.movedInto(box)) {
            context.setCursor(Cursor(Cursor.TEXT_CURSOR))
        } else if (context.movedOutOf(box)) {
            context.setCursor(Cursor(Cursor.DEFAULT_CURSOR))
        }
    }
}