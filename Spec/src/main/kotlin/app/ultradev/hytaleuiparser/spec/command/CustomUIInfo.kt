package app.ultradev.hytaleuiparser.spec.command

import java.io.InputStream
import java.io.OutputStream

data class CustomUIInfo(
    val key: String,
    val commands: List<UICommand>,
    val type: Type = Type.Page,
    val clear: Boolean = true,
) {
    enum class Type {
        Page,
        Hud,
        AnchorUI
    }

    fun write(os: OutputStream) {
        val flag = type.ordinal or if (clear) 0x80 else 0
        os.write(flag)

        os.writeLengthPrefixedString(key)
        os.writeVarInt(commands.size)
        commands.forEach { it.write(os) }
    }

    companion object {
        fun read(inputStream: InputStream): CustomUIInfo {
            val flag = inputStream.readOrThrow()
            val type = Type.entries[flag and 0x7]
            val clear = flag and 0x80 != 0

            val className = inputStream.readLengthPrefixedString()

            val commandCount = inputStream.readVarInt()
            val commands = (1..commandCount).map { UICommand.read(inputStream) }

            return CustomUIInfo(className, commands, type, clear)
        }
    }
}