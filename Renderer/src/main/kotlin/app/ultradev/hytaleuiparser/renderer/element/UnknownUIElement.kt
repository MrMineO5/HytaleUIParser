package app.ultradev.hytaleuiparser.renderer.element

import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.generated.elements.ElementProperties

class UnknownUIElement(
    node: AstNode,
    children: List<AbstractUIElement>,
    override val properties: ElementProperties
) : BranchUIElement(node, children) {
    override fun withChildren(children: List<AbstractUIElement>) = UnknownUIElement(node, children, properties)

    override fun withProperties(properties: ElementProperties): AbstractUIElement = UnknownUIElement(node, children, properties)
}
