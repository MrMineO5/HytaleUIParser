package app.ultradev.hytaleuiparser.renderer.element

import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.generated.types.LayoutMode
import app.ultradev.hytaleuiparser.renderer.layout.Layout
import app.ultradev.hytaleuiparser.renderer.target.RenderTarget
import java.awt.Point

abstract class BranchUIElement(
    node: AstNode,
    val children: List<AbstractUIElement>,
) : AbstractUIElement(node) {
    open val layoutMode: LayoutMode get() = LayoutMode.Full
    val visibleChildren get() = children.filter { it.visible }

    override fun contentDesiredHeight(available: Int): Int = Layout.get(layoutMode).contentDesiredHeight(this, available)
    override fun contentDesiredWidth(available: Int): Int = Layout.get(layoutMode).contentDesiredWidth(this, available)

    override fun afterDraw(target: RenderTarget, mousePosition: Point) {
        visibleChildren.forEach { it.draw0(target, mousePosition) }
    }
}
