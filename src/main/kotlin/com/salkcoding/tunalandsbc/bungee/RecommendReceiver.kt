package com.salkcoding.tunalandsbc.bungee

import com.google.gson.JsonParser
import com.salkcoding.tunalandsbc.currentServerName
import com.salkcoding.tunalandsbc.gui.render.openRecommendGui
import com.salkcoding.tunalandsbc.lands.Lands
import fish.evatuna.metamorphosis.kafka.KafkaReceiveEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.*

val recommendReceiveMap = mutableMapOf<UUID, MutableMap<UUID, Lands>>()

class RecommendReceiver : Listener {

    @EventHandler
    fun onReceived(event: KafkaReceiveEvent) {
        if (!event.key.startsWith("com.salkcoding.tunalands")) return
        //Split a last sub key
        when (event.key.split(".").last()) {
            "response_recommend" -> {
                val json = JsonParser().parse(event.value).asJsonObject
                if (currentServerName != json["targetServerName"].asString) return

                val uuid = UUID.fromString(json["uuid"].asString)
                val player = Bukkit.getPlayer(uuid)
                if (player == null) {//Leave server before open GUI
                    recommendReceiveMap.remove(uuid)
                    return
                }
                //Prevent NPE
                if (uuid !in recommendReceiveMap) recommendReceiveMap[uuid] = mutableMapOf()
                val recommendArray = json["recommendArray"].asJsonArray
                recommendArray.forEach {
                    val recommendJson = it.asJsonObject
                    val targetUUID = UUID.fromString(recommendJson["targetUUID"].asString)
                    val open = recommendJson["open"].asBoolean
                    val memberSize = recommendJson["memberSize"].asInt
                    val visitorCount = recommendJson["visitorCount"].asLong
                    val createdMillisecond = recommendJson["createdMillisecond"].asLong
                    val recommend = recommendJson["recommend"].asInt
                    val landsName = recommendJson["landsName"].asString
                    val lore = mutableListOf<String>()
                    recommendJson["lore"].asJsonArray.forEach { string ->
                        lore.add(string.asString)
                    }
                    recommendReceiveMap[uuid]!![targetUUID] = Lands(
                        targetUUID, open, memberSize, visitorCount, createdMillisecond, recommend, landsName, lore
                    )
                }
                player.openRecommendGui()
            }
        }
    }
}