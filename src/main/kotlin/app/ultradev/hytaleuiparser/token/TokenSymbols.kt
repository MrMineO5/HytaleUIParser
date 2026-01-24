package app.ultradev.hytaleuiparser.token

object TokenSymbols {
    val FILE_REF_MARKER = '$'
    val VARIABLE_MARKER = '@'
    val ASSIGNMENT_MARKER = '='
    val MEMBER_MARKER = '.'
    val FIELD_MARKER = ':'
    val SELECTOR_OR_COLOR = '#';

    val END_STATEMENT = ';'

    val START_ELEMENT = '{'
    val END_ELEMENT = '}'

    val START_TYPE = '('
    val END_TYPE = ')'


    val TOKEN_MAP = mapOf(
        FILE_REF_MARKER to Token.Type.FILE_REF,
        VARIABLE_MARKER to Token.Type.VARIABLE,
        ASSIGNMENT_MARKER to Token.Type.ASSIGN,
        END_STATEMENT to Token.Type.END_STATEMENT,
        START_ELEMENT to Token.Type.START_ELEMENT,
        END_ELEMENT to Token.Type.END_ELEMENT,
        START_TYPE to Token.Type.START_TYPE,
        END_TYPE to Token.Type.END_TYPE,
        FIELD_MARKER to Token.Type.FIELD,
        SELECTOR_OR_COLOR to Token.Type.SELECTOR
    )
}