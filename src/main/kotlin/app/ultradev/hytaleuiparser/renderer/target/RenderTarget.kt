package app.ultradev.hytaleuiparser.renderer.target

import app.ultradev.hytaleuiparser.renderer.RenderBox
import app.ultradev.hytaleuiparser.renderer.element.UIElement
import java.awt.Color
import java.awt.Image
import java.awt.image.BufferedImage

interface RenderTarget {
    val box: RenderBox

    fun renderImage(
        image: BufferedImage,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        horizontalBorder: Int = 0,
        verticalBorder: Int = 0
    )
    fun renderFill(color: Color, x: Int, y: Int, width: Int, height: Int)

    fun renderText(
        text: String,
        x: Int,
        y: Int,
        color: Color,
        isBold: Boolean = false,
        isItalics: Boolean = false,
        isUnderlined: Boolean = false,
        isSecondary: Boolean = false
    )
}