package com.salkcoding.tunalandsbc.bungee

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.salkcoding.tunalandsbc.currentServerName
import com.salkcoding.tunalandsbc.metamorphosis
import com.salkcoding.tunalandsbc.util.TeleportCooltime
import fish.evatuna.metamorphosis.kafka.KafkaReceiveEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.*

class SpawnCooldownReceiver : Listener {

    @EventHandler
    fun onReceived(event: KafkaReceiveEvent) {
        if (!event.key.startsWith("com.salkcoding.tunalands")) return
        //Split a last sub key
        when (event.key.split(".").last()) {
            "response_spawn" -> {
                val json = JsonParser.parseString(event.value).asJsonObject
                if (currentServerName != json["targetServerName"].asString) return

                val uuid = UUID.fromString(json["uuid"].asString)
                val player = Bukkit.getPlayer(uuid)
                if (player != null) {
                    val spawnCooldown = json["spawnCooldown"].asLong

                    TeleportCooltime.addPlayer(player, null, spawnCooldown, {
                        val sendJson = JsonObject().apply {
                            addProperty("uuid", uuid.toString())
                            addProperty("name", player.name)
                        }

                        metamorphosis.send("com.salkcoding.tunalands.pending_spawn_teleport", sendJson.toString())
                    }, true)
                }
            }
        }
    }
}