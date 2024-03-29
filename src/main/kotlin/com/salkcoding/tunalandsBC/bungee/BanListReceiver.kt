package com.salkcoding.tunalandsBC.bungee

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.stream.MalformedJsonException
import com.salkcoding.tunalandsBC.currentServerName
import com.salkcoding.tunalandsBC.gui.render.openBanListGui
import com.salkcoding.tunalandsBC.lands.BanData
import com.salkcoding.tunalandsBC.tunaLands
import fish.evatuna.metamorphosis.redis.MetamorphosisReceiveEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.*

val banReceiverMap = mutableMapOf<UUID, MutableMap<UUID, BanData>>()

class BanListReceiver : Listener {

    @EventHandler
    fun onReceived(event: MetamorphosisReceiveEvent) {
        if (!event.key.startsWith("com.salkcoding.tunalands")) return
        lateinit var json: JsonObject
        try {
            json = JsonParser.parseString(event.value).asJsonObject
        } catch (e: MalformedJsonException) {
            tunaLands.logger.warning("${event.key} sent an object without transform to JSON object!")
        }

        //Split a last sub key
        when (event.key.split(".").last()) {
            "response_banlist" -> {
                if (currentServerName != json["targetServerName"].asString) return

                val uuid = UUID.fromString(json["uuid"].asString)
                val player = Bukkit.getPlayer(uuid)
                if (player == null) {//Leave server before open GUI
                    banReceiverMap.remove(uuid)
                    return
                }
                //Prevent NPE
                if (uuid !in banReceiverMap) banReceiverMap[uuid] = mutableMapOf()
                val banArray = json["banArray"].asJsonArray
                banArray.forEach {
                    val banJson = it.asJsonObject
                    val targetUUID = UUID.fromString(banJson["targetUUID"].asString)
                    banReceiverMap[uuid]!![targetUUID] = BanData(
                        targetUUID,
                        banJson["banned"].asLong
                    )
                }
                player.openBanListGui()
            }
        }
    }
}