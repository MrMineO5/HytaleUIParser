package app.ultradev.hytaleuiparser.ast

sealed interface VariableReference : VariableValue {
    override val resolvedValue: VariableValue?

    override fun deepResolve(): VariableValue? {
        var curr = resolvedValue
        while (curr is VariableReference) curr = curr.resolvedValue
        return curr
    }
}

