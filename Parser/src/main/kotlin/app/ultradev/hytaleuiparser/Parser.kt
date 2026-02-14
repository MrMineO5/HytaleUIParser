package app.ultradev.hytaleuiparser

import app.ultradev.hytaleuiparser.ast.*
import app.ultradev.hytaleuiparser.token.Token

class Parser(tokens: Iterator<app.ultradev.hytaleuiparser.token.Token>) {
    val tokens = _root_ide_package_.app.ultradev.hytaleuiparser.TokenIterator(tokens, skipComments = true)
    val nodes = mutableListOf<app.ultradev.hytaleuiparser.ast.AstNode>()

    val parserErrors = mutableListOf<app.ultradev.hytaleuiparser.ParserError>()

    fun parserError(message: String, token: app.ultradev.hytaleuiparser.token.Token) {
        parserErrors.add(_root_ide_package_.app.ultradev.hytaleuiparser.ParserError(message, token))
    }

    private fun recoverable(block: () -> app.ultradev.hytaleuiparser.ast.AstNode?): app.ultradev.hytaleuiparser.ast.AstNode? {
        return try {
            block()
        } catch (e: app.ultradev.hytaleuiparser.ParserException) {
            parserError(e.message ?: "Unknown error", e.token)
            null
        }
    }

    fun finish(): app.ultradev.hytaleuiparser.ast.RootNode {
        parseCompletely()
        val root = _root_ide_package_.app.ultradev.hytaleuiparser.ast.RootNode(nodes)
        root.applyParent(root)
        return root
    }

    private fun parseCompletely() {
        while (tokens.peek().type != _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.EOF) {
            nodes.add(parseRoot())
        }
    }

    private fun parseRoot(): app.ultradev.hytaleuiparser.ast.AstNode {
        val node = tokens.peek()

        return when (node.type) {
            _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.VARIABLE -> parseVariableAssignmentOrElement()
            _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.REFERENCE -> parseRefAssignmentOrElement()
            _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.IDENTIFIER -> parseElement()

            else -> throw _root_ide_package_.app.ultradev.hytaleuiparser.ParserException(
                "Unsupported root node type found: ${node.type}",
                node
            )
        }
    }

    private fun parseVariable(): app.ultradev.hytaleuiparser.ast.NodeVariable {
        val reference = tokens.peek()
        if (reference.type != _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.VARIABLE) throw _root_ide_package_.app.ultradev.hytaleuiparser.ParserException(
            "Expected variable marker",
            reference
        )
        return _root_ide_package_.app.ultradev.hytaleuiparser.ast.NodeVariable(listOf(nextToken()))
    }

    private fun parseIdentifier(): app.ultradev.hytaleuiparser.ast.NodeIdentifier {
        val token = tokens.peek()
        if (token.type != _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.IDENTIFIER) throw _root_ide_package_.app.ultradev.hytaleuiparser.ParserException(
            "Expected identifier",
            token
        )
        return _root_ide_package_.app.ultradev.hytaleuiparser.ast.NodeIdentifier(listOf(nextToken()))
    }

    private fun parseVariableAssignment(): app.ultradev.hytaleuiparser.ast.NodeAssignVariable {
        val variable = parseVariable()
        val assignment = tokens.next()
        if (assignment.type != _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.ASSIGNMENT) throw _root_ide_package_.app.ultradev.hytaleuiparser.ParserException(
            "Expected assignment operator",
            assignment
        )
        val value = parseVariableValue()
        if (value !is app.ultradev.hytaleuiparser.ast.VariableValue) throw _root_ide_package_.app.ultradev.hytaleuiparser.ParserException(
            "Expected variable value after assignment operator", assignment
        ) // TODO: AstNode should always have a token?
        val end = parseEndStatement()
        return _root_ide_package_.app.ultradev.hytaleuiparser.ast.NodeAssignVariable(
            _root_ide_package_.app.ultradev.hytaleuiparser.listOfInsertMissing(
                variable,
                _root_ide_package_.app.ultradev.hytaleuiparser.ast.NodeToken(assignment),
                value,
                end
            )
        )
    }

    private fun parseReference(): app.ultradev.hytaleuiparser.ast.NodeReference {
        val reference = tokens.next()
        if (reference.type != _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.REFERENCE) throw _root_ide_package_.app.ultradev.hytaleuiparser.ParserException(
            "Expected reference marker",
            reference
        )
        return _root_ide_package_.app.ultradev.hytaleuiparser.ast.NodeReference(
            listOf(
                _root_ide_package_.app.ultradev.hytaleuiparser.ast.NodeToken(
                    reference
                )
            )
        )
    }

    private fun parseReferenceAssignment(): app.ultradev.hytaleuiparser.ast.NodeAssignReference {
        val reference = parseReference()
        val assignment = tokens.next()
        if (assignment.type != _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.ASSIGNMENT) throw _root_ide_package_.app.ultradev.hytaleuiparser.ParserException(
            "Expected assignment operator",
            assignment
        )
        val filePath = parseQuotedStringConstant()
        val endStatement = parseEndStatement()
        return _root_ide_package_.app.ultradev.hytaleuiparser.ast.NodeAssignReference(
            _root_ide_package_.app.ultradev.hytaleuiparser.listOfInsertMissing(
                reference,
                _root_ide_package_.app.ultradev.hytaleuiparser.ast.NodeToken(assignment),
                filePath,
                endStatement
            )
        )
    }

    private fun parseEndStatement(): app.ultradev.hytaleuiparser.ast.NodeToken? {
        if (tokens.peek().type != _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.END_STATEMENT) {
            parserError("Expected end statement", tokens.peek())
            return null
        }
        val endStatement = tokens.next()
        return _root_ide_package_.app.ultradev.hytaleuiparser.ast.NodeToken(endStatement)
    }

    private fun parseQuotedStringConstant(): app.ultradev.hytaleuiparser.ast.NodeConstant {
        val token = tokens.next()
        if (token.type != _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.STRING) throw _root_ide_package_.app.ultradev.hytaleuiparser.ParserException(
            "Expected quoted string constant", token
        )
        if (token.text.first() != '"' || token.text.last() != '"') throw _root_ide_package_.app.ultradev.hytaleuiparser.ParserException(
            "Expected quoted string constant, found ${token.text}", token
        )
        return _root_ide_package_.app.ultradev.hytaleuiparser.ast.NodeConstant(
            listOf(
                _root_ide_package_.app.ultradev.hytaleuiparser.ast.NodeToken(
                    token
                )
            )
        )
    }

    private fun parseUnquotedStringConstant(): app.ultradev.hytaleuiparser.ast.NodeConstant {
        val token = tokens.next()
        if (token.type != _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.IDENTIFIER) throw _root_ide_package_.app.ultradev.hytaleuiparser.ParserException(
            "Expected identifier constant", token
        )
        return _root_ide_package_.app.ultradev.hytaleuiparser.ast.NodeConstant(
            listOf(
                _root_ide_package_.app.ultradev.hytaleuiparser.ast.NodeToken(
                    token
                )
            )
        )
    }

    private fun parseVariableReference(): app.ultradev.hytaleuiparser.ast.AstNode {
        val token = tokens.peek()
        val variable = when (token.type) {
            _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.VARIABLE -> parseVariable()
            _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.REFERENCE -> parseRefMember()
            else -> throw _root_ide_package_.app.ultradev.hytaleuiparser.ParserException(
                "Expected variable reference",
                token
            )
        }
        var curr: app.ultradev.hytaleuiparser.ast.AstNode = variable
        while (tokens.peek().type == _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.MEMBER_MARKER) {
            val marker = tokens.next()
            val member = recoverable { parseIdentifier() }
            val valid = member != null
            curr = _root_ide_package_.app.ultradev.hytaleuiparser.ast.NodeMemberField(
                _root_ide_package_.app.ultradev.hytaleuiparser.listOfInsertMissing(
                    curr,
                    _root_ide_package_.app.ultradev.hytaleuiparser.ast.NodeToken(marker),
                    member
                ), valid
            )
        }
        return curr
    }

    private fun parseVariableValue(): app.ultradev.hytaleuiparser.ast.AstNode {
        val token = tokens.peek()

        val variable = when (token.type) {
            _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.STRING -> parseQuotedStringConstant()
            _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.NUMBER -> _root_ide_package_.app.ultradev.hytaleuiparser.ast.NodeConstant(
                listOf(_root_ide_package_.app.ultradev.hytaleuiparser.ast.NodeToken(tokens.next()))
            )
            _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.SELECTOR -> parseColor()
            _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.IDENTIFIER -> {
                val next = tokens.peek(2)
                when (next.type) {
                    _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.START_ELEMENT, _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.SELECTOR -> parseElement()
                    _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.START_PARENTHESIS -> parseType()

                    _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.FIELD_DELIMITER, _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.END_STATEMENT, _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.END_PARENTHESIS,
                    _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.MATH_ADD, _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.MATH_SUBTRACT, _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.MATH_MULTIPLY, _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.MATH_DIVIDE -> parseUnquotedStringConstant()

                    else -> throw _root_ide_package_.app.ultradev.hytaleuiparser.ParserException(
                        "Unexpected token after identifier",
                        next
                    )
                }
            }

            _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.VARIABLE, _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.REFERENCE -> parseVariableReference()
            _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.TRANSLATION_MARKER -> parseTranslation()
            _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.START_PARENTHESIS -> {
                val afterStart = tokens.peek(2)
                val isType = when (afterStart.type) {
                    _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.SPREAD -> true
                    _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.END_PARENTHESIS -> true
                    _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.IDENTIFIER -> tokens.peek(3).type == _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.FIELD_MARKER
                    else -> false
                }
                if (isType) parseType() else parseMathParenthesis()
            }

            _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.START_ARRAY -> parseArray()
            _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.MATH_SUBTRACT -> {
                val minus = tokens.next()
                val value = parseVariableValue()
                _root_ide_package_.app.ultradev.hytaleuiparser.ast.NodeNegate(
                    listOf(
                        _root_ide_package_.app.ultradev.hytaleuiparser.ast.NodeToken(
                            minus
                        ), value
                    )
                )
            }

            else -> throw _root_ide_package_.app.ultradev.hytaleuiparser.ParserException(
                "Expected a variable value",
                token
            )
        }

        if (!tokens.hasNext()) return variable

        val next = tokens.peek()

        return when (next.type) {
            _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.MATH_ADD, _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.MATH_SUBTRACT, _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.MATH_MULTIPLY, _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.MATH_DIVIDE -> {
                val mathOperator = tokens.next()
                val right = parseVariableValue()
                if (right is app.ultradev.hytaleuiparser.ast.NodeMathOperation) {
                    _root_ide_package_.app.ultradev.hytaleuiparser.ast.NodeMathOperation(
                        _root_ide_package_.app.ultradev.hytaleuiparser.listOfInsertMissing(
                            _root_ide_package_.app.ultradev.hytaleuiparser.ast.NodeMathOperation(
                                _root_ide_package_.app.ultradev.hytaleuiparser.listOfInsertMissing(
                                    variable,
                                    _root_ide_package_.app.ultradev.hytaleuiparser.ast.NodeToken(mathOperator),
                                    right.param1
                                )
                            ),
                            right.operator, right.param2
                        )
                    )
                } else {
                    _root_ide_package_.app.ultradev.hytaleuiparser.ast.NodeMathOperation(
                        listOf(
                            variable,
                            _root_ide_package_.app.ultradev.hytaleuiparser.ast.NodeToken(mathOperator),
                            right
                        )
                    )
                }
            }

            _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.MEMBER_MARKER -> {
                var current = variable
                while (tokens.peek().type == _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.MEMBER_MARKER) {
                    val memberMarker = tokens.next()
                    val member = parseIdentifier()
                    current = _root_ide_package_.app.ultradev.hytaleuiparser.ast.NodeMemberField(
                        listOf(
                            current,
                            _root_ide_package_.app.ultradev.hytaleuiparser.ast.NodeToken(memberMarker),
                            member
                        )
                    )
                }
                current
            }

            else -> variable
        }
    }

    private fun parseArray(): app.ultradev.hytaleuiparser.ast.NodeArray {
        val start = tokens.next()
        if (start.type != _root_ide_package_.app.ultradev.hytaleuiparser.token.Token.Type.START_ARRAY) throw _root_ide_package_.app.ultradev.hytaleuiparser.ParserException(
            "Expected start array",
            start
        )

        val children = mutableListOf<app.ultradev.hytaleuiparser.ast.AstNode>(
            _root_ide_package_.app.ultradev.hytaleuiparser.ast.NodeToken(
                start
            )
        )
        while (tokens.hasNext()) {
            val next = tokens.peek()
            if (next.type == Token.Type.END_ARRAY) {
                children.add(NodeToken(tokens.next()))
                return NodeArray(children)
            }

            val value = parseVariableValue()
            children.add(value)

            val afterValue = tokens.peek()
            when (afterValue.type) {
                Token.Type.FIELD_DELIMITER -> {
                    val delimiter = tokens.next()
                    children.add(NodeToken(delimiter))
                }

                Token.Type.END_ARRAY -> {
                    children.add(NodeToken(tokens.next()))
                    return NodeArray(children)
                }

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

        val children = mutableListOf<AstNode>(NodeToken(start))
        while (tokens.hasNext()) {
            val next = tokens.peek()

            when (next.type) {
                Token.Type.IDENTIFIER -> children.add(parseField())
                Token.Type.SPREAD -> children.add(parseSpread())
                Token.Type.REFERENCE -> children.add(parseRefMember())

                Token.Type.END_PARENTHESIS -> {
                    children.add(NodeToken(tokens.next()))
                    val body = NodeBody(children)
                    return if (type != null) {
                        NodeIdentifiedType(listOf(type, body))
                    } else {
                        NodeType(listOf(body))
                    }
                }

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
        return if (selector == null) {
            NodeElement(listOf(identifier, body))
        } else {
            NodeElementWithSelector(listOf(identifier, selector, body))
        }
    }

    private fun parseSelectorElement(): NodeSelectorElement {
        val selector = parseSelector()
        val body = parseElementBody()
        return NodeSelectorElement(listOf(selector, body))
    }

    private fun parseElementBody(): NodeBody {
        val start = tokens.next()
        if (start.type != Token.Type.START_ELEMENT) throw ParserException("Expected start element", start)

        val children = mutableListOf<AstNode>(NodeToken(start))

        while (tokens.hasNext()) {
            val next = tokens.peek()

            when (next.type) {
                Token.Type.END_ELEMENT -> {
                    children.add(NodeToken(tokens.next()))
                    return NodeBody(children)
                }

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

        return NodeField(listOfInsertMissing(identifier, NodeToken(fieldMarker), value, end))
    }

    private fun parseSelector(): NodeSelector {
        val selector = tokens.next()
        if (selector.type != Token.Type.SELECTOR) throw ParserException("Expected selector marker", selector)
        return NodeSelector(listOf(NodeToken(selector)))
    }

    private fun parseColor(): NodeColor {
        val color = tokens.next()
        if (color.type != Token.Type.SELECTOR) throw ParserException("Expected selector", color)

        var opacity: NodeOpacity? = null
        val next = tokens.peek()
        if (next.type == Token.Type.START_PARENTHESIS) {
            opacity = parseOpacity()
        }

        return NodeColor(
            listOfInsertMissing(
                NodeConstant(
                    listOf(NodeToken(color))
                ),
                opacity
            )
        )
    }

    private fun parseOpacity(): NodeOpacity {
        val start = tokens.next()
        if (start.type != Token.Type.START_PARENTHESIS) throw ParserException("Expected start type marker", start)

        val value = tokens.next()
        if (value.type != Token.Type.NUMBER) throw ParserException("Expected opacity value", value)

        val end = tokens.next()
        if (end.type != Token.Type.END_PARENTHESIS) throw ParserException("Expected end type marker", end)
        return NodeOpacity(listOf(NodeToken(start), NodeConstant(listOf(NodeToken(value))), NodeToken(end)))
    }

    private fun parseRefMember(): NodeRefMember {
        val reference = parseReference()
        val memberMarker = tokens.next()
        if (memberMarker.type != Token.Type.MEMBER_MARKER) throw ParserException("Expected member marker", memberMarker)
        val member = recoverable { parseVariable() }
        return NodeRefMember(listOfInsertMissing(reference, NodeToken(memberMarker), member))
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
        val variable = recoverable { parseVariableReference() }

        val next = tokens.peek()
        val end = when (next.type) {
            Token.Type.FIELD_DELIMITER -> NodeToken(tokens.next())
            Token.Type.END_PARENTHESIS -> null

            else -> throw ParserException("Expected field delimiter or end type after spread", next)
        }
        return NodeSpread(listOfInsertMissing(NodeToken(spreadMarker), variable, end))
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
            listOf(
                NodeToken(translationMarker),
                NodeConstant(parts.map { NodeToken(it) })
            )
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
        return optionalPeek(count) ?: throw ParserException("Unexpected end of file", current)
    }
    private fun optionalPeek(count: Int = 1): Token? {
        if (!tokens.hasNextMany(count)) return null
        return tokens.peek(count)
    }

    private fun nextToken(): NodeToken {
        return NodeToken(tokens.next())
    }
}
