package app.ultradev.hytaleuiparser.token

data class Token(
    val type: Type,
    val text: String,

    val startLine: Int = -1,
    val startColumn: Int = -1,
    val startOffset: Int = -1,
) {
    enum class Type {
        IDENTIFIER,
        VARIABLE,
        REFERENCE,
        ASSIGNMENT,
        MEMBER_MARKER,
        END_STATEMENT,
        SPREAD,
        START_ELEMENT,
        END_ELEMENT,
        STRING,
        NUMBER,
        START_PARENTHESIS,
        END_PARENTHESIS,
        SELECTOR,
        FIELD_MARKER,
        FIELD_DELIMITER,
        COMMENT,
        
        START_ARRAY,
        END_ARRAY,

        MATH_ADD,
        MATH_SUBTRACT,
        MATH_MULTIPLY,
        MATH_DIVIDE,

        TRANSLATION_MARKER,

        WHITESPACE,
        UNKNOWN,
    }
}