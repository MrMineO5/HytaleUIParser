package app.ultradev.hytaleuiparser.plugin

import app.ultradev.hytaleuiparser.spec.command.CustomUIInfo
import app.ultradev.hytaleuiparser.spec.command.writeVarInt
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class UIDebugServer(bindAddress: String, port: Int) {
    private val serverSocket: ServerSocket
    private val acceptThread: Thread

    @Volatile
    private var running = true

    private val clients = mutableListOf<Socket>()
    private val clientLock = ReentrantLock()

    init {
        serverSocket = ServerSocket()
        serverSocket.reuseAddress = true
        serverSocket.bind(InetSocketAddress(bindAddress, port))

        acceptThread = Thread({
            try {
                while (running) {
                    val client = serverSocket.accept()
                    if (!running) {
                        client.closeQuietly()
                        return@Thread
                    }
                    clientLock.withLock { clients.add(client) }
                }
            } catch (_: IOException) {
                // Expected on shutdown when serverSocket.close() unblocks accept()
            } finally {
                running = false
            }
        }, "UI Debug Connection Listener")
    }

    fun start() {
        acceptThread.start()
    }

    fun stop() {
        running = false

        serverSocket.closeQuietly()
        acceptThread.interrupt()

        clientLock.withLock {
            clients.forEach { it.closeQuietly() }
            clients.clear()
        }
    }

    fun writePageInfo(pageInfo: CustomUIInfo) = clientLock.withLock {
        if (!running) return@withLock

        val failed = mutableListOf<Socket>()
        clients.forEach { client ->
            try {
                val out = client.getOutputStream()
                pageInfo.write(out)
                out.flush()
            } catch (_: IOException) {
                failed.add(client)
            }
        }
        failed.forEach { it.closeQuietly() }
        clients.removeAll(failed)
    }

    private fun Socket.closeQuietly() {
        try {
            close()
        } catch (_: IOException) {
        }
    }

    private fun ServerSocket.closeQuietly() {
        try {
            close()
        } catch (_: IOException) {
        }
    }
}