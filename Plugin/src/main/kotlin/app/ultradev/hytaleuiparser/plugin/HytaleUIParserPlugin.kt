package app.ultradev.hytaleuiparser.plugin

import app.ultradev.hytaleuiparser.spec.command.CustomUIInfo
import app.ultradev.hytaleuiparser.spec.command.UICommand
import com.hypixel.hytale.protocol.packets.interface_.CustomHud
import com.hypixel.hytale.protocol.packets.interface_.CustomPage
import com.hypixel.hytale.protocol.packets.interface_.CustomUICommand
import com.hypixel.hytale.protocol.packets.interface_.UpdateAnchorUI
import com.hypixel.hytale.server.core.io.adapter.PacketAdapters
import com.hypixel.hytale.server.core.io.adapter.PacketWatcher
import com.hypixel.hytale.server.core.plugin.JavaPlugin
import com.hypixel.hytale.server.core.plugin.JavaPluginInit

class HytaleUIParserPlugin(jpi: JavaPluginInit) : JavaPlugin(jpi) {
    private val config = this.withConfig(PluginConfig.CODEC)

    private var server: UIDebugServer? = null

    override fun setup() {
        config.save()

        val config = config.get()
        server = UIDebugServer(config.bindAddress, config.port)
        server!!.start()
        logger.atInfo().log("UIDebugServer started on ${config.bindAddress}:${config.port}")

        PacketAdapters.registerOutbound(PacketWatcher { _, packet ->
            val info = when (packet) {
                is CustomPage -> CustomUIInfo(
                    packet.key ?: "",
                    transformCommands(packet.commands),
                    CustomUIInfo.Type.Page,
                    packet.clear
                )

                is CustomHud -> CustomUIInfo(
                    "",
                    transformCommands(packet.commands),
                    CustomUIInfo.Type.Hud,
                    packet.clear
                )

                is UpdateAnchorUI -> CustomUIInfo(
                    packet.anchorId ?: "",
                    transformCommands(packet.commands),
                    CustomUIInfo.Type.AnchorUI,
                    packet.clear
                )

                else -> return@PacketWatcher
            }
            server?.writePageInfo(info)
        })
    }

    private fun transformCommands(commands: Array<CustomUICommand>?): List<UICommand> {
        if (commands == null) return emptyList()
        return commands.map {
            UICommand(
                UICommand.Type.valueOf(it.type.name),
                it.selector, it.data, it.text
            )
        }
    }

    override fun shutdown() {
        server?.stop()
    }
}
