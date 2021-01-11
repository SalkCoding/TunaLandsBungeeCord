package com.salkcoding.tunalandsBC.listener

import com.salkcoding.tunalandsBC.bungeeApi
import com.salkcoding.tunalandsBC.tunaLands
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException

class PlayerConnectListener : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        sendConnectAsynchronously(event.player, "tunalands-playerjoin")
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        sendConnectAsynchronously(event.player, "tunalands-playerquit")
    }

    private fun sendConnectAsynchronously(player: Player, subChannel: String) {
        Bukkit.getScheduler().runTaskAsynchronously(tunaLands, Runnable {
            val messageBytes = ByteArrayOutputStream()
            val messageOut = DataOutputStream(messageBytes)
            try {
                messageOut.writeUTF(player.uniqueId.toString())
            } catch (exception: IOException) {
                exception.printStackTrace()
            }

            bungeeApi.forward("ALL", subChannel, messageBytes.toByteArray())
        })
    }
}