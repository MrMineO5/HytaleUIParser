package app.ultradev.hytaleuiparser.renderer.element.impl

import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.generated.elements.LabelProperties
import app.ultradev.hytaleuiparser.generated.types.LabelStyle
import app.ultradev.hytaleuiparser.renderer.element.AbstractUIElement
import app.ultradev.hytaleuiparser.renderer.target.RenderTarget
import app.ultradev.hytaleuiparser.renderer.text.TextRenderInfo
import java.awt.Color
import java.awt.font.FontRenderContext

class UILabelElement(
    node: AstNode,
    override val properties: LabelProperties,
) : AbstractUIElement(node) {
    val text get() = properties.text ?: ""
    val textRenderInfo by lazy { TextRenderInfo.Companion.fromLabelStyle(properties.style ?: LabelStyle.Companion.EMPTY) }

    override fun desiredWidth(): Int? {
        return super.desiredWidth()
            // TODO: Use FontRendererContext from graphics that will draw this element?
            ?: textRenderInfo.getWidth(FontRenderContext(null, false, false), text)
    }

    override fun draw(target: RenderTarget) {
        super.draw(target)
        target.renderText(text, box.x, box.y, Color.WHITE, textRenderInfo)
    }
}