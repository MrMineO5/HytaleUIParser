package app.ultradev.hytaleuiparser.renderer.command

import app.ultradev.hytaleuiparser.spec.command.CustomUIInfo
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.atomic.AtomicBoolean
import java.util.function.Consumer

class UIDebugClient {
    private val client: Socket = Socket()

    private val listeners = mutableListOf<Consumer<CustomUIInfo>>()
    private val disconnectListeners = mutableListOf<Runnable>()

    private val running = AtomicBoolean(false)

    private val readerThread = Thread({
        while (running.get()) {
            try {
                val page = readPage()
                listeners.forEach { it.accept(page) }
            } catch (_: Exception) {
                if (!running.getAndSet(false)) return@Thread
                for (l in disconnectListeners) l.run()
            }
        }
    }, "UI Debug Client")

    fun connect(address: String = "127.0.0.1", port: Int = 14152) {
        if (!running.compareAndSet(false, true)) return
        client.connect(InetSocketAddress(address, port))
        readerThread.start()
    }

    private fun readPage(): CustomUIInfo {
        return CustomUIInfo.read(client.getInputStream())
    }

    fun shutdown() {
        running.set(false)
        runCatching { client.close() }
        readerThread.interrupt()
    }

    fun listen(listener: Consumer<CustomUIInfo>) {
        listeners.add(listener)
    }

    fun listenDisconnect(listener: Runnable) {
        disconnectListeners.add(listener)
    }
}