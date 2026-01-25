package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.validation.Scope

sealed interface VariableReference : VariableValue {
    val resolvedScope: Scope
}
