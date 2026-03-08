package app.ultradev.hytaleuiparser.renderer.element.impl

import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.generated.elements.ElementProperties
import app.ultradev.hytaleuiparser.generated.elements.ItemSlotProperties
import app.ultradev.hytaleuiparser.renderer.context.RenderContext
import app.ultradev.hytaleuiparser.renderer.element.AbstractUIElement

class UIItemSlotElement(
    node: AstNode,
    override val properties: ItemSlotProperties,
) : AbstractUIElement(node) {
    override fun draw(context: RenderContext) {
        super.draw(context)

        if (properties.itemId != null) {
            val item = context.cache.items[properties.itemId!!]

            if (properties.showQualityBackground == true) {
                val quality = item.Quality?.let { context.cache.itemQualities[it] }?.SlotTexture
                    ?.let { "Common/$it" }
                    ?: "Common/UI/ItemQualities/Slots/SlotDefault.png"

                val qualityBackground = context.cache.images[quality]
                context.draw.renderImage(qualityBackground, box)
            }

            if (item.Icon != null) {
                val icon = context.cache.images["Common/${item.Icon}"]
                context.draw.renderImage(icon, box)
            }
        }

//        drawPatchStyle(target, context, box, properties.)
    }

    override fun mouseDown(context: RenderContext): Boolean {
        context.interactivity.active = this
        return true
    }

    override fun mouseUp(context: RenderContext) {
        if (context.interactivity.active == this) context.interactivity.active = null
    }

    override fun withProperties(properties: ElementProperties) = UIItemSlotElement(node, properties as ItemSlotProperties)
}
