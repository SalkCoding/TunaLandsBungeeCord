package com.salkcoding.tunalandsbc.bungee

import com.google.common.io.ByteStreams
import com.salkcoding.tunalandsbc.bungeeApi
import com.salkcoding.tunalandsbc.tunaLands
import com.salkcoding.tunalandsbc.bungee.channelapi.BungeeChannelApi
import org.bukkit.entity.Player
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException

class ReloadReceiver : BungeeChannelApi.ForwardConsumer {

    override fun accept(channel: String, player: Player, data: ByteArray) {
        val inMessage = ByteStreams.newDataInput(data)
        val serverName = inMessage.readUTF()

        val messageBytes = ByteArrayOutputStream()
        val messageOut = DataOutputStream(messageBytes)
        val onlinePlayers = tunaLands.server.onlinePlayers
        messageOut.writeInt(onlinePlayers.size)
        onlinePlayers.forEach {
            try {
                messageOut.writeUTF(it.uniqueId.toString())
            } catch (exception: IOException) {
                exception.printStackTrace()
            }
        }

        bungeeApi.forward(serverName, channel, messageBytes.toByteArray())
    }

}