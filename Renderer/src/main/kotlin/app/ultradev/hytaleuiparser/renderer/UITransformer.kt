package app.ultradev.hytaleuiparser.renderer

import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.ast.NodeElement
import app.ultradev.hytaleuiparser.ast.NodeElementWithSelector
import app.ultradev.hytaleuiparser.ast.RootNode
import app.ultradev.hytaleuiparser.ast.VariableValue
import app.ultradev.hytaleuiparser.asttools.valueAsString
import app.ultradev.hytaleuiparser.generated.types.Anchor
import app.ultradev.hytaleuiparser.generated.types.Padding
import app.ultradev.hytaleuiparser.generated.types.PatchStyle
import app.ultradev.hytaleuiparser.renderer.element.UIElement
import app.ultradev.hytaleuiparser.renderer.element.UIElementBackground
import app.ultradev.hytaleuiparser.renderer.element.UILabelElement
import app.ultradev.hytaleuiparser.validation.ElementType
import java.awt.Image
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import kotlin.io.path.exists
import kotlin.io.path.name

class UITransformer(val root: RootNode) {
    private val elements = mutableListOf<UIElement>()
    var indent = 0

    fun buildElementList(box: RenderBox): List<UIElement> {
        root.elements.forEach { renderElement(it, box) }
        return elements
    }


    private fun renderElement(node: NodeElement, box: RenderBox) {
        val properties = node.resolveProperties()

        val childBox = createUIElement(node, node.resolvedType, properties, box)

        indent++
        val varImpl = node.resolvedVariableImplementation
        if (varImpl != null) {
            varImpl.childElements.forEach { child ->
                val correspondingSelector = node.selectorElements.find { it.selector!!.identifier == (child as? NodeElementWithSelector)?.selector?.identifier } ?: run {
                    renderElement(child, childBox)
                    return@forEach
                }

                val combinedProperties = child.resolveProperties() + correspondingSelector.resolveProperties()
                val childBox = createUIElement(child, child.resolvedType, combinedProperties, childBox)

                indent++
                child.childElements.forEach { renderElement(it, childBox) }
                correspondingSelector.childElements.forEach { renderElement(it, childBox) }
                indent--
            }
        }
        node.childElements.forEach { renderElement(it, childBox) }
        indent--
    }

    private fun createUIElement(node: AstNode, type: ElementType, properties: Map<String, VariableValue>, parentBox: RenderBox): RenderBox {
        val anchor = properties["Anchor"]?.let(Anchor::fromVariable) ?: Anchor.EMPTY
        val padding = properties["Padding"]?.let(Padding::fromVariable) ?: Padding.EMPTY

        val selfBox = parentBox.withAnchor(anchor)
        val childBox = selfBox.withPadding(padding)

        val background = properties["Background"]?.let(PatchStyle::fromVariable) ?: PatchStyle.EMPTY

        val element = when (type) {
            ElementType.Label -> {
                val text = properties["Text"]?.valueAsString() ?: ""
                UILabelElement(node, selfBox, patchStyleToBackground(node, background), text)
            }

            else -> UIElement(node, selfBox, patchStyleToBackground(node, background))
        }
        elements.add(element)

        return childBox
    }


    private fun patchStyleToBackground(node: AstNode, style: PatchStyle): UIElementBackground {
        val image = style.texturePath?.let {
            val path = TestRenderer.basePath.resolve(style.texturePath)
            if (!path.exists()) {
                // try @2x and scale it
                val newPath = path.parent.resolve(path.name.substringBeforeLast(".") + "@2x." + path.name.substringAfterLast("."))
                if (!newPath.exists()) error("Couldn't find texture: $newPath next to ${node.file.path}")
                val image = ImageIO.read(newPath.toFile())
                val new = BufferedImage(image.width / 2, image.height / 2, BufferedImage.TYPE_INT_ARGB)
                val g = new.createGraphics()
                g.drawImage(image.getScaledInstance(image.width / 2, image.height / 2, Image.SCALE_FAST), 0, 0, null)
                g.dispose()
                new
            } else {
                ImageIO.read(path.toFile())
            }
        }
        return UIElementBackground(
            color = style.color,
            image = image,
            horizontalBorder = style.horizontalBorder ?: style.border ?: 0,
            verticalBorder = style.verticalBorder ?: style.border ?: 0,
        )
    }
}