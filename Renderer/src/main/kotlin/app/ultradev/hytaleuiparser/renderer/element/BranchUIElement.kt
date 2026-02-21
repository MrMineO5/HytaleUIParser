package app.ultradev.hytaleuiparser.renderer.element

import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.generated.elements.ElementProperties
import app.ultradev.hytaleuiparser.generated.types.LayoutMode
import app.ultradev.hytaleuiparser.renderer.RenderContext
import app.ultradev.hytaleuiparser.renderer.layout.Layout
import app.ultradev.hytaleuiparser.renderer.target.RenderTarget

abstract class BranchUIElement(
    node: AstNode,
    val children: List<AbstractUIElement>,
) : AbstractUIElement(node) {
    open val layoutMode: LayoutMode get() = LayoutMode.Full
    val visibleChildren get() = children.filter { it.visible }

    override fun contentDesiredHeight(available: Int): Int =
        Layout.get(layoutMode).contentDesiredHeight(this, available)

    override fun contentDesiredWidth(available: Int): Int = Layout.get(layoutMode).contentDesiredWidth(this, available)

    override fun afterDraw(target: RenderTarget, context: RenderContext) {
        visibleChildren.forEach { it.draw0(target, context) }
    }


    override fun mouseMoved(context: RenderContext) {
        visibleChildren.forEach { it.mouseMoved(context) }
    }

    override fun mouseDown(context: RenderContext): Boolean {
        visibleChildren.asReversed().forEach {
            if (!context.mouseInside(it.box)) return@forEach
            if (it.mouseDown(context)) return true
        }
        return false
    }

    override fun mouseUp(context: RenderContext) {
        visibleChildren.forEach {
            it.mouseUp(context)
        }
    }

    abstract fun withChildren(children: List<AbstractUIElement>): BranchUIElement
}
