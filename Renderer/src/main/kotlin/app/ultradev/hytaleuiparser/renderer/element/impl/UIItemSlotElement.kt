package app.ultradev.hytaleuiparser.renderer.element.impl

import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.generated.elements.ButtonProperties
import app.ultradev.hytaleuiparser.generated.elements.ElementProperties
import app.ultradev.hytaleuiparser.generated.elements.ItemSlotButtonProperties
import app.ultradev.hytaleuiparser.generated.elements.ItemSlotProperties
import app.ultradev.hytaleuiparser.generated.types.LayoutMode
import app.ultradev.hytaleuiparser.renderer.RenderContext
import app.ultradev.hytaleuiparser.renderer.element.AbstractUIElement
import app.ultradev.hytaleuiparser.renderer.element.BranchUIElement
import app.ultradev.hytaleuiparser.renderer.render.drawPatchStyle
import app.ultradev.hytaleuiparser.renderer.target.RenderTarget

class UIItemSlotElement(
    node: AstNode,
    override val properties: ItemSlotProperties,
) : AbstractUIElement(node) {
    override fun draw(target: RenderTarget, context: RenderContext) {
        super.draw(target, context)

        if (properties.itemId != null) {
            val item = context.cache.items[properties.itemId!!]

            if (properties.showQualityBackground == true) {
                val quality = item.Quality?.let { context.cache.itemQualities[it] }?.SlotTexture
                    ?.let { "Common/$it" }
                    ?: "Common/UI/ItemQualities/Slots/SlotDefault.png"

                val qualityBackground = context.cache.images[quality]
                target.renderImage(qualityBackground, box.x, box.y, box.width, box.height)
            }

            if (item.Icon != null) {
                val icon = context.cache.images["Common/${item.Icon}"]
                target.renderImage(icon, box.x, box.y, box.width, box.height)
            }
        }

//        drawPatchStyle(target, context, box, properties.)
    }

    override fun mouseDown(context: RenderContext): Boolean {
        context.active = this
        return true
    }

    override fun mouseUp(context: RenderContext) {
        if (context.active == this) context.active = null
    }

    override fun withProperties(properties: ElementProperties) = UIItemSlotElement(node, properties as ItemSlotProperties)
}
