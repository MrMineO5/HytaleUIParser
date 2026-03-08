package app.ultradev.hytaleuiparser.renderer.target.opengl

import app.ultradev.hytaleuiparser.renderer.RenderBox
import app.ultradev.hytaleuiparser.renderer.text.msdf.MSDFRenderer
import com.jogamp.opengl.GL
import java.awt.Color
import java.awt.image.BufferedImage

class GLRenderer(
    val stateManager: GLStateManager
) {
    private fun drawQuad(x: Float, y: Float, width: Float, height: Float) {
        stateManager.setUniform4f("uRect", x, y, width, height)
        stateManager.gl.glDrawArrays(GL.GL_TRIANGLE_STRIP, 0, 4)
    }

    private fun drawGlyph(font: MSDFRenderer, x: Float, y: Float, size: Float, glyph: Char): Float {
        val glyph = font.lookupTable[glyph] ?: return 0f
        val scale = size / font.definition.metrics.emSize
        if (glyph.planeBounds == null || glyph.atlasBounds == null) return (glyph.advance * scale).toFloat()

        stateManager.setUniform4f(
            "uFontPos",
            glyph.atlasBounds.left.toFloat() / font.definition.atlas.width,
            glyph.atlasBounds.top.toFloat() / font.definition.atlas.height,
            glyph.atlasBounds.right.toFloat() / font.definition.atlas.width,
            glyph.atlasBounds.bottom.toFloat() / font.definition.atlas.height
        )
        stateManager.setUniform1f(
            "uScreenPxRange",
            (scale / font.definition.atlas.size * font.definition.atlas.distanceRange).toFloat()
        )


        val finalX = x + glyph.planeBounds.left * scale
        val finalY = y + glyph.planeBounds.top * scale
        val finalWidth = (glyph.planeBounds.right - glyph.planeBounds.left) * scale
        val finalHeight = (glyph.planeBounds.bottom - glyph.planeBounds.top) * scale

        drawQuad(finalX.toFloat(), finalY.toFloat(), finalWidth.toFloat(), finalHeight.toFloat())
        return (glyph.advance * scale).toFloat()
    }

    fun drawString(font: MSDFRenderer, x: Float, y: Float, size: Float, text: String, color: Color) {
        stateManager.useShader(GLShader.FONT)
        stateManager.bindTexture(GL.GL_TEXTURE1, font.atlas)

        stateManager.setUniformColor4f("uFontColor", color)

        var x = x
        for (ch in text) {
            x += drawGlyph(font, x, y, size, ch)
        }
    }

    fun drawImage(
        image: BufferedImage,
        x: Float, y: Float,
        width: Float, height: Float,
        borderLeft: Float = 0f, borderRight: Float = 0f,
        borderTop: Float = 0f, borderBottom: Float = 0f,
        scale: Float = 1f,
    ) {
        stateManager.bindTexture(GL.GL_TEXTURE0, image)

        if (borderLeft == 0f && borderRight == 0f && borderTop == 0f && borderBottom == 0f) {
            stateManager.useShader(GLShader.IMAGE)
            drawQuad(x, y, width, height)
        } else {
            stateManager.useShader(GLShader.NINE_SLICE)

            stateManager.setUniform4f(
                "uSourceRect",
                borderLeft / width,
                borderTop / height,
                1 - borderRight / width,
                1 - borderBottom / height
            )
            stateManager.setUniform4f(
                "uTargetRect",
                borderLeft * scale / image.width,
                borderTop * scale / image.height,
                1 - borderRight * scale / image.width,
                1 - borderBottom * scale / image.height
            )

            drawQuad(x, y, width, height)
        }
    }

    fun drawFill(x: Float, y: Float, width: Float, height: Float, color: Color) {
        stateManager.useShader(GLShader.FILL)
        stateManager.setUniformColor4f("uColor", color)
        drawQuad(x, y, width, height)
    }

    fun setClipBox(box: RenderBox?) {
        if (box == null) {
            stateManager.gl.glDisable(GL.GL_SCISSOR_TEST)
        } else {
            stateManager.gl.glEnable(GL.GL_SCISSOR_TEST)

            stateManager.gl.glScissor(box.x, stateManager.height - box.y - box.height, box.width, box.height)
        }
    }
}