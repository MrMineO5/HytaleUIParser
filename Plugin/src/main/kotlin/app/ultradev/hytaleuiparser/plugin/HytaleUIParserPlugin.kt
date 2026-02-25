package app.ultradev.hytaleuiparser.plugin

import com.hypixel.hytale.protocol.Packet
import com.hypixel.hytale.protocol.packets.interface_.CustomPage
import com.hypixel.hytale.server.core.io.PacketHandler
import com.hypixel.hytale.server.core.io.adapter.PacketAdapters
import com.hypixel.hytale.server.core.plugin.JavaPlugin
import com.hypixel.hytale.server.core.plugin.JavaPluginInit
import app.ultradev.hytaleuiparser.spec.command.UICommand
import java.io.ByteArrayOutputStream

class HytaleUIParserPlugin(
    jpi : JavaPluginInit
) : JavaPlugin(jpi) {
    private var config = this.withConfig(PluginConfig.CODEC)
    override fun setup() {
        super.setup()
        
        // Save it to disk to make sure they can edit it.
        config.save();

        PacketAdapters.registerOutbound(
            com.hypixel.hytale.server.core.io.adapter.PacketWatcher { 
                handler: PacketHandler, packet: Packet ->
                val packetName = packet::class.simpleName
                if (packetName == "CustomPage") {
                    val uiCmd = packet as CustomPage
                    if (uiCmd.commands == null) {
                        return@PacketWatcher
                    }
                    
                    val page = uiCmd.key
                    // Some kinda memorystream
                    val outputStream = ByteArrayOutputStream()
                    val cmds = uiCmd.commands!!.map {
                        UICommand(enumValueOf(it.type.name), it.selector, it.data, it.text)
                    }
                    
                    // write to stream
                    cmds.forEach { it.write(outputStream) }
                    
                    
                    // dump to tcp clients
                }
            }
        )
    }
}
