package app.ultradev.hytaleuiparser.token

object TokenSymbols {
    val REFERENCE_MARKER = '$'
    val VARIABLE_MARKER = '@'
    val ASSIGNMENT_MARKER = '='
    val MEMBER_MARKER = '.'
    val TRANSLATION_MARKER = '%'
    val FIELD_MARKER = ':'
    val FIELD_DELIMITER = ','

    val END_STATEMENT = ';'

    val START_ELEMENT = '{'
    val END_ELEMENT = '}'

    val START_TYPE = '('
    val END_TYPE = ')'


    val MATH_ADD = '+'
    val MATH_SUBTRACT = '-'
    val MATH_MULTIPLY = '*'
    val MATH_DIVIDE = '/'

    val START_ARRAY = '['
    
    val END_ARRAY = ']'
    
    val TOKEN_MAP = mapOf(
        REFERENCE_MARKER to Token.Type.REFERENCE_MARKER,
        VARIABLE_MARKER to Token.Type.VARIABLE_MARKER,
        ASSIGNMENT_MARKER to Token.Type.ASSIGNMENT,
        END_STATEMENT to Token.Type.END_STATEMENT,
        START_ELEMENT to Token.Type.START_ELEMENT,
        END_ELEMENT to Token.Type.END_ELEMENT,
        START_TYPE to Token.Type.START_PARENTHESIS,
        END_TYPE to Token.Type.END_PARENTHESIS,
        FIELD_MARKER to Token.Type.FIELD_MARKER,
        FIELD_DELIMITER to Token.Type.FIELD_DELIMITER,
        
        START_ARRAY to Token.Type.START_ARRAY,
        END_ARRAY to Token.Type.END_ARRAY,
        
        MATH_ADD to Token.Type.MATH_ADD,
        MATH_SUBTRACT to Token.Type.MATH_SUBTRACT,
        MATH_MULTIPLY to Token.Type.MATH_MULTIPLY,
        MATH_DIVIDE to Token.Type.MATH_DIVIDE,

        TRANSLATION_MARKER to Token.Type.TRANSLATION_MARKER
    )
}