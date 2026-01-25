package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.validation.Scope

sealed interface VariableValue {
    fun _initResolvedScope(scope: Scope) {}
}
