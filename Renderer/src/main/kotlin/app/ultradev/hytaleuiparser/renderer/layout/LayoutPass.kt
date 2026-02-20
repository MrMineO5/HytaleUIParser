package app.ultradev.hytaleuiparser.renderer.layout

import app.ultradev.hytaleuiparser.renderer.element.BranchUIElement

object LayoutPass {
    fun run(element: BranchUIElement) {
        val mode = element.layoutMode
        Layout.get(mode).doLayout(element)

        element.visibleChildren.forEach {
            if (it is BranchUIElement) run(it)
        }
    }
}