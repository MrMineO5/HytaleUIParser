package app.ultradev.hytaleuiparser.warning

import app.ultradev.hytaleuiparser.ast.AstNode

class WarnLeftCenterWrapWidthTooLarge(
    override val node: AstNode
) : ValidatorWarning {
    override val message: String
        get() = "LeftCenterWrap child is too wide"

    override val description: String
        get() = "When the width is larger than parent width minus one, the layout positions the element as if it had a width of zero"
}
