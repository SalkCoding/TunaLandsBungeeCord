package com.salkcoding.tunalandsbc.bungee

import com.google.gson.JsonParser
import com.salkcoding.tunalandsbc.currentServerName
import com.salkcoding.tunalandsbc.gui.render.openVisitGui
import com.salkcoding.tunalandsbc.lands.Lands
import fish.evatuna.metamorphosis.kafka.KafkaReceiveEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.*

val visitReceiverMap = mutableMapOf<UUID, MutableMap<UUID, Lands>>()

class VisitReceiver : Listener {

    @EventHandler
    fun onReceived(event: KafkaReceiveEvent) {
        if (!event.key.startsWith("com.salkcoding.tunalands")) return
        //Split a last sub key
        when (event.key.split(".").last()) {
            "response_visit" -> {
                val json = JsonParser().parse(event.value).asJsonObject
                if (currentServerName != json["serverName"].asString) return

                val uuid = UUID.fromString(json["uuid"].asString)
                val player = Bukkit.getPlayer(uuid)
                if (player == null) {//Leave server before open GUI
                    visitReceiverMap.remove(uuid)
                    return
                }
                //Prevent NPE
                if (uuid !in visitReceiverMap) visitReceiverMap[uuid] = mutableMapOf()
                val visitArray = json["visitArray"].asJsonArray
                visitArray.forEach {
                    val visitJson = it.asJsonObject
                    val targetUUID = UUID.fromString(visitJson["targetUUID"].asString)
                    val open = visitJson["open"].asBoolean
                    val memberSize = visitJson["memberSize"].asInt
                    val visitorCount = visitJson["visitorCount"].asLong
                    val createdMillisecond = visitJson["createdMillisecond"].asLong
                    val recommend = visitJson["recommend"].asInt
                    val landsName = visitJson["landsName"].asString
                    val lore = mutableListOf<String>()
                    visitJson["lore"].asJsonArray.forEach { string ->
                        lore.add(string.asString)
                    }
                    visitReceiverMap[uuid]!![targetUUID] = Lands(
                        targetUUID, open, memberSize, visitorCount, createdMillisecond, recommend, landsName, lore
                    )
                }
                player.openVisitGui()
            }
        }
    }
}