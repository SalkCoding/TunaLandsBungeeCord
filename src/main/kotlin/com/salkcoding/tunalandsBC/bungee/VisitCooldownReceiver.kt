package com.salkcoding.tunalandsBC.bungee

import com.google.common.io.ByteStreams
import com.salkcoding.tunalandsBC.bungeeApi
import com.salkcoding.tunalandsBC.util.TeleportCooltime
import com.salkcoding.tunalandsBC.bungee.channelapi.BungeeChannelApi
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.util.*

class VisitCooldownReceiver : BungeeChannelApi.ForwardConsumer {

    override fun accept(channel: String, receiver: Player, data: ByteArray) {
        val inMessage = ByteStreams.newDataInput(data)
        val uuid = UUID.fromString(inMessage.readUTF())
        val player = Bukkit.getPlayer(uuid)
        if (player != null) {//Leave server before open GUI
            val targetUUID = inMessage.readUTF()
            val serverName = inMessage.readUTF()
            val visitCooldown = inMessage.readLong()

            TeleportCooltime.addPlayer(player, null, visitCooldown, {
                bungeeApi.connectOther(player.name, serverName)

                val messageBytes = ByteArrayOutputStream()
                val messageOut = DataOutputStream(messageBytes)
                try {
                    messageOut.writeUTF(uuid.toString())
                    messageOut.writeUTF(targetUUID)
                } catch (exception: IOException) {
                    exception.printStackTrace()
                }
                bungeeApi.forward(serverName, "tunalands-visit-teleport", messageBytes.toByteArray())
            }, true)
        }
    }
}