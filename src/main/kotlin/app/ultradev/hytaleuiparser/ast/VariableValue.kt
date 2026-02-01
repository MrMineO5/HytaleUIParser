package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.validation.types.TypeType

sealed interface VariableValue {
    val asAstNode: AstNode get() = this as AstNode
    val resolvedValue: VariableValue? get() = this

    fun deepResolve(): VariableValue? = resolvedValue

    val resolvedTypes: Set<TypeType> get() = deepResolve()!!.resolvedTypes // TODO: Technically resolution shouldn't be nullable I guess?
}
