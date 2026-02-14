package app.ultradev.hytaleuiparser.renderer.layout

import app.ultradev.hytaleuiparser.generated.types.LayoutMode
import app.ultradev.hytaleuiparser.renderer.element.BranchUIElement
import app.ultradev.hytaleuiparser.renderer.layout.impl.LayoutFull

interface Layout {
    fun doLayout(element: BranchUIElement)

    companion object {
        fun get(mode: LayoutMode): Layout {
            return when (mode) {
                // TODO: Support more modes
                else -> LayoutFull
            }
        }
    }
}