package app.ultradev.hytaleuiparser.renderer.element

import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.generated.elements.ElementProperties
import app.ultradev.hytaleuiparser.generated.types.Padding
import app.ultradev.hytaleuiparser.renderer.RenderBox
import app.ultradev.hytaleuiparser.renderer.RenderContext
import app.ultradev.hytaleuiparser.renderer.extensions.bottomFallback
import app.ultradev.hytaleuiparser.renderer.extensions.leftFallback
import app.ultradev.hytaleuiparser.renderer.extensions.rightFallback
import app.ultradev.hytaleuiparser.renderer.extensions.topFallback
import app.ultradev.hytaleuiparser.renderer.render.drawPatchStyle
import app.ultradev.hytaleuiparser.renderer.target.RenderTarget

abstract class AbstractUIElement(
    val node: AstNode
) {
    abstract val properties: ElementProperties

    val visible get() = properties.visible ?: true

    lateinit var box: RenderBox
    val contentBox: RenderBox get() = box.withPadding(properties.padding ?: Padding.EMPTY)

    open fun contentDesiredWidth(available: Int): Int = 0
    open fun contentDesiredHeight(available: Int): Int = 0

    fun desiredWidth(available: Int): Int {
        var width = properties.anchor?.width
        if (width != null) return width
        val pl = properties.padding?.leftFallback() ?: 0
        val pr = properties.padding?.rightFallback() ?: 0

        width = contentDesiredWidth(available - pl - pr)

        width += pl + pr
        return width
    }

    fun desiredHeight(available: Int): Int {
        var height = properties.anchor?.height
        if (height != null) return height
        height = contentDesiredHeight(available)

        val pt = properties.padding?.topFallback() ?: 0
        val pb = properties.padding?.bottomFallback() ?: 0
        height += pt + pb
        return height
    }

    fun desiredWidthFromTotal(available: Int): Int {
        val left = properties.anchor?.leftFallback() ?: 0
        val right = properties.anchor?.rightFallback() ?: 0
        return desiredWidth(available - left - right)
    }

    fun desiredHeightFromTotal(available: Int): Int {
        val top = properties.anchor?.topFallback() ?: 0
        val bottom = properties.anchor?.bottomFallback() ?: 0
        return desiredHeight(available - top - bottom)
    }

    fun totalWidth(available: Int): Int = desiredWidthFromTotal(available) + (properties.anchor?.leftFallback()
        ?: 0) + (properties.anchor?.rightFallback() ?: 0)

    fun totalHeight(available: Int): Int = desiredHeightFromTotal(available) + (properties.anchor?.topFallback()
        ?: 0) + (properties.anchor?.bottomFallback() ?: 0)


    fun draw0(target: RenderTarget, context: RenderContext) {
        draw(target, context)
        afterDraw(target, context)
    }

    open fun draw(target: RenderTarget, context: RenderContext) {
        drawPatchStyle(target, context, box, properties.background)
    }

    open fun afterDraw(target: RenderTarget, context: RenderContext) {}
}
