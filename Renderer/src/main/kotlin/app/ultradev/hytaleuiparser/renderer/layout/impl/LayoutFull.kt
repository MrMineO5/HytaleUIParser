package app.ultradev.hytaleuiparser.renderer.layout.impl

import app.ultradev.hytaleuiparser.generated.types.Anchor
import app.ultradev.hytaleuiparser.renderer.element.BranchUIElement
import app.ultradev.hytaleuiparser.renderer.layout.Layout

object LayoutFull : Layout {
    override fun doLayout(element: BranchUIElement) {
        element.children.forEach { child ->
            child.box = element.contentBox
                .withAnchor(child.properties.anchor ?: Anchor.EMPTY)
        }
    }

    override fun contentDesiredHeight(element: BranchUIElement): Int = element.children.maxOfOrNull { it.desiredHeight() } ?: 0
    override fun contentDesiredWidth(element: BranchUIElement): Int = element.children.maxOfOrNull { it.desiredWidth() } ?: 0
}
