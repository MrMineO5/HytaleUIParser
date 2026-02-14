package app.ultradev.hytaleuiparser.renderer.layout.impl

import app.ultradev.hytaleuiparser.generated.types.Anchor
import app.ultradev.hytaleuiparser.generated.types.Padding
import app.ultradev.hytaleuiparser.renderer.element.BranchUIElement
import app.ultradev.hytaleuiparser.renderer.layout.Layout

object LayoutFull : Layout {
    override fun doLayout(element: BranchUIElement) {
        element.children.forEach { child ->
            child.box = element.box
                .withPadding(element.properties.padding ?: Padding.EMPTY)
                .withAnchor(child.properties.anchor ?: Anchor.EMPTY)
        }
    }
}