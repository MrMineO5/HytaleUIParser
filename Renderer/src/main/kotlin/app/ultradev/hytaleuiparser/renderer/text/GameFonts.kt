package app.ultradev.hytaleuiparser.renderer.text

import java.awt.Font

object GameFonts {
    val default: Font = Font.createFont(Font.TRUETYPE_FONT, GameFonts.javaClass.getResourceAsStream("/fonts/NunitoSans-Regular.ttf"))
    val defaultBold: Font = Font.createFont(Font.TRUETYPE_FONT, GameFonts.javaClass.getResourceAsStream("/fonts/NunitoSans-ExtraBold.ttf"))
    val secondary: Font = Font.createFont(Font.TRUETYPE_FONT, GameFonts.javaClass.getResourceAsStream("/fonts/Lexend-Bold.ttf"))
    val mono: Font = Font.createFont(Font.TRUETYPE_FONT, GameFonts.javaClass.getResourceAsStream("/fonts/NotoMono-Regular.ttf"))
}