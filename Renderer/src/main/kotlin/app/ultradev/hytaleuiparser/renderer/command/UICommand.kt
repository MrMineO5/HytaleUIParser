package app.ultradev.hytaleuiparser.renderer.command

data class UICommand(
    val type: Type,
    val selectorString: String? = null,
    val data: String? = null,
    val text: String? = null,
) {
    val selector by lazy { Selector.parse(selectorString ?: "") }

    enum class Type {
        Append,
        AppendInline,
        InsertBefore,
        InsertBeforeInline,
        Remove,
        Set,
        Clear,
    }
}
