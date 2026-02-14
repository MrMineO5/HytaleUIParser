package app.ultradev.hytaleuiparser.renderer.element.impl

import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.generated.elements.GroupProperties
import app.ultradev.hytaleuiparser.generated.types.LayoutMode
import app.ultradev.hytaleuiparser.renderer.element.AbstractUIElement
import app.ultradev.hytaleuiparser.renderer.element.BranchUIElement
import app.ultradev.hytaleuiparser.renderer.target.RenderTarget

class UIGroupElement(
    node: AstNode,
    children: List<AbstractUIElement>,
    override val properties: GroupProperties,
) : BranchUIElement(node, children) {
    override val layoutMode: LayoutMode
        get() = properties.layoutMode ?: super.layoutMode

    override fun draw(target: RenderTarget) {
        super.draw(target)
        if (background?.color != null) {
            println("Color: ${background?.color}, box: $box, node: ${node.text}")
        }
    }
}
