package app.ultradev.hytaleuiparser.renderer.command

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
        fun writeVarInt(value: Int) {
            var rem = value
            while (rem != 0) {
                val next = rem shr 7
                val value = rem and 0x7F or (if (next == 0) 0 else 0x80)
                os.write(value)
                rem = next
            }
        }

        os.write(type.ordinal)
        var flags = 0
        if (selectorString != null) flags = flags or 1
        if (data != null) flags = flags or 2
        if (text != null) flags = flags or 4
        os.write(flags)
        if (selectorString != null) {
            writeVarInt(selectorString.length)
            os.write(selectorString.toByteArray())
        }
        if (data != null) {
            writeVarInt(data.length)
            os.write(data.toByteArray())
        }
        if (text != null) {
            writeVarInt(text.length)
            os.write(text.toByteArray())
        }
    }

    companion object {
        fun read(inputStream: InputStream): UICommand {
            fun readVarInt(): Int {
                var value = 0
                var shift = 0
                while (true) {
                    val next = inputStream.read()
                    value = value or ((next and 0x7F) shl shift)
                    if (next and 0x80 == 0) return value
                    shift += 7
                }
            }

            val type = Type.entries[inputStream.read()]
            val flags = inputStream.read()
            val selectorString = if (flags and 1 == 1) String(inputStream.readNBytes(readVarInt())) else null
            val data = if (flags and 2 == 2) String(inputStream.readNBytes(readVarInt())) else null
            val text = if (flags and 4 == 4) String(inputStream.readNBytes(readVarInt())) else null
            return UICommand(type, selectorString, data, text)
        }
    }
}
