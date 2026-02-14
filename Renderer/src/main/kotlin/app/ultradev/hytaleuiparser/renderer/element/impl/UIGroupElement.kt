package app.ultradev.hytaleuiparser.renderer.element.impl

import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.generated.elements.GroupProperties
import app.ultradev.hytaleuiparser.generated.elements.LabelProperties
import app.ultradev.hytaleuiparser.generated.types.LabelStyle
import app.ultradev.hytaleuiparser.generated.types.LayoutMode
import app.ultradev.hytaleuiparser.renderer.element.AbstractUIElement
import app.ultradev.hytaleuiparser.renderer.element.BranchUIElement
import app.ultradev.hytaleuiparser.renderer.target.RenderTarget
import app.ultradev.hytaleuiparser.renderer.text.TextRenderInfo
import java.awt.Color
import java.awt.font.FontRenderContext

class UIGroupElement(
    node: AstNode,
    children: List<AbstractUIElement>,
    override val properties: GroupProperties,
) : BranchUIElement(node, children) {
    override val layoutMode: LayoutMode
        get() = properties.layoutMode ?: super.layoutMode
}
