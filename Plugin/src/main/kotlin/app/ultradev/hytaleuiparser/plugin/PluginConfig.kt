package app.ultradev.hytaleuiparser.plugin

import com.hypixel.hytale.codec.Codec
import com.hypixel.hytale.codec.KeyedCodec
import com.hypixel.hytale.codec.builder.BuilderCodec
import java.util.function.Supplier

class PluginConfig {
    var bindAddress = "127.0.0.1"
    var port = 14152

    companion object {
        val CODEC: BuilderCodec<PluginConfig> = BuilderCodec.builder(
            PluginConfig::class.java
        ) { PluginConfig() }
            .append(
                KeyedCodec("BindAddress", Codec.STRING),
                { config, value -> config.bindAddress = value },
                { config -> config.bindAddress })
            .add()
            .append(
                KeyedCodec("Port", Codec.INTEGER),
                { config, value -> config.port = value },
                { config -> config.port })
            .add()
            .build()
    }
}
