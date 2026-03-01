package app.ultradev.hytaleuiparser.renderer.element

import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.generated.types.LayoutMode
import app.ultradev.hytaleuiparser.generated.types.ScrollbarStyle
import app.ultradev.hytaleuiparser.renderer.Axis
import app.ultradev.hytaleuiparser.renderer.BoxSize
import app.ultradev.hytaleuiparser.renderer.RenderBox
import app.ultradev.hytaleuiparser.renderer.RenderContext
import app.ultradev.hytaleuiparser.renderer.extensions.totalSpace
import app.ultradev.hytaleuiparser.renderer.layout.Layout
import app.ultradev.hytaleuiparser.renderer.render.drawScrollBarStyle
import app.ultradev.hytaleuiparser.renderer.target.RenderTarget

abstract class BranchUIElement(
    node: AstNode,
    val children: List<AbstractUIElement>,
) : AbstractUIElement(node) {
    open val layoutMode: LayoutMode get() = LayoutMode.Full
    val visibleChildren get() = children.asSequence().filter { it.visible }
    val visibleChildrenReversed get() = children.asReversed().asSequence().filter { it.visible }

    var scrollOffset: Int = 0

    val childBox: RenderBox get() {
        val contentBox = this.contentBox
        val scrollbarBox = scrollbarSize()
        return RenderBox(
            contentBox.x,
            contentBox.y,
            contentBox.width - scrollbarBox.width,
            contentBox.height - scrollbarBox.height
        )
    }

    fun scrollbarSize(): BoxSize {
        var scrollbarBox = BoxSize.ZERO
        if (layoutMode == LayoutMode.TopScrolling || layoutMode == LayoutMode.BottomScrolling || contentHeight != null) {
            scrollbarBox += BoxSize(
                (properties["ScrollbarStyle"] as? ScrollbarStyle ?: ScrollbarStyle.EMPTY).totalSpace(),
                0
            )
        }
        if (layoutMode == LayoutMode.RightScrolling || layoutMode == LayoutMode.LeftScrolling || contentWidth != null) {
            scrollbarBox += BoxSize(
                0,
                (properties["ScrollbarStyle"] as? ScrollbarStyle ?: ScrollbarStyle.EMPTY).totalSpace()
            )
        }
        return scrollbarBox
    }

    override fun contentDesiredSize(available: BoxSize): BoxSize {
        val scrollbarBox = scrollbarSize()
        return Layout.get(layoutMode).contentDesiredSize(this, available - scrollbarBox) + scrollbarBox
    }

    override fun draw(target: RenderTarget, context: RenderContext) {
        super.draw(target, context)
        val ch = contentHeight
        if (ch != null && ch > childBox.height) {
            drawScrollBarStyle(
                target,
                context,
                contentBox,
                ch,
                childBox.height,
                scrollOffset,
                Axis.VERTICAL,
                properties["ScrollbarStyle"] as? ScrollbarStyle
            )
        }
    }

    override fun afterDraw(target: RenderTarget, context: RenderContext) {
        var oldClip: RenderBox? = null
        var oldOffset: Pair<Int, Int> = 0 to 0
        var targetInfoSet = false
        if (contentHeight != null) {
            oldClip = target.setClip(RenderBox(0, childBox.y, Int.MAX_VALUE, childBox.height))
            oldOffset = target.setOffset(0, -scrollOffset)
            targetInfoSet = true
        }
        visibleChildren.forEach { it.draw0(target, context) }
        if (targetInfoSet) {
            target.setClip(oldClip)
            target.setOffset(oldOffset.first, oldOffset.second)
        }
    }


    override fun mouseMoved(context: RenderContext) {
        visibleChildren.forEach { it.mouseMoved(context) }
    }

    override fun mouseDown(context: RenderContext): Boolean {
        visibleChildrenReversed.forEach {
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

    override fun mouseWheel(delta: Int, context: RenderContext): Boolean {
        visibleChildrenReversed.forEach {
            if (!context.mouseInside(it.box)) return@forEach
            if (it.mouseWheel(delta, context)) return true
        }

        val ch = contentHeight
        if (ch != null) {
            scrollOffset += delta * 10
            if (scrollOffset < 0) scrollOffset = 0
            if (scrollOffset > ch - childBox.height) scrollOffset = ch - childBox.height
        }

        return super.mouseWheel(delta, context)
    }
}
