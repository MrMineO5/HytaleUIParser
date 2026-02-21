package app.ultradev.hytaleuiparser.renderer.element.impl

import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.generated.elements.ButtonProperties
import app.ultradev.hytaleuiparser.generated.elements.ElementProperties
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

    override fun withChildren(children: List<AbstractUIElement>) = UIGroupElement(node, children, properties)

    override fun withProperties(properties: ElementProperties) = UIGroupElement(node, children, properties as GroupProperties)
}
