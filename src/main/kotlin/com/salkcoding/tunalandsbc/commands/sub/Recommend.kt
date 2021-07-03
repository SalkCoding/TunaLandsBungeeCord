package com.salkcoding.tunalandsbc.commands.sub

import com.google.gson.JsonObject
import com.salkcoding.tunalandsbc.currentServerName
import com.salkcoding.tunalandsbc.metamorphosis
import com.salkcoding.tunalandsbc.tunaLands
import com.salkcoding.tunalandsbc.util.errorFormat
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class Recommend : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        when {
            label == "recommend" && args.isEmpty() -> {
                val player = sender as? Player
                if (player != null) {
                    workAsync(player)
                } else sender.sendMessage("콘솔에서는 사용할 수 없는 명령어입니다.".errorFormat())
                return true
            }
        }
        return false
    }

    private fun workAsync(player: Player) {
        Bukkit.getScheduler().runTaskAsynchronously(tunaLands, Runnable {
            val sendJson = JsonObject()
            sendJson.addProperty("uuid", player.uniqueId.toString())
            sendJson.addProperty("serverName", currentServerName)

            metamorphosis.send("com.salkcoding.tunalands.recommend", sendJson.toString())
        })
    }
}