package app.ultradev.hytaleuiparser.token

import app.ultradev.hytaleuiparser.ast.NodeToken

internal object GeneratedTokens {
    private val REFERENCE_MARKER = Token(Token.Type.REFERENCE_MARKER, TokenSymbols.REFERENCE_MARKER.toString())
    private val ASSIGNMENT = Token(Token.Type.ASSIGNMENT, TokenSymbols.ASSIGNMENT_MARKER.toString())
    private val END_STATEMENT = Token(Token.Type.END_STATEMENT, TokenSymbols.END_STATEMENT.toString())
    private val START_ARRAY = Token(Token.Type.START_ARRAY, TokenSymbols.START_ARRAY.toString())
    private val END_ARRAY = Token(Token.Type.END_ARRAY, TokenSymbols.END_ARRAY.toString())

    fun referenceMarker() = NodeToken(REFERENCE_MARKER)
    fun assignment() = NodeToken(ASSIGNMENT)
    fun endStatement() = NodeToken(END_STATEMENT)
    fun arrayStart() = NodeToken(START_ARRAY)
    fun arrayEnd() = NodeToken(END_ARRAY)
}