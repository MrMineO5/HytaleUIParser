package app.ultradev.hytaleuiparser.spec.command

import java.io.InputStream
import java.io.OutputStream

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

    fun write(os: OutputStream) {
        os.write(type.ordinal)
        var flags = 0
        if (selectorString != null) flags = flags or 1
        if (data != null) flags = flags or 2
        if (text != null) flags = flags or 4
        os.write(flags)
        if (selectorString != null) os.writeLengthPrefixedString(selectorString)
        if (data != null) os.writeLengthPrefixedString(data)
        if (text != null) os.writeLengthPrefixedString(text)
    }

    companion object {
        fun read(inputStream: InputStream): UICommand {
            val type = Type.entries[inputStream.readOrThrow()]
            val flags = inputStream.readOrThrow()

            fun maybeRead(flag: Int): String? {
                return if (flags and flag == flag) {
                    inputStream.readLengthPrefixedString()
                } else null
            }

            val selectorString = maybeRead(1)
            val data = maybeRead(2)
            val text = maybeRead(4)
            return UICommand(type, selectorString, data, text)
        }
    }
}
