package app.ultradev.hytaleuiparser.renderer.text.msdf

import kotlinx.serialization.json.Json
import javax.imageio.ImageIO

object MSDFFonts {
    private val json = Json {
        ignoreUnknownKeys = true
    }

    private fun classPathFont(name: String): MSDFRenderer {
        val desc = javaClass.getResourceAsStream("/fonts/msdf/$name.json")!!.reader().readText()
        val definition = json.decodeFromString<MSDFDefinition>(desc)
        val atlas = ImageIO.read(javaClass.getResourceAsStream("/fonts/msdf/$name.png")!!)
        return MSDFRenderer(atlas, definition)
    }

    val default = classPathFont("NunitoSans-Medium")
    val defaultBold = classPathFont("NunitoSans-ExtraBold")

    val secondary = classPathFont("Lexend-Bold")
    val mono = classPathFont("NotoMono-Regular")
}