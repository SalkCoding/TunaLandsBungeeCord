package com.salkcoding.tunalandsbc.bungee

import com.google.common.io.ByteStreams
import com.salkcoding.tunalandsbc.gui.render.openVisitGui
import com.salkcoding.tunalandsbc.lands.Lands
import com.salkcoding.tunalandsbc.bungee.channelapi.BungeeChannelApi
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

val visitReceiverMap = mutableMapOf<UUID, MutableMap<UUID, Lands>>()

class VisitReceiver : BungeeChannelApi.ForwardConsumer {

    override fun accept(channel: String, receiver: Player, data: ByteArray) {
        val inMessage = ByteStreams.newDataInput(data)
        val uuid = UUID.fromString(inMessage.readUTF())
        val player = Bukkit.getPlayer(uuid)
        if (player == null) {//Leave server before open GUI
            visitReceiverMap.remove(uuid)
            return
        }
        //Prevent NPE
        if (uuid !in visitReceiverMap) visitReceiverMap[uuid] = mutableMapOf()
        val length = inMessage.readInt()
        for (i in 0 until length) {
            val targetUUID = UUID.fromString(inMessage.readUTF())
            val open = inMessage.readBoolean()
            val memberSize = inMessage.readInt()
            val visitorCount = inMessage.readLong()
            val createdMillisecond = inMessage.readLong()
            val lore = mutableListOf<String>()
            (1..3).forEach { _ ->
                lore.add(inMessage.readUTF())
            }
            visitReceiverMap[uuid]!![targetUUID] = Lands(
                targetUUID, open, memberSize, visitorCount, createdMillisecond, lore
            )
        }
        if (length != 36)//Last page
            player.openVisitGui()
    }
}