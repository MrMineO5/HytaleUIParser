package app.ultradev.hytaleuiparser.renderer.element

import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.generated.elements.ElementProperties
import app.ultradev.hytaleuiparser.renderer.RenderBox
import app.ultradev.hytaleuiparser.renderer.target.RenderTarget

abstract class AbstractUIElement(
    val node: AstNode
) {
    abstract val properties: ElementProperties

    val background by lazy { properties.background?.let(UIElementBackground::fromPatchStyle) }

    lateinit var box: RenderBox

    open fun desiredWidth(): Int? = properties.anchor?.width
    open fun desiredHeight(): Int? = properties.anchor?.height

    fun draw0(target: RenderTarget) {
        draw(target)
        afterDraw(target)
    }
    open fun draw(target: RenderTarget) {
        background?.draw(target, box)
    }
    open fun afterDraw(target: RenderTarget) {}
}
