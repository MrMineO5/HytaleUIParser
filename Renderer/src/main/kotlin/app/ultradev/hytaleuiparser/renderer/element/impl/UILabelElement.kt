package app.ultradev.hytaleuiparser.renderer.element.impl

import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.generated.elements.ElementProperties
import app.ultradev.hytaleuiparser.generated.elements.LabelProperties
import app.ultradev.hytaleuiparser.generated.types.LabelStyle
import app.ultradev.hytaleuiparser.renderer.BoxSize
import app.ultradev.hytaleuiparser.renderer.RenderContext
import app.ultradev.hytaleuiparser.renderer.element.AbstractUIElement
import app.ultradev.hytaleuiparser.renderer.extensions.maxOfOrZero
import app.ultradev.hytaleuiparser.renderer.target.RenderTarget
import app.ultradev.hytaleuiparser.renderer.text.TextRenderStyle

class UILabelElement(
    node: AstNode,
    override val properties: LabelProperties,
) : AbstractUIElement(node) {
    val text get() = properties.text ?: ""
    val textRenderInfo get() = TextRenderStyle.fromLabelStyle(properties.style ?: LabelStyle.EMPTY)

    override fun contentDesiredSize(available: BoxSize): BoxSize {
        val wrapped = textRenderInfo.wrap(text, available.width)

        val height = textRenderInfo.getHeight() * wrapped.size
        val width = wrapped.maxOfOrZero { textRenderInfo.getWidth(it) }
        return BoxSize(
            width,
            height
        )
    }

    override fun draw(target: RenderTarget, context: RenderContext) {
        super.draw(target, context)
        target.renderText(text, box, textRenderInfo)
    }

    override fun withProperties(properties: ElementProperties) = UILabelElement(node, properties as LabelProperties)
}