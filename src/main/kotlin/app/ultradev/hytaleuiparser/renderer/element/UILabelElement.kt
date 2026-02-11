package app.ultradev.hytaleuiparser.renderer.element

import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.renderer.RenderBox
import app.ultradev.hytaleuiparser.renderer.target.RenderTarget
import java.awt.Color

class UILabelElement(
    node: AstNode,
    box: RenderBox,
    background: UIElementBackground,

    val text: String,
) : UIElement(node, box, background) {
    override fun draw(target: RenderTarget) {
        super.draw(target)

        target.renderText(text, box.x, box.y, Color.WHITE)
    }
}