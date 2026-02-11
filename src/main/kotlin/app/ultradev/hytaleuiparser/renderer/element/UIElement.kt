package app.ultradev.hytaleuiparser.renderer.element

import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.renderer.RenderBox
import app.ultradev.hytaleuiparser.renderer.target.RenderTarget

open class UIElement(
    val node: AstNode,
    val box: RenderBox,

    val background: UIElementBackground? = null,
) {
    open fun draw(target: RenderTarget) {
        background?.draw(target, box)
    }
}
