package de.saschahlusiak.freebloks.network.message

import de.saschahlusiak.freebloks.network.*
import java.nio.ByteBuffer

data class NetCurrentPlayer(val player: Int): Message(MessageType.CurrentPlayer, 1) {
    init {
        assert(player >= -1 && player <= 3) { "Invalid player $player" }
    }

    override fun write(buffer: ByteBuffer) {
        super.write(buffer)
        buffer.put(player.toByte())
    }

    companion object {
        fun from(data: ByteBuffer): NetCurrentPlayer {
            val player = data.get().toInt()
            return NetCurrentPlayer(player)
        }
    }
}