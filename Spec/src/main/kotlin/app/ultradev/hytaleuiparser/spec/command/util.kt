package app.ultradev.hytaleuiparser.spec.command

import java.io.EOFException
import java.io.InputStream
import java.io.OutputStream

fun OutputStream.writeVarInt(value: Int) {
    var rem = value
    while (rem != 0) {
        val next = rem shr 7
        val value = rem and 0x7F or (if (next == 0) 0 else 0x80)
        this.write(value)
        rem = next
    }
}
fun OutputStream.writeLengthPrefixedString(value: String) {
    this.writeVarInt(value.length)
    this.write(value.toByteArray())
}

fun InputStream.readVarInt(): Int {
    var value = 0
    var shift = 0
    while (true) {
        val next = this.readOrThrow()
        value = value or ((next and 0x7F) shl shift)
        if (next and 0x80 == 0) return value
        shift += 7
    }
}
fun InputStream.readLengthPrefixedString(): String {
    val length = this.readVarInt()
    val bytes = this.readNBytes(length)
    if (bytes.size < length) throw EOFException("EOF reached while reading string")
    return String(bytes)
}

fun InputStream.readOrThrow(): Int {
    val value = this.read()
    if (value == -1) throw EOFException()
    return value
}
