package app.ultradev.hytaleuiparser.spec.command

import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream

class TestCustomUIInfo {
    @Test
    fun testSerializeDeserialize() {
        val command = UICommand(UICommand.Type.Append, null, null, "Pages/BarterPage.ui")
        val info = CustomUIInfo("BarterPage", listOf(command))

        val os = ByteArrayOutputStream()
        info.write(os)

        val input = os.toByteArray()
        val read = CustomUIInfo.read(input.inputStream())

        assert(info == read)
    }
}