package app.ultradev.hytaleuiparser.renderer.element

import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.ast.NodeElementWithSelector
import app.ultradev.hytaleuiparser.generated.elements.ElementProperties
import app.ultradev.hytaleuiparser.generated.types.Padding
import app.ultradev.hytaleuiparser.renderer.BoxSize
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

    val selector: String? get() = (node as? NodeElementWithSelector)?.selector?.identifier

    val visible get() = properties.visible ?: true

    lateinit var box: RenderBox
    val contentBox: RenderBox get() = box.withPadding(properties.padding ?: Padding.EMPTY)

    internal var layoutContentWidth: Int? = null
    internal var layoutContentHeight: Int? = null
    val contentWidth: Int? get() = layoutContentWidth ?: properties.contentWidth
    val contentHeight: Int? get() = layoutContentHeight ?: properties.contentHeight

    open fun contentDesiredSize(available: BoxSize): BoxSize = BoxSize.ZERO

    fun desiredSize(available: BoxSize): BoxSize {
        var width = properties.anchor?.width
        var height = properties.anchor?.height

        val box = contentDesiredSize(available)

        if (width == null) {
            val pl = properties.padding?.leftFallback() ?: 0
            val pr = properties.padding?.rightFallback() ?: 0

            width = box.width
            width += pl + pr
        }

        if (height == null) {
            val pt = properties.padding?.topFallback() ?: 0
            val pb = properties.padding?.bottomFallback() ?: 0

            height = box.height
            height += pt + pb
        }

        return BoxSize(width, height)
    }
    fun desiredSizeFromTotal(available: BoxSize): BoxSize {
        val left = properties.anchor?.leftFallback() ?: 0
        val right = properties.anchor?.rightFallback() ?: 0
        val top = properties.anchor?.topFallback() ?: 0
        val bottom = properties.anchor?.bottomFallback() ?: 0
        return desiredSize(available - BoxSize(left + right, top + bottom))
    }
    fun totalSize(available: BoxSize): BoxSize {
        return desiredSizeFromTotal(available) + BoxSize(
            width = (properties.anchor?.leftFallback() ?: 0) + (properties.anchor?.rightFallback() ?: 0),
            height = (properties.anchor?.topFallback() ?: 0) + (properties.anchor?.bottomFallback() ?: 0)
        )
    }


    fun draw0(target: RenderTarget, context: RenderContext) {
//        val old = target.setClip(contentBox)
        draw(target, context)
//        target.setClip(old)
        afterDraw(target, context)
    }

    open fun draw(target: RenderTarget, context: RenderContext) {
        drawPatchStyle(target, context, box, properties.background)
    }

    open fun afterDraw(target: RenderTarget, context: RenderContext) {}


    /* ----------------
     * | INTERACTIONS |
     * ---------------- */
    open fun mouseMoved(context: RenderContext) {}

    open fun mouseDown(context: RenderContext): Boolean {
        return false
    }
    open fun mouseUp(context: RenderContext) {}

    open fun mouseWheel(delta: Int, context: RenderContext): Boolean {
        return false
    }



    abstract fun withProperties(properties: ElementProperties): AbstractUIElement
}
