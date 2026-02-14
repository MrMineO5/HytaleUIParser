package app.ultradev.hytaleuiparser.renderer.element

import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.generated.types.LayoutMode
import app.ultradev.hytaleuiparser.renderer.target.RenderTarget

abstract class BranchUIElement(
    node: AstNode,
    val children: List<AbstractUIElement>,
) : AbstractUIElement(node) {
    open val layoutMode: LayoutMode get() = LayoutMode.Full

    override fun afterDraw(target: RenderTarget) {
        children.forEach { it.draw0(target) }
    }
}
