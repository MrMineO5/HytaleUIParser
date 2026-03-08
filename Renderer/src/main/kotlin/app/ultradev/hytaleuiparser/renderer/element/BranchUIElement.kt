package app.ultradev.hytaleuiparser.renderer.element

import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.generated.types.LayoutMode
import app.ultradev.hytaleuiparser.generated.types.ScrollbarStyle
import app.ultradev.hytaleuiparser.renderer.type.Axis
import app.ultradev.hytaleuiparser.renderer.type.BoxSize
import app.ultradev.hytaleuiparser.renderer.type.RenderBox
import app.ultradev.hytaleuiparser.renderer.context.RenderContext
import app.ultradev.hytaleuiparser.renderer.extensions.totalSpace
import app.ultradev.hytaleuiparser.renderer.layout.Layout
import app.ultradev.hytaleuiparser.renderer.render.drawScrollBarStyle

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

    override fun draw(context: RenderContext) {
        super.draw(context)
        val ch = contentHeight
        if (ch != null && ch > childBox.height) {
            drawScrollBarStyle(
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

    private fun <T> withScrollModification(context: RenderContext, applyClip: Boolean, action: () -> T): T {
        var didApplyOffset = false
        if (contentHeight != null) {
            if (applyClip) context.draw.pushClip(RenderBox(0, childBox.y, Int.MAX_VALUE, childBox.height))
            context.draw.pushOffset(0, -scrollOffset)
            didApplyOffset = true
        }
        val result = action()
        if (didApplyOffset) {
            context.draw.popOffset()
            if (applyClip) context.draw.popClip()
        }
        return result
    }

    override fun afterDraw(context: RenderContext) = withScrollModification(context, true) {
        visibleChildren.forEach { it.draw0(context) }
    }


    override fun mouseMoved(context: RenderContext) = withScrollModification(context, false) {
        visibleChildren.forEach { it.mouseMoved(context) }
    }

    override fun mouseDown(context: RenderContext): Boolean = withScrollModification(context, false) {
        visibleChildrenReversed.forEach {
            if (!context.interactivity.mouseInside(it.box)) return@forEach
            if (it.mouseDown(context)) return@withScrollModification true
        }
        return@withScrollModification false
    }

    override fun mouseUp(context: RenderContext) = withScrollModification(context, false) {
        visibleChildren.forEach {
            it.mouseUp(context)
        }
    }

    abstract fun withChildren(children: List<AbstractUIElement>): BranchUIElement

    override fun mouseWheel(delta: Int, context: RenderContext): Boolean = withScrollModification(context, false) {
        visibleChildrenReversed.forEach {
            if (!context.interactivity.mouseInside(it.box)) return@forEach
            if (it.mouseWheel(delta, context)) return@withScrollModification true
        }

        val ch = contentHeight
        if (ch != null) {
            scrollOffset += delta * 10
            if (scrollOffset < 0) scrollOffset = 0
            if (scrollOffset > ch - childBox.height) scrollOffset = ch - childBox.height
        }

        return@withScrollModification super.mouseWheel(delta, context)
    }
}
