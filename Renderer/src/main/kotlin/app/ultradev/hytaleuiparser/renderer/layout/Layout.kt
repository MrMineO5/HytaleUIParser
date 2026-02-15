package app.ultradev.hytaleuiparser.renderer.layout

import app.ultradev.hytaleuiparser.generated.types.LayoutMode
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

    fun contentDesiredWidth(element: BranchUIElement, available: Int): Int
    fun contentDesiredHeight(element: BranchUIElement, available: Int): Int

    companion object {
        fun get(mode: LayoutMode): Layout {
            return when (mode) {
                LayoutMode.Full -> LayoutFull
                LayoutMode.Top -> LayoutTop
                LayoutMode.Left -> LayoutLeft
                LayoutMode.Middle -> LayoutMiddle
                LayoutMode.Right -> LayoutRight
                LayoutMode.Bottom -> LayoutBottom
                LayoutMode.Center -> LayoutCenter
                LayoutMode.CenterMiddle -> LayoutCenterMiddle
                LayoutMode.MiddleCenter -> LayoutMiddleCenter
                LayoutMode.LeftCenterWrap -> LayoutLeftCenterWrap
                // TODO: Support more modes
                else -> {
                    System.err.println("Warning: Unimplemented layout mode: $mode")
                    LayoutFull
                }
            }
        }
    }
}
