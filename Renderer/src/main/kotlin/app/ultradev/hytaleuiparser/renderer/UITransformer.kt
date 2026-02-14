package app.ultradev.hytaleuiparser.renderer

import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.ast.NodeElement
import app.ultradev.hytaleuiparser.ast.NodeElementWithSelector
import app.ultradev.hytaleuiparser.ast.RootNode
import app.ultradev.hytaleuiparser.generated.elements.ElementProperties
import app.ultradev.hytaleuiparser.generated.elements.GroupProperties
import app.ultradev.hytaleuiparser.generated.elements.LabelProperties
import app.ultradev.hytaleuiparser.renderer.element.AbstractUIElement
import app.ultradev.hytaleuiparser.renderer.element.UnknownUIElement
import app.ultradev.hytaleuiparser.renderer.element.impl.UIGroupElement
import app.ultradev.hytaleuiparser.renderer.element.impl.UILabelElement

object UITransformer {
    fun transform(root: RootNode): UIGroupElement {
        val children = root.elements.map { processElement(it) }

        // TODO: We can use this to render ui files as if they were in a container with a different e.g. LayoutMode
        return UIGroupElement(
            root,
            children,
            GroupProperties()
        )
    }

    private fun processElement(node: NodeElement): AbstractUIElement {
        val varImpl = node.resolvedVariableImplementation
        return if (varImpl != null) {
            processVarNode(node, varImpl)
        } else {
            processNormalNode(node)
        }
    }

    private fun processVarNode(implNode: NodeElement, varNode: NodeElement): AbstractUIElement {
        val properties = ElementProperties.fromProperties(implNode.resolvedType, implNode.resolveProperties())

        val children = if (implNode.resolvedType.allowsChildren) {
            val bySelector = implNode.selectorElements.associateBy { it.selector!!.identifier }
            varNode.childElements.map { child ->
                if (child !is NodeElementWithSelector) return@map processElement(child)
                val correspondingSelector = bySelector[child.selector!!.identifier] ?: return@map processElement(child)

                val combinedProperties = child.resolveProperties() + correspondingSelector.resolveProperties()
                val elementProperties = ElementProperties.fromProperties(child.resolvedType, combinedProperties)

                val childChildren = child.childElements.map { processElement(it) } +
                        correspondingSelector.childElements.map { processElement(it) }

                createUIElement(child, elementProperties, childChildren)
            } +
                    implNode.childElements.map { processElement(it) }
        } else emptyList()

        return createUIElement(implNode, properties, children)
    }

    private fun processNormalNode(node: NodeElement): AbstractUIElement {
        val properties = ElementProperties.fromProperties(node.resolvedType, node.resolveProperties())

        val children = if (node.resolvedType.allowsChildren) {
            node.childElements.map { processElement(it) }
        } else emptyList()

        return createUIElement(node, properties, children)
    }

    private fun createUIElement(node: AstNode, properties: ElementProperties, children: List<AbstractUIElement> = emptyList()): AbstractUIElement {
        return when (properties) {
            is LabelProperties -> UILabelElement(node, properties)
            is GroupProperties -> UIGroupElement(node, children, properties)

            else -> {
                System.err.println("Warning: Unknown element type: ${properties::class.simpleName}")
                UnknownUIElement(node, children, properties)
            }
        }
    }
}