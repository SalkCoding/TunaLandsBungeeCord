package com.salkcoding.tunalandsBC.bungee

import com.google.common.io.ByteStreams
import com.salkcoding.tunalandsBC.gui.render.openBanListGui
import com.salkcoding.tunalandsBC.lands.BanData
import io.github.leonardosnt.bungeechannelapi.BungeeChannelApi
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

val banReceiverMap = mutableMapOf<UUID, MutableMap<UUID, BanData>>()

class BanListReceiver : BungeeChannelApi.ForwardConsumer {

    override fun accept(channel: String, receiver: Player, data: ByteArray) {
        val inMessage = ByteStreams.newDataInput(data)
        val uuid = UUID.fromString(inMessage.readUTF())
        val player = Bukkit.getPlayer(uuid)
        if (player == null) {//Leave server before open GUI
            banReceiverMap.remove(uuid)
            return
        }
        //Prevent NPE
        if (uuid !in banReceiverMap) banReceiverMap[uuid] = mutableMapOf()
        val length = inMessage.readInt()
        for (i in 0 until length) {
            val targetUUID = UUID.fromString(inMessage.readUTF())
            banReceiverMap[uuid]!![targetUUID] = BanData(
                targetUUID,
                inMessage.readLong()
            )
        }
        if (length != 36)//Last page
            player.openBanListGui()
    }
}