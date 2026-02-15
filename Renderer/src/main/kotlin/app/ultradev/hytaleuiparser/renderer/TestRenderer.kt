package app.ultradev.hytaleuiparser.renderer

import app.ultradev.hytaleuiparser.Validator
import app.ultradev.hytaleuiparser.source.AssetSources
import java.awt.BorderLayout
import java.awt.Dimension
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JPanel

object TestRenderer {
    val source = AssetSources.getAssetsZipSource()

    @JvmStatic
    fun main(args: Array<String>) {
        val frame = JFrame("Hytale UI Renderer")
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.size = Dimension(800, 600)

        val assets = AssetSources.parseUIFiles(source)

        val validator = Validator(assets)
        val testPage = validator.validateRoot("Common/UI/Custom/Pages/LaunchPadSettingsPage.ui") ?: error("Failed to validate page")

        val rootUIElement = UITransformer.transform(testPage)


        val backgroundImage = ImageIO.read(javaClass.getResourceAsStream("/background.png"))

        val parent = JPanel(BorderLayout())
        parent.add(HytaleUIPanel(rootUIElement, backgroundImage), BorderLayout.CENTER)

        frame.contentPane = parent

        frame.isVisible = true
    }
}