package app.ultradev.hytaleuiparser

import app.ultradev.hytaleuiparser.ast.*
import app.ultradev.hytaleuiparser.token.Token

class Parser(tokens: Iterator<Token>) {
    val tokens = TokenIterator(tokens, skipComments = true)
    val nodes = mutableListOf<AstNode>()

    var previous: AstNode? = null

    fun finish(): RootNode {
        parseCompletely()
        return RootNode(nodes)
    }

    fun hasNext() = tokens.hasNext()

    private fun parseCompletely() {
        while (tokens.hasNext()) {
            nodes.add(parseRoot())
        }
    }

    fun parseRoot(): AstNode {
        val node = tokens.peek()

        return when (node.type) {
            Token.Type.VARIABLE_MARKER -> parseVariableAssignmentOrElement()
            Token.Type.REFERENCE_MARKER -> parseRefAssignmentOrElement()


            // Skip comments
            Token.Type.COMMENT -> {
                tokens.next()
                return parseRoot()
            }

            Token.Type.IDENTIFIER -> parseElement()

            else -> throw ParserException("Unsupported root node type found: ${node.type}", node)
        }
    }

    private fun parseVariableOrRefMember(): AstNode {
        val next = tokens.peek()
        return when (next.type) {
            Token.Type.VARIABLE_MARKER -> parseVariable()
            Token.Type.REFERENCE_MARKER -> parseRefMember()
            else -> throw ParserException("Expected variable or reference", next)
        }
    }

    private fun parseVariable(): NodeVariable {
        val marker = tokens.next()
        if (marker.type != Token.Type.VARIABLE_MARKER) throw ParserException("Expected variable marker", marker)
        val identifier = parseIdentifier()
        return NodeVariable(NodeToken(marker), identifier)
    }

    private fun parseIdentifier(): NodeIdentifier {
        val token = tokens.next()
        if (token.type != Token.Type.IDENTIFIER) throw ParserException("Expected identifier", token)
        return NodeIdentifier(NodeToken(token), token.text)
    }

    private fun parseVariableAssignment(): NodeAssignVariable {
        val variable = parseVariable()
        val assignment = tokens.next()
        if (assignment.type != Token.Type.ASSIGNMENT) throw ParserException("Expected assignment operator", assignment)
        val value = parseVariableValue()
        if (value !is VariableValue) throw ParserException(
            "Expected variable value after assignment operator", assignment
        ) // TODO: AstNode should always have a token?
        val end = parseEndStatement()
        return NodeAssignVariable(variable, NodeToken(assignment), value, end)
    }

    private fun parseReference(): NodeReference {
        val marker = tokens.next()
        if (marker.type != Token.Type.REFERENCE_MARKER) throw ParserException("Expected reference marker", marker)
        val identifier = parseIdentifier()
        return NodeReference(NodeToken(marker), identifier)
    }

    private fun parseReferenceAssignment(): NodeAssignReference {
        val reference = parseReference()
        val assignment = tokens.next()
        if (assignment.type != Token.Type.ASSIGNMENT) throw ParserException("Expected assignment operator", assignment)
        val filePath = parseStringConstant()
        val endStatement = parseEndStatement()
        return NodeAssignReference(reference, NodeToken(assignment), filePath, endStatement)
    }

    private fun parseEndStatement(): NodeToken {
        val endStatement = tokens.next()
        if (endStatement.type != Token.Type.END_STATEMENT) throw ParserException("Expected end statement", endStatement)
        return NodeToken(endStatement)
    }

    private fun parseStringConstant(): NodeConstant {
        val token = tokens.next()
        if (token.type != Token.Type.STRING && token.type != Token.Type.IDENTIFIER) throw ParserException(
            "Expected string constant", token
        )
        return NodeConstant(listOf(NodeToken(token)), token.text)
    }

    private fun parseVariableValue(): AstNode {
        val token = tokens.peek()

        val variable = when (token.type) {
            Token.Type.STRING -> parseStringConstant()
            Token.Type.IDENTIFIER -> {
                val next = tokens.peek(2)
                when (next.type) {
                    Token.Type.START_ELEMENT, Token.Type.SELECTOR_MARKER -> parseElement()
                    Token.Type.START_TYPE -> parseType()

                    Token.Type.FIELD_DELIMITER, Token.Type.END_STATEMENT, Token.Type.END_TYPE,
                    Token.Type.MATH_ADD, Token.Type.MATH_SUBTRACT, Token.Type.MATH_MULTIPLY, Token.Type.MATH_DIVIDE -> parseStringConstant()

                    Token.Type.MEMBER_MARKER -> parseDecimalNumber()

                    else -> throw ParserException("Unexpected token after identifier", next)
                }
            }

            Token.Type.VARIABLE_MARKER -> parseVariable()
            Token.Type.REFERENCE_MARKER -> parseRefMember()
            Token.Type.TRANSLATION_MARKER -> parseTranslation()
            Token.Type.START_TYPE -> parseType()
            Token.Type.SELECTOR_MARKER -> parseColor()
            Token.Type.MATH_SUBTRACT -> {
                val minus = tokens.next()
                val value = parseVariableValue()
                NodeNegate(NodeToken(minus), value)
            }

            else -> throw ParserException("Expected a variable value", token)
        }

        val next = tokens.peek()

        return when (next.type) {
            Token.Type.MATH_ADD, Token.Type.MATH_SUBTRACT, Token.Type.MATH_MULTIPLY, Token.Type.MATH_DIVIDE -> {
                val mathOperator = tokens.next()
                val right = parseVariableValue()
                if (right is NodeMathOperation) {
                    NodeMathOperation(
                        NodeMathOperation(variable, NodeToken(mathOperator), right.param1),
                        right.operator, right.param2
                    )
                } else {
                    NodeMathOperation(variable, NodeToken(mathOperator), right)
                }
            }

            else -> variable
        }
    }

    private fun parseType(): NodeType {
        var start = tokens.peek()

        var type: NodeIdentifier? = null
        if (start.type == Token.Type.IDENTIFIER) {
            type = parseIdentifier()
        }

        start = tokens.next()

        if (start.type != Token.Type.START_TYPE) throw ParserException("Expected start type", start)

        val children = mutableListOf<AstNode>()
        while (tokens.hasNext()) {
            val next = tokens.peek()

            when (next.type) {
                Token.Type.IDENTIFIER -> children.add(parseField())
                Token.Type.SPREAD -> children.add(parseSpread())

                Token.Type.END_TYPE -> return NodeType(
                    type, NodeBody(NodeToken(start), NodeToken(tokens.next()), children)
                )

                else -> throw ParserException("Expected spread, field, or end type", next)
            }
        }

        throw ParserException("Unclosed type", start)
    }

    private fun parseElement(): NodeElement { // TODO: Handle selector
        val ident = tokens.peek()

        val identifier = when (ident.type) {
            Token.Type.IDENTIFIER -> parseIdentifier()
            Token.Type.VARIABLE_MARKER -> parseVariable()
            Token.Type.REFERENCE_MARKER -> parseRefMember()
            else -> throw ParserException("Expected identifier or variable", ident)
        }

        var selector: NodeSelector? = null
        val next = tokens.peek()
        if (next.type == Token.Type.SELECTOR_MARKER) {
            selector = parseSelector()
        }

        val body = parseElementBody()
        return NodeElement(identifier, body, selector)
    }

    private fun parseSelectorElement(): NodeSelectorElement {
        val selector = parseSelector()
        val body = parseElementBody()
        return NodeSelectorElement(selector, body)
    }

    private fun parseElementBody(): NodeBody {
        val start = tokens.next()
        if (start.type != Token.Type.START_ELEMENT) throw ParserException("Expected start element", start)

        val children = mutableListOf<AstNode>()

        while (tokens.hasNext()) {
            val next = tokens.peek()

            when (next.type) {
                Token.Type.END_ELEMENT -> return NodeBody(NodeToken(start), NodeToken(tokens.next()), children)

                Token.Type.VARIABLE_MARKER -> children.add(parseVariableAssignmentOrElement())
                Token.Type.REFERENCE_MARKER -> children.add(parseRefAssignmentOrElement())
                Token.Type.SELECTOR_MARKER -> children.add(parseSelectorElement())

                Token.Type.IDENTIFIER -> {
                    val nextNext = tokens.peek(2)
                    when (nextNext.type) {
                        Token.Type.FIELD_MARKER -> {
                            children.add(parseField())
                        }

                        Token.Type.START_ELEMENT, Token.Type.SELECTOR_MARKER -> children.add(parseElement())

                        else -> throw ParserException("Expected field or element in element body", next)
                    }
                }

                else -> throw ParserException("Invalid token in element body", next)
            }
        }
        throw ParserException("Unclosed element body", start)
    }

    private fun parseField(): NodeField {
        val identifier = parseIdentifier()
        val fieldMarker = tokens.next()
        if (fieldMarker.type != Token.Type.FIELD_MARKER) throw ParserException("Expected field marker", fieldMarker)
        val value = parseVariableValue()

        val next = tokens.peek()
        val end = when (next.type) {
            Token.Type.FIELD_DELIMITER, Token.Type.END_STATEMENT -> NodeToken(tokens.next())
            Token.Type.END_TYPE -> null

            else -> throw ParserException("Expected field delimiter, end statement, or end type after field", next)
        }

        return NodeField(identifier, NodeToken(fieldMarker), value, end)
    }

    private fun parseSelector(): NodeSelector {
        val marker = tokens.next()
        if (marker.type != Token.Type.SELECTOR_MARKER) throw ParserException("Expected selector marker", marker)
        val identifier = parseIdentifier()
        return NodeSelector(NodeToken(marker), identifier)
    }

    private fun parseColor(): NodeColor {
        val colorMarker = tokens.next()
        if (colorMarker.type != Token.Type.SELECTOR_MARKER) throw ParserException("Expected color marker", colorMarker)
        val value = parseStringConstant()

        var opacity: NodeOpacity? = null
        val next = tokens.peek()
        if (next.type == Token.Type.START_TYPE) {
            opacity = parseOpacity()
        }

        return NodeColor(NodeToken(colorMarker), value, opacity)
    }

    private fun parseOpacity(): NodeOpacity {
        val start = tokens.next()
        if (start.type != Token.Type.START_TYPE) throw ParserException("Expected start type marker", start)

        val value = parseDecimalNumber()

        val end = tokens.next()
        if (end.type != Token.Type.END_TYPE) throw ParserException("Expected end type marker", end)
        return NodeOpacity(NodeToken(start), NodeToken(end), value)
    }

    private fun parseDecimalNumber(): NodeConstant {
        val integerPart = tokens.next()
        if (integerPart.type != Token.Type.IDENTIFIER) throw ParserException("Expected decimal number", integerPart)
        val decimal = tokens.peek()
        return if (decimal.type == Token.Type.MEMBER_MARKER) {
            val decimal = tokens.next()
            val decimalPart = tokens.next()
            if (decimalPart.type != Token.Type.IDENTIFIER) throw ParserException("Expected decimal number", integerPart)
            NodeConstant(
                listOf(
                    NodeToken(integerPart), NodeToken(decimal), NodeToken(decimalPart)
                ), integerPart.text + decimal.text + decimalPart.text
            )
        } else {
            NodeConstant(listOf(NodeToken(integerPart)), integerPart.text)
        }
    }

    private fun parseRefMember(): NodeRefMember {
        val reference = parseReference()
        val memberMarker = tokens.next()
        if (memberMarker.type != Token.Type.MEMBER_MARKER) throw ParserException("Expected member marker", memberMarker)
        val member = parseVariable()
        return NodeRefMember(reference, NodeToken(memberMarker), member)
    }

    private fun parseRefAssignmentOrElement(): AstNode {
        val next = tokens.peek()
        if (next.type != Token.Type.REFERENCE_MARKER) throw ParserException("Expected reference marker", next)
        // peek(2) would be the reference identifier
        val nextNextNext = tokens.peek(3)
        return when (nextNextNext.type) {
            Token.Type.ASSIGNMENT -> parseReferenceAssignment()
            Token.Type.MEMBER_MARKER -> parseElement()
            else -> throw ParserException("Expected reference assignment or member", nextNextNext)
        }
    }

    private fun parseSpread(): NodeSpread {
        val spreadMarker = tokens.next()
        if (spreadMarker.type != Token.Type.SPREAD) throw ParserException("Expected spread marker", spreadMarker)
        val variable = parseVariableOrRefMember()

        val next = tokens.peek()
        val end = when (next.type) {
            Token.Type.FIELD_DELIMITER -> NodeToken(tokens.next())
            Token.Type.END_TYPE -> null

            else -> throw ParserException("Expected field delimiter or end type after spread", next)
        }
        return NodeSpread(NodeToken(spreadMarker), variable, end)
    }

    private fun parseTranslation(): NodeTranslation {
        val translationMarker = tokens.next()
        if (translationMarker.type != Token.Type.TRANSLATION_MARKER) throw ParserException(
            "Expected translation marker",
            translationMarker
        )

        val parts = mutableListOf<Token>()
        while (tokens.peek().type.let { it == Token.Type.IDENTIFIER || it == Token.Type.MEMBER_MARKER }) {
            parts.add(tokens.next())
        }

        return NodeTranslation(
            NodeToken(translationMarker),
            NodeConstant(parts.map { NodeToken(it) }, parts.joinToString("") { it.text })
        )
    }

    private fun parseVariableAssignmentOrElement(): AstNode {
        val next = tokens.peek()
        if (next.type != Token.Type.VARIABLE_MARKER) throw ParserException("Expected variable marker", next)
        // peek(2) would be the variable identifier
        val nextNextNext = tokens.peek(3)
        return when (nextNextNext.type) {
            Token.Type.ASSIGNMENT -> parseVariableAssignment()
            Token.Type.START_ELEMENT, Token.Type.SELECTOR_MARKER -> parseElement()
            else -> throw ParserException("Expected variable assignment or element", nextNextNext)
        }
    }
}