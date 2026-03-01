package app.ultradev.hytaleuiparser.renderer.text

enum class TextRenderMode {
    TTF,
    MSDF,
    ;

    companion object {
        var active = MSDF
    }
}