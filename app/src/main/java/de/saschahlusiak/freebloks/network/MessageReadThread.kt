package de.saschahlusiak.freebloks.network

import android.util.Log
import de.saschahlusiak.freebloks.model.GameStateException
import java.io.InputStream

/**
 * This Thread will continuously poll the client's InputStream to process individual messages.
 *
 * An IOException thrown inside the loop will cause the Thread to go down. The caught exception
 * is stored in [error].
 *
 * A [ProtocolException] or [GameStateException] will cause the Thread to throw a RuntimeException.
 *
 * @param reader the reader to read from
 * @param handler where the received messages are passed to
 * @param onGoDown callback when the thread is going down, e.g. because of disconnect
 */
class MessageReadThread(
    private val reader: MessageReader,
    private val handler: MessageHandler,
    private val onGoDown: () -> Unit
) : Thread("GameClientThread") {
    private val tag = MessageReadThread::class.java.simpleName

    constructor(
        inputStream: InputStream,
        handler: MessageHandler,
        onGoDown: () -> Unit
    ) : this(MessageReader(inputStream), handler, onGoDown)

    @get:Synchronized
    private var goDown = false

    @get:Synchronized
    var error: Throwable? = null
        private set

    @Synchronized
    fun goDown() {
        goDown = true
        interrupt()
    }

    override fun run() {
        try {
            for (message in reader) {
                if (goDown) return

                handler.handleMessage(message)
            }
        } catch (e: Exception) {
            if (goDown) return

            // crashReporter.logException(e)
            synchronized(this) {
                error = e
            }
        } finally {
            // connection is lost or whatever, shut down the client
            onGoDown.invoke()
            Log.i(tag, "Disconnected, thread going down. Error: ${error?.message}")
        }
    }
}