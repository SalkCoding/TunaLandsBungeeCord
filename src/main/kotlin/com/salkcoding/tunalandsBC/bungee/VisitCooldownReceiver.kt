package com.salkcoding.tunalandsBC.bungee

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.salkcoding.tunalandsBC.currentServerName
import com.salkcoding.tunalandsBC.metamorphosis
import com.salkcoding.tunalandsBC.util.TeleportCooltime
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
                val json = JsonParser.parseString(event.value).asJsonObject
                if (currentServerName != json["targetServerName"].asString) return

                val uuid = UUID.fromString(json["uuid"].asString)
                val player = Bukkit.getPlayer(uuid)
                if (player != null) {
                    val ownerUUID = json["ownerUUID"].asString

                    val visitCooldown = json["visitCooldown"].asLong

                    TeleportCooltime.addPlayer(player, null, visitCooldown, {
                        val sendJson = JsonObject().apply {
                            addProperty("uuid", uuid.toString())
                            addProperty("name", player.name)
                            addProperty("ownerUUID", ownerUUID)
                        }
                        metamorphosis.send("com.salkcoding.tunalands.pending_visit_teleport", sendJson.toString())
                    }, true)
                }
            }
        }
    }
}