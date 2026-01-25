package app.ultradev.hytaleuiparser

import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.ast.NodeAssignVariable
import app.ultradev.hytaleuiparser.ast.NodeConstant
import app.ultradev.hytaleuiparser.ast.NodeElement
import app.ultradev.hytaleuiparser.ast.NodeField
import app.ultradev.hytaleuiparser.ast.NodeIdentifier
import app.ultradev.hytaleuiparser.ast.NodeSelectorElement
import app.ultradev.hytaleuiparser.ast.NodeToken
import app.ultradev.hytaleuiparser.ast.NodeType
import app.ultradev.hytaleuiparser.ast.NodeVariable
import app.ultradev.hytaleuiparser.ast.RootNode
import app.ultradev.hytaleuiparser.validation.ElementType

object Validator {
    val types = mutableSetOf<String>()
    val properties = mutableMapOf<ElementType, MutableSet<String>>()
    val typeProperties = mutableMapOf<String, MutableSet<String>>()

    fun validate(ast: AstNode) {
        when (ast) {
            is RootNode -> ast.children.forEach(::validate)
            is NodeElement -> validateElement(ast)
            is NodeAssignVariable -> validateAssignVariable(ast)

            is NodeToken -> {} // Skipped
            else -> {
                println("WARNING: No validation for ${ast::class.simpleName}")
                ast.children.forEach(::validate)
            }
        }
    }

    fun validateElement(node: NodeElement) {
        val type = node.type
        if (type is NodeIdentifier) {
            val elementType = ElementType.valueOf(type.identifier)

            properties.computeIfAbsent(elementType) { mutableSetOf() }
                .addAll(node.properties.map { it.identifier.identifier })

            node.properties.forEach {


                val value = it.value
                if (value is NodeType) {
                    typeProperties.computeIfAbsent(it.identifier.identifier) { mutableSetOf() }
                        .addAll(value.body.elements.filterIsInstance<NodeField>().map { it.identifier.identifier })
                }
            }

            node.properties.find { it.identifier.identifier == "Anchor" }?.let {
                val value = it.value
                when (value) {
                    is NodeType -> {}
                    is NodeVariable -> {}
                }
                println(value)
            }
        }

        node.childElements.forEach(::validateElement)
        node.selectorElements.forEach(::validateSelectorElement)
    }

    fun validateSelectorElement(node: NodeSelectorElement) {
        node.childElements.forEach(::validateElement)
        node.selectorElements.forEach(::validateSelectorElement)
    }

    fun validateAssignVariable(node: NodeAssignVariable) {
        val value = node.value
        when (value) {
            is NodeConstant -> {}
            is NodeType -> {
                if (value.type != null) {
                    types.add(value.type.identifier)
                }
            }

            else -> println("WARNING: Unsupported variable value type: ${value::class.simpleName}")
        }
    }
}
