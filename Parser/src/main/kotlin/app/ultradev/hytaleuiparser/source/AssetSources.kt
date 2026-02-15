package app.ultradev.hytaleuiparser.source

import app.ultradev.hytaleuiparser.Parser
import app.ultradev.hytaleuiparser.Tokenizer
import app.ultradev.hytaleuiparser.ast.RootNode
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.name

object AssetSources {
    fun parseUIFiles(source: AssetSource): Map<String, RootNode> {
        val uiFiles = source.listUIFiles()
        val output = HashMap<String, RootNode>(uiFiles.size)
        uiFiles.forEach {
            try {
                val tokenizer = Tokenizer(source.getAsset(it)!!.reader())
                val parser = Parser(tokenizer)
                output[it.toString()] = parser.finish()
            } catch (e: Exception) {
                throw RuntimeException("Failed to parse ${it.name}", e)
            }
        }
        return output
    }

    fun getHytaleDirectory(): String {
        val home = System.getProperty("user.home")
        val os = System.getProperty("os.name")

        return when {
            os.startsWith("Windows") ->
                "$home/AppData/Roaming/Hytale"
            os.startsWith("Mac") ->
                "$home/Library/Application Support/Hytale"
            else -> {
                val flatpak = Path("$home/.var/app/com.hypixel.HytaleLauncher/data/Hytale")
                if (flatpak.exists()) flatpak.toString()
                else "$home/.local/share/Hytale"
            }
        }
    }

    fun getAssetsZipPath(hytaleDir: String = getHytaleDirectory(), patchline: String = "release", build: String = "latest"): String {
        return "${hytaleDir}/install/${patchline}/package/game/${build}/Assets.zip"
    }

    fun getAssetsZipSource(hytaleDir: String = getHytaleDirectory(), patchline: String = "release", build: String = "latest"): AssetSource {
        val archive = Path(getAssetsZipPath(hytaleDir, patchline, build))
        return ArchiveAssetSource(archive)
    }
}