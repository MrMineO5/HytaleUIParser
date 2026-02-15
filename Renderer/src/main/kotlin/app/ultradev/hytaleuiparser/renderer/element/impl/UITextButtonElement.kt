package app.ultradev.hytaleuiparser.renderer.element.impl

import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.generated.elements.LabelProperties
import app.ultradev.hytaleuiparser.generated.elements.TextButtonProperties
import app.ultradev.hytaleuiparser.generated.types.LabelStyle
import app.ultradev.hytaleuiparser.generated.types.PatchStyle
import app.ultradev.hytaleuiparser.renderer.element.AbstractUIElement
import app.ultradev.hytaleuiparser.renderer.element.UIElementBackground
import app.ultradev.hytaleuiparser.renderer.target.RenderTarget
import app.ultradev.hytaleuiparser.renderer.text.TextRenderStyle
import java.awt.Color
import java.awt.Point
import java.awt.font.FontRenderContext

class UITextButtonElement(
    node: AstNode,
    override val properties: TextButtonProperties,
) : AbstractUIElement(node) {
    val text get() = properties.text ?: ""
    val textRenderInfo by lazy { TextRenderStyle.fromLabelStyle(properties.style?.default?.labelStyle ?: LabelStyle.EMPTY) }
    val buttonBackground by lazy { UIElementBackground.fromPatchStyle(properties.style?.default?.background ?: PatchStyle.EMPTY) }
    val hoveredBackground by lazy { UIElementBackground.fromPatchStyle(properties.style?.hovered?.background ?: PatchStyle.EMPTY) }

    override fun draw(target: RenderTarget, mousePosition: Point) {
        super.draw(target, mousePosition)
        if (box.contains(mousePosition.x, mousePosition.y)) {
            hoveredBackground.draw(target, box)
        } else {
            buttonBackground.draw(target, box)
        }
        target.renderText(text, box, Color.WHITE, textRenderInfo)
    }
}