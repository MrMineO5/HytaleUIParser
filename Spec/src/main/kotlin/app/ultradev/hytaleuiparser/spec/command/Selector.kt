package app.ultradev.hytaleuiparser.spec.command

data class Selector(
    val parts: List<Part>
) {
    sealed interface Part {
        fun toSelectorString(): String
    }
    data class Named(val name: String) : Part {
        override fun toSelectorString() = " $name"
    }
    data class Index(val index: Int) : Part {
        override fun toSelectorString() = "[$index]"
    }
    data class Field(val path: List<String>) : Part {
        override fun toSelectorString() = ".${path.joinToString(".")}"
    }

    fun toSelectorString() = parts.joinToString("") { it.toSelectorString() }.trim()
    fun firstPart() = parts.firstOrNull()
    fun pop() = Selector(parts.drop(1))

    companion object {
        val EMPTY = Selector(emptyList())

        @JvmStatic
        fun main(args: Array<String>) {
            val selStr = "#A #test[123] #B.field"
            val selector = parse(selStr)
            println("Parsing: $selStr")
            println(selector)
            println("Selector string: ${selector.toSelectorString()}")
        }

        /**
         * Parses a Hytale selector string into a list of selector parts.
         *
         * This is *NOT* a Validator, Hytale's selector syntax is very strict, e.g. you must have one and exactly one
         * space between selector "#A #B", neither "#A#B" nor "#A  #B" are valid.
         * We do not currently check this
         */
        fun parse(selectorString: String): Selector {
            if (selectorString.isBlank()) return EMPTY

            val parts = mutableListOf<Part>()
            var index = 0
            while (index < selectorString.length) {
                when (selectorString[index]) {
                    '#' -> {
                        index++
                        val mark = index
                        while (index < selectorString.length && selectorString[index].isLetterOrDigit()) index++
                        val selector = "#" + selectorString.substring(mark, index)
                        parts.add(Named(selector))
                    }

                    '[' -> {
                        index++
                        val mark = index
                        while (index < selectorString.length && selectorString[index].isDigit()) index++

                        val selectedIndex = selectorString.substring(mark, index)

                        if (index >= selectorString.length || selectorString[index] != ']') error("Expected closing indexing operator ']' in selector")
                        index++

                        parts.add(Index(selectedIndex.toInt()))
                    }

                    '.' -> {
                        val rest = selectorString.substring(index + 1)
                        val path = rest.split('.')
                        if (path.any { p -> p.any { !it.isLetterOrDigit() } }) error("Field path contains invalid characters")
                        parts.add(Field(path))
                        break
                    }

                    ' ' -> index++

                    else -> error("Found unexpected character in selector: ${selectorString[index]}")
                }
            }
            return Selector(parts)
        }
    }
}