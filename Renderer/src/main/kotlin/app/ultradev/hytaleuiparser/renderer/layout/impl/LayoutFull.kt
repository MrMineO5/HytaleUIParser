package app.ultradev.hytaleuiparser.renderer.layout.impl

import app.ultradev.hytaleuiparser.generated.types.Anchor
import app.ultradev.hytaleuiparser.renderer.BoxSize
import app.ultradev.hytaleuiparser.renderer.element.BranchUIElement
import app.ultradev.hytaleuiparser.renderer.extensions.maxOfOrZero
import app.ultradev.hytaleuiparser.renderer.layout.Layout

object LayoutFull : Layout {
    override fun doLayout(element: BranchUIElement) {
        element.visibleChildren.forEach { child ->
            child.box = element.contentBox
                .withAnchor(child.properties.anchor ?: Anchor.EMPTY)
        }
    }

    override val combineMode = BoxSize.BoxCombineMode(
        BoxSize.AxisCombineMode.MAX_OR_ZERO,
        BoxSize.AxisCombineMode.MAX_OR_ZERO
    )
}
