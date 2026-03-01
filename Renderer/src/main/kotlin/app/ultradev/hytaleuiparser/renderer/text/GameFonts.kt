package app.ultradev.hytaleuiparser.renderer.text

import java.awt.Font

object GameFonts {
    private fun classPathFont(name: String) = Font.createFont(Font.TRUETYPE_FONT, GameFonts.javaClass.getResourceAsStream("/fonts/$name.ttf"))

    val default: Font = classPathFont("NunitoSans-Medium")
    val defaultBold: Font = classPathFont("NunitoSans-ExtraBold")

    val secondary: Font = classPathFont("Lexend-Bold")
    val mono: Font = classPathFont("NotoMono-Regular")
}