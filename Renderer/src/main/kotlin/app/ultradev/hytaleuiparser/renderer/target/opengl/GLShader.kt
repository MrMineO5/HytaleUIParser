package app.ultradev.hytaleuiparser.renderer.target.opengl

enum class GLShader(val mainTex: Int) {
    FONT(1),
    NINE_SLICE(0),
    IMAGE(0),
    FILL(-1),
}