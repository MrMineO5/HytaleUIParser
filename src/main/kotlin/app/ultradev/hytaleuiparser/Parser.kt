package app.ultradev.hytaleuiparser

import app.ultradev.hytaleuiparser.ast.*
import app.ultradev.hytaleuiparser.token.Token

class Parser(tokens: Iterator<Token>) {
    val tokens = TokenIterator(tokens, skipComments = true)
    val nodes = mutableListOf<AstNode>()

    var previous: AstNode? = null

    fun finish(): RootNode {
        parseCompletely()
        val root = RootNode(nodes)
        root.applyParent(root)
        return root
    }

    private fun parseCompletely() {
        while (tokens.hasNext()) {
            nodes.add(parseRoot())
        }
    }

    private fun parseRoot(): AstNode {
        val node = tokens.peek()

        return when (node.type) {
            Token.Type.VARIABLE -> parseVariableAssignmentOrElement()
            Token.Type.REFERENCE -> parseRefAssignmentOrElement()
            Token.Type.IDENTIFIER -> parseElement()

            else -> throw ParserException("Unsupported root node type found: ${node.type}", node)
        }
    }

    private fun parseVariableOrRefMember(): AstNode {
        val next = tokens.peek()
        return when (next.type) {
            Token.Type.VARIABLE -> parseVariable()
            Token.Type.REFERENCE -> parseRefMember()
            else -> throw ParserException("Expected variable or reference", next)
        }
    }

    private fun parseVariable(): NodeVariable {
        val reference = tokens.next()
        if (reference.type != Token.Type.VARIABLE) throw ParserException("Expected variable marker", reference)
        return NodeVariable(NodeToken(reference))
    }

    private fun parseIdentifier(): NodeIdentifier {
        val token = tokens.next()
        if (token.type != Token.Type.IDENTIFIER) throw ParserException("Expected identifier", token)
        return NodeIdentifier(NodeToken(token))
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
        val reference = tokens.next()
        if (reference.type != Token.Type.REFERENCE) throw ParserException("Expected reference marker", reference)
        return NodeReference(NodeToken(reference))
    }

    private fun parseReferenceAssignment(): NodeAssignReference {
        val reference = parseReference()
        val assignment = tokens.next()
        if (assignment.type != Token.Type.ASSIGNMENT) throw ParserException("Expected assignment operator", assignment)
        val filePath = parseQuotedStringConstant()
        val endStatement = parseEndStatement()
        return NodeAssignReference(reference, NodeToken(assignment), filePath, endStatement)
    }

    private fun parseEndStatement(): NodeToken {
        val endStatement = tokens.next()
        if (endStatement.type != Token.Type.END_STATEMENT) throw ParserException("Expected end statement", endStatement)
        return NodeToken(endStatement)
    }

    private fun parseQuotedStringConstant(): NodeConstant {
        val token = tokens.next()
        if (token.type != Token.Type.STRING) throw ParserException(
            "Expected quoted string constant", token
        )
        if (token.text.first() != '"' || token.text.last() != '"') throw ParserException(
            "Expected quoted string constant, found ${token.text}", token
        )
        return NodeConstant(listOf(NodeToken(token)), token.text.substring(1, token.text.length - 1))
    }

    private fun parseUnquotedStringConstant(): NodeConstant {
        val token = tokens.next()
        if (token.type != Token.Type.IDENTIFIER) throw ParserException(
            "Expected identifier constant", token
        )
        return NodeConstant(listOf(NodeToken(token)), token.text)
    }

    private fun parseVariableValue(): AstNode {
        val token = tokens.peek()

        val variable = when (token.type) {
            Token.Type.STRING -> parseQuotedStringConstant()
            Token.Type.NUMBER -> NodeConstant(tokens.next())
            Token.Type.SELECTOR -> parseColor()
            Token.Type.IDENTIFIER -> {
                val next = tokens.peek(2)
                when (next.type) {
                    Token.Type.START_ELEMENT, Token.Type.SELECTOR -> parseElement()
                    Token.Type.START_PARENTHESIS -> parseType()

                    Token.Type.FIELD_DELIMITER, Token.Type.END_STATEMENT, Token.Type.END_PARENTHESIS,
                    Token.Type.MATH_ADD, Token.Type.MATH_SUBTRACT, Token.Type.MATH_MULTIPLY, Token.Type.MATH_DIVIDE -> parseUnquotedStringConstant()

                    else -> throw ParserException("Unexpected token after identifier", next)
                }
            }

            Token.Type.VARIABLE -> parseVariable()
            Token.Type.REFERENCE -> parseRefMember()
            Token.Type.TRANSLATION_MARKER -> parseTranslation()
            Token.Type.START_PARENTHESIS -> {
                val afterStart = tokens.peek(2)
                val isType = when (afterStart.type) {
                    Token.Type.SPREAD -> true
                    Token.Type.END_PARENTHESIS -> true
                    Token.Type.IDENTIFIER -> tokens.peek(3).type == Token.Type.FIELD_MARKER
                    else -> false
                }
                if (isType) parseType() else parseMathParenthesis()
            }
            Token.Type.START_ARRAY -> parseArray()
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

            Token.Type.MEMBER_MARKER -> {
                var current = variable
                while (tokens.peek().type == Token.Type.MEMBER_MARKER) {
                    val memberMarker = tokens.next()
                    val member = parseIdentifier()
                    current = NodeMemberField(current, NodeToken(memberMarker), member)
                }
                current
            }

            else -> variable
        }
    }

    private fun parseArray(): NodeArray {
        val start = tokens.next()
        if (start.type != Token.Type.START_ARRAY) throw ParserException("Expected start array", start)

        val elements = mutableListOf<AstNode>()
        while (tokens.hasNext()) {
            val next = tokens.peek()
            if (next.type == Token.Type.END_ARRAY) {
                return NodeArray(NodeToken(start), elements, NodeToken(tokens.next()))
            }

            val value = parseVariableValue()
            elements.add(value)

            val afterValue = tokens.peek()
            when (afterValue.type) {
                Token.Type.FIELD_DELIMITER -> {
                    val delimiter = tokens.next()
                    elements.add(NodeToken(delimiter))
                }

                Token.Type.END_ARRAY -> return NodeArray(NodeToken(start), elements, NodeToken(tokens.next()))

                else -> throw ParserException("Expected array element delimiter or end array", afterValue)
            }
        }

        throw ParserException("Unclosed array", start)
    }

    private fun parseMathParenthesis(): AstNode {
        val start = tokens.next()
        if (start.type != Token.Type.START_PARENTHESIS) throw ParserException("Expected start parenthesis", start)
        val value = parseVariableValue()
        val end = tokens.next()
        if (end.type != Token.Type.END_PARENTHESIS) throw ParserException("Expected end parenthesis", end)
        return value
    }

    private fun parseType(): NodeType {
        var start = tokens.peek()

        var type: NodeIdentifier? = null
        if (start.type == Token.Type.IDENTIFIER) {
            type = parseIdentifier()
        }

        start = tokens.next()

        if (start.type != Token.Type.START_PARENTHESIS) throw ParserException("Expected start type", start)

        val children = mutableListOf<AstNode>()
        while (tokens.hasNext()) {
            val next = tokens.peek()

            when (next.type) {
                Token.Type.IDENTIFIER -> children.add(parseField())
                Token.Type.SPREAD -> children.add(parseSpread())
                Token.Type.REFERENCE -> children.add(parseRefMember())
                
                Token.Type.END_PARENTHESIS -> return NodeType(
                    type, NodeBody(NodeToken(start), children, NodeToken(tokens.next()))
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
            Token.Type.VARIABLE -> parseVariable()
            Token.Type.REFERENCE -> parseRefMember()
            else -> throw ParserException("Expected identifier or variable", ident)
        }

        var selector: NodeSelector? = null
        val next = tokens.peek()
        if (next.type == Token.Type.SELECTOR) {
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
                Token.Type.END_ELEMENT -> return NodeBody(NodeToken(start), children, NodeToken(tokens.next()))

                Token.Type.VARIABLE -> children.add(parseVariableAssignmentOrElement())
                Token.Type.REFERENCE -> children.add(parseRefAssignmentOrElement())
                Token.Type.SELECTOR -> children.add(parseSelectorElement())

                Token.Type.IDENTIFIER -> {
                    val nextNext = tokens.peek(2)
                    when (nextNext.type) {
                        Token.Type.FIELD_MARKER -> {
                            children.add(parseField())
                        }

                        Token.Type.START_ELEMENT, Token.Type.SELECTOR -> children.add(parseElement())

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
            Token.Type.END_PARENTHESIS -> null

            else -> throw ParserException("Expected field delimiter, end statement, or end type after field", next)
        }

        return NodeField(identifier, NodeToken(fieldMarker), value, end)
    }

    private fun parseSelector(): NodeSelector {
        val selector = tokens.next()
        if (selector.type != Token.Type.SELECTOR) throw ParserException("Expected selector marker", selector)
        return NodeSelector(NodeToken(selector))
    }

    private fun parseColor(): NodeColor {
        val color = tokens.next()
        if (color.type != Token.Type.SELECTOR) throw ParserException("Expected selector", color)

        var opacity: NodeOpacity? = null
        val next = tokens.peek()
        if (next.type == Token.Type.START_PARENTHESIS) {
            opacity = parseOpacity()
        }

        return NodeColor(NodeConstant(color), opacity)
    }

    private fun parseOpacity(): NodeOpacity {
        val start = tokens.next()
        if (start.type != Token.Type.START_PARENTHESIS) throw ParserException("Expected start type marker", start)

        val value = tokens.next()
        if (value.type != Token.Type.NUMBER) throw ParserException("Expected opacity value", value)

        val end = tokens.next()
        if (end.type != Token.Type.END_PARENTHESIS) throw ParserException("Expected end type marker", end)
        return NodeOpacity(NodeToken(start), NodeToken(end), NodeConstant(value))
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
        if (next.type != Token.Type.REFERENCE) throw ParserException("Expected reference marker", next)
        val nextNext = tokens.peek(2)
        return when (nextNext.type) {
            Token.Type.ASSIGNMENT -> parseReferenceAssignment()
            Token.Type.MEMBER_MARKER -> parseElement()
            else -> throw ParserException("Expected reference assignment or member", nextNext)
        }
    }

    private fun parseSpread(): NodeSpread {
        val spreadMarker = tokens.next()
        if (spreadMarker.type != Token.Type.SPREAD) throw ParserException("Expected spread marker", spreadMarker)
        val variable = parseVariableOrRefMember()

        val next = tokens.peek()
        val end = when (next.type) {
            Token.Type.FIELD_DELIMITER -> NodeToken(tokens.next())
            Token.Type.END_PARENTHESIS -> null

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
        if (next.type != Token.Type.VARIABLE) throw ParserException("Expected variable marker", next)
        val nextNext = safePeek(next, 2)
        return when (nextNext.type) {
            Token.Type.ASSIGNMENT -> parseVariableAssignment()
            Token.Type.START_ELEMENT, Token.Type.SELECTOR -> parseElement()
            else -> throw ParserException("Expected variable assignment or element", nextNext)
        }
    }

    private fun safePeek(current: Token, count: Int = 1): Token {
        if (!tokens.hasNextMany(count)) throw ParserException("Unexpected end of file", current)
        return tokens.peek(count)
    }
}
