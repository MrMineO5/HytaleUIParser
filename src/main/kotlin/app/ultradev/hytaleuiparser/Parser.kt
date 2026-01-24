package app.ultradev.hytaleuiparser

import java.io.Reader

class Parser(
    val reader: Reader
) {
    init {
        reader.mark(1)
        val next = reader.read()
        reader.reset()
    }
}