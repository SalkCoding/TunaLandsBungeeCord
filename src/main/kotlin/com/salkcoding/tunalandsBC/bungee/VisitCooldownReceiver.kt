package com.salkcoding.tunalandsBC.bungee

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.salkcoding.tunalandsBC.currentServerName
import com.salkcoding.tunalandsBC.metamorphosis
import com.salkcoding.tunalandsBC.util.TeleportCooltime
import fish.evatuna.metamorphosis.redis.MetamorphosisReceiveEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.*

class VisitCooldownReceiver : Listener {

    @EventHandler
    fun onReceived(event: MetamorphosisReceiveEvent) {
        if (!event.key.startsWith("com.salkcoding.tunalands")) return
        //Split a last sub key
        when (event.key.split(".").last()) {
            "response_visit_connect" -> {
                val json = JsonParser.parseString(event.value).asJsonObject
                if (currentServerName != json["targetServerName"].asString) return

                val uuid = UUID.fromString(json["uuid"].asString)
                val player = Bukkit.getPlayer(uuid) ?: return
                val ownerUUID = json["ownerUUID"].asString

                val visitCooldown = json["visitCooldown"].asLong

                TeleportCooltime.addPlayer(player, null, visitCooldown, {
                    val sendJson = JsonObject().apply {
                        addProperty("uuid", uuid.toString())
                        addProperty("name", player.name)
                        addProperty("ownerUUID", ownerUUID)
                    }
                    metamorphosis.send("com.salkcoding.tunalands.pending_visit_teleport", sendJson.toString())
                }, false)
            }

            "response_visit_specific" -> {
                val json = JsonParser.parseString(event.value).asJsonObject
                if (currentServerName != json["targetServerName"].asString) return

                val uuid = UUID.fromString(json["uuid"].asString)
                val player = Bukkit.getPlayer(uuid) ?: return
                val ownerUUID = json["ownerUUID"].asString

                val visitCooldown = json["visitCooldown"].asLong

                TeleportCooltime.addPlayer(player, null, visitCooldown, {
                    val sendJson = JsonObject().apply {
                        addProperty("uuid", uuid.toString())
                        addProperty("name", player.name)
                        addProperty("ownerUUID", ownerUUID)
                    }
                    metamorphosis.send("com.salkcoding.tunalands.pending_visit_teleport", sendJson.toString())
                }, false)
            }
        }
    }
}