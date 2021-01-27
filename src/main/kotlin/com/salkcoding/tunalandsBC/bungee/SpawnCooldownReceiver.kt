package com.salkcoding.tunalandsbc.bungee

import com.google.common.io.ByteStreams
import com.salkcoding.tunalandsbc.bungeeApi
import com.salkcoding.tunalandsbc.util.TeleportCooltime
import com.salkcoding.tunalandsbc.bungee.channelapi.BungeeChannelApi
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.util.*

class SpawnCooldownReceiver : BungeeChannelApi.ForwardConsumer {

    override fun accept(channel: String, receiver: Player, data: ByteArray) {
        val inMessage = ByteStreams.newDataInput(data)
        val uuid = UUID.fromString(inMessage.readUTF())
        val player = Bukkit.getPlayer(uuid)
        if (player != null) {//Leave server before teleport
            val serverName = inMessage.readUTF()
            val spawnCooldown = inMessage.readLong()

            TeleportCooltime.addPlayer(player, null, spawnCooldown, {
                bungeeApi.connect(player, serverName)

                val messageBytes = ByteArrayOutputStream()
                val messageOut = DataOutputStream(messageBytes)
                try {
                    messageOut.writeUTF(uuid.toString())
                } catch (exception: IOException) {
                    exception.printStackTrace()
                }
                bungeeApi.forward(serverName, "tunalands-spawn-teleport", messageBytes.toByteArray())
            }, true)
        }
    }
}