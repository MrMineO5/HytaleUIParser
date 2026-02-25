package app.ultradev.hytaleuiparser.spec.command

import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream

class TestUICommand {
    @Test
    fun testSerializeDeserialize() {
        val command = UICommand(UICommand.Type.Append, null, null, "Pages/BarterPage.ui")

        val os = ByteArrayOutputStream()
        command.write(os)

        val input = os.toByteArray()
        val read = UICommand.read(input.inputStream())

        assert(command == read)
    }
}