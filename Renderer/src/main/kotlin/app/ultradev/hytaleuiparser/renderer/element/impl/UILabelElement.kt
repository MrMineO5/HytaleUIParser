package app.ultradev.hytaleuiparser.renderer.element.impl

import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.generated.elements.LabelProperties
import app.ultradev.hytaleuiparser.generated.types.LabelStyle
import app.ultradev.hytaleuiparser.renderer.RenderContext
import app.ultradev.hytaleuiparser.renderer.element.AbstractUIElement
import app.ultradev.hytaleuiparser.renderer.target.RenderTarget
import app.ultradev.hytaleuiparser.renderer.text.TextRenderStyle
import java.awt.Color
import java.awt.font.FontRenderContext

class UILabelElement(
    node: AstNode,
    override val properties: LabelProperties,
) : AbstractUIElement(node) {
    val text get() = properties.text ?: ""
    val textRenderInfo get() = TextRenderStyle.fromLabelStyle(properties.style ?: LabelStyle.EMPTY)

    override fun contentDesiredHeight(available: Int): Int {
        return textRenderInfo.getHeight(FontRenderContext(null, false, false), text)
    }

    override fun contentDesiredWidth(available: Int): Int {
        return textRenderInfo.getWidth(FontRenderContext(null, false, false), text)
    }

    override fun draw(target: RenderTarget, context: RenderContext) {
        super.draw(target, context)
        target.renderText(text, box, textRenderInfo)
    }
}