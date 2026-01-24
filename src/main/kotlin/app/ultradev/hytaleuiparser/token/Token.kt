package app.ultradev.hytaleuiparser.token

data class Token(
    val type: Type,
    val text: String,

    val startLine: Int,
    val startColumn: Int
) {
    enum class Type {
        IDENTIFIER,
        VARIABLE_MARKER,
        REFERENCE_MARKER,
        ASSIGNMENT,
        MEMBER_MARKER,
        END_STATEMENT,
        SPREAD,
        START_ELEMENT,
        END_ELEMENT,
        STRING,
        START_TYPE,
        END_TYPE,
        SELECTOR_MARKER,
        FIELD_MARKER,
        FIELD_DELIMITER,
        COMMENT,

        MATH_ADD,
        MATH_SUBTRACT,

        TRANSLATION_MARKER,
    }
}