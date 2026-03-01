package app.ultradev.hytaleuiparser.renderer.layout

import app.ultradev.hytaleuiparser.generated.types.LayoutMode
import app.ultradev.hytaleuiparser.renderer.BoxSize
import app.ultradev.hytaleuiparser.renderer.BoxSize.Companion.combine
import app.ultradev.hytaleuiparser.renderer.element.BranchUIElement
import app.ultradev.hytaleuiparser.renderer.layout.impl.LayoutBottom
import app.ultradev.hytaleuiparser.renderer.layout.impl.LayoutCenter
import app.ultradev.hytaleuiparser.renderer.layout.impl.LayoutCenterMiddle
import app.ultradev.hytaleuiparser.renderer.layout.impl.LayoutFull
import app.ultradev.hytaleuiparser.renderer.layout.impl.LayoutLeft
import app.ultradev.hytaleuiparser.renderer.layout.impl.LayoutLeftCenterWrap
import app.ultradev.hytaleuiparser.renderer.layout.impl.LayoutMiddle
import app.ultradev.hytaleuiparser.renderer.layout.impl.LayoutMiddleCenter
import app.ultradev.hytaleuiparser.renderer.layout.impl.LayoutRight
import app.ultradev.hytaleuiparser.renderer.layout.impl.LayoutTop

interface Layout {
    fun doLayout(element: BranchUIElement)

    val combineMode: BoxSize.BoxCombineMode
    fun contentDesiredSize(element: BranchUIElement, available: BoxSize): BoxSize {
        return element.visibleChildren.map { it.totalSize(available) }.combine(combineMode)
    }

    companion object {
        fun get(mode: LayoutMode): Layout {
            return when (mode) {
                LayoutMode.Full -> LayoutFull

                LayoutMode.Top -> LayoutTop
                LayoutMode.Left -> LayoutLeft
                LayoutMode.Right -> LayoutRight
                LayoutMode.Bottom -> LayoutBottom

                LayoutMode.Center -> LayoutCenter
                LayoutMode.Middle -> LayoutMiddle
                LayoutMode.CenterMiddle -> LayoutCenterMiddle
                LayoutMode.MiddleCenter -> LayoutMiddleCenter

                LayoutMode.LeftCenterWrap -> LayoutLeftCenterWrap

                // TODO: Implement scrolling correctly...
                // These layouts will probably just set a larger content pane than the element size which should
                //  trigger a scrollbar (in hytale at least)
                LayoutMode.TopScrolling -> LayoutTop
                LayoutMode.BottomScrolling -> LayoutBottom
                LayoutMode.LeftScrolling -> LayoutLeft
                LayoutMode.RightScrolling -> LayoutRight
            }
        }
    }
}
