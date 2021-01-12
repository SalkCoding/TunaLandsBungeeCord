package com.salkcoding.tunalandsBC.bungee

import com.google.common.io.ByteStreams
import com.salkcoding.tunalandsBC.bungeeApi
import com.salkcoding.tunalandsBC.tunaLands
import com.salkcoding.tunalandsBC.util.TeleportCooltime
import io.github.leonardosnt.bungeechannelapi.BungeeChannelApi
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
                bungeeApi.connectOther(player.name, serverName)

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