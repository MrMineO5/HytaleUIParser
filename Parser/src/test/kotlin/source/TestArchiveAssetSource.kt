package source

import app.ultradev.hytaleuiparser.Validator
import app.ultradev.hytaleuiparser.source.ArchiveAssetSource
import app.ultradev.hytaleuiparser.source.AssetSource
import app.ultradev.hytaleuiparser.source.AssetSources
import kotlin.io.path.Path
import kotlin.test.Test

class TestArchiveAssetSource {
    @Test
    fun testFindsFiles() {
        val archive = Path(AssetSources.getAssetsZipPath())
        val source = ArchiveAssetSource(archive)

        val uiFiles = AssetSources.parseUIFiles(source)
        Validator(uiFiles).validate()

        assert("Common/UI/Custom/Common.ui" in uiFiles)
    }
}
