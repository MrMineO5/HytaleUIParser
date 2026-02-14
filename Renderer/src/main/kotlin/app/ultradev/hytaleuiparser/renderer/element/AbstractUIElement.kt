package app.ultradev.hytaleuiparser.renderer.element

import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.generated.elements.ElementProperties
import app.ultradev.hytaleuiparser.generated.types.Padding
import app.ultradev.hytaleuiparser.renderer.RenderBox
import app.ultradev.hytaleuiparser.renderer.extensions.bottomFallback
import app.ultradev.hytaleuiparser.renderer.extensions.leftFallback
import app.ultradev.hytaleuiparser.renderer.extensions.rightFallback
import app.ultradev.hytaleuiparser.renderer.extensions.topFallback
import app.ultradev.hytaleuiparser.renderer.target.RenderTarget

abstract class AbstractUIElement(
    val node: AstNode
) {
    abstract val properties: ElementProperties

    val visible get() = properties.visible ?: true

    val background by lazy { properties.background?.let(UIElementBackground::fromPatchStyle) }

    lateinit var box: RenderBox
    val contentBox: RenderBox by lazy { box.withPadding(properties.padding ?: Padding.EMPTY) }

    open fun contentDesiredWidth(): Int = 0
    open fun contentDesiredHeight(): Int = 0

    fun desiredWidth(): Int {
        var width = properties.anchor?.width
        if (width != null) return width
        width = contentDesiredWidth()

        val pl = properties.padding?.leftFallback() ?: 0
        val pr = properties.padding?.rightFallback() ?: 0
        width += pl + pr
        return width
    }
    fun desiredHeight(): Int {
        var height = properties.anchor?.height
        if (height != null) return height
        height = contentDesiredHeight()

        val pt = properties.padding?.topFallback() ?: 0
        val pb = properties.padding?.bottomFallback() ?: 0
        height += pt + pb
        return height
    }

    fun totalWidth(): Int = desiredWidth() + (properties.anchor?.leftFallback() ?: 0) + (properties.anchor?.rightFallback() ?: 0)
    fun totalHeight(): Int = desiredHeight() + (properties.anchor?.topFallback() ?: 0) + (properties.anchor?.bottomFallback() ?: 0)


    fun draw0(target: RenderTarget) {
        draw(target)
        afterDraw(target)
    }
    open fun draw(target: RenderTarget) {
        background?.draw(target, box)
    }
    open fun afterDraw(target: RenderTarget) {}
}
