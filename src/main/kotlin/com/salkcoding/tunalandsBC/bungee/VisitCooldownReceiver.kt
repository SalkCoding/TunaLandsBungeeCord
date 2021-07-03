package com.salkcoding.tunalandsbc.bungee

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.salkcoding.tunalandsbc.bungeeApi
import com.salkcoding.tunalandsbc.currentServerName
import com.salkcoding.tunalandsbc.metamorphosis
import com.salkcoding.tunalandsbc.util.TeleportCooltime
import fish.evatuna.metamorphosis.kafka.KafkaReceiveEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.*

class VisitCooldownReceiver : Listener {

    @EventHandler
    fun onReceived(event: KafkaReceiveEvent) {
        if (!event.key.startsWith("com.salkcoding.tunalands")) return
        //Split a last sub key
        when (event.key.split(".").last()) {
            "response_visit_connect" -> {
                val json = JsonParser().parse(event.value).asJsonObject
                val serverName = json["serverName"].asString
                if (currentServerName != serverName) return

                val uuid = UUID.fromString(json["uuid"].asString)
                val player = Bukkit.getPlayer(uuid)
                if (player != null) {
                    val ownerUUID = json["ownerUUID"].asString

                    val visitCooldown = json["visitCooldown"].asLong

                    TeleportCooltime.addPlayer(player, null, visitCooldown, {
                        bungeeApi.connect(player, json["visitServerName"].asString)

                        val sendJson = JsonObject()
                        sendJson.addProperty("uuid", uuid.toString())
                        sendJson.addProperty("ownerUUID", ownerUUID)
                        metamorphosis.send("com.salkcoding.tunalands.visit_teleported", sendJson.toString())
                    }, true)
                }
            }
        }
    }
}