package app.ultradev.hytaleuiparser.renderer.command

import app.ultradev.hytaleuiparser.spec.command.CustomUIInfo
import java.net.InetSocketAddress
import java.net.Socket
import java.util.function.Consumer

class UIDebugClient {
    private val client: Socket = Socket()

    private val listeners = mutableListOf<Consumer<CustomUIInfo>>()
    private val readerThread = Thread({
        while (true) {
            val page = readPage()
            listeners.forEach { it.accept(page) }
        }
    }, "UI Debug Client")

    fun connect(address: String = "127.0.0.1", port: Int = 14152) {
        client.connect(InetSocketAddress(address, port))
        readerThread.start()
    }

    private fun readPage(): CustomUIInfo {
        return CustomUIInfo.read(client.getInputStream())
    }

    fun shutdown() {
        readerThread.interrupt()
        client.close()
    }

    fun listen(listener: Consumer<CustomUIInfo>) {
        listeners.add(listener)
    }
}