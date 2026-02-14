package app.ultradev.hytaleuiparser.renderer.element

import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.generated.elements.ElementProperties

class UnknownUIElement(
    node: AstNode,
    children: List<AbstractUIElement>,
    override val properties: ElementProperties
) : BranchUIElement(node, children)
