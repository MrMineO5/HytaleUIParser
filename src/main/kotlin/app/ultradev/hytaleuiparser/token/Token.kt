package app.ultradev.hytaleuiparser.token

data class Token(
    val type: Type,
    val text: String,

    val startLine: Int,
    val startColumn: Int
) {
    enum class Type {
        IDENTIFIER,
        VARIABLE,
        FILE_REF,
        ASSIGN,
        MEMBER,
        END_STATEMENT,
        SPREAD,
        START_ELEMENT,
        END_ELEMENT,
        STRING,
        START_TYPE,
        END_TYPE,
        SELECTOR,
        FIELD
    }
}