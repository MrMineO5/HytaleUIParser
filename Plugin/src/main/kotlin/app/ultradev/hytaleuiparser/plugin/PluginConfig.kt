package app.ultradev.hytaleuiparser.plugin

import com.hypixel.hytale.codec.Codec
import com.hypixel.hytale.codec.KeyedCodec
import com.hypixel.hytale.codec.builder.BuilderCodec
import java.util.function.Supplier

class PluginConfig {
    var bindAddress = "127.0.0.1"
    var port = 14152.toShort()

    companion object {
        val CODEC: BuilderCodec<PluginConfig?> = BuilderCodec.builder(
            PluginConfig::class.java,
            Supplier { PluginConfig() })
            .append<String?>(
                KeyedCodec<String?>("BindAddress", Codec.STRING),
                { config: PluginConfig?, value: String? -> config!!.bindAddress = value!! },
                { config: PluginConfig? -> config!!.bindAddress })
            .add()
            .append<Short?>(
                KeyedCodec<Short?>("Port", Codec.SHORT),
                { config: PluginConfig?, value: Short? -> config!!.port = value!! },
                { config: PluginConfig? -> config!!.port })
            .add()
            .build()
    }
}
