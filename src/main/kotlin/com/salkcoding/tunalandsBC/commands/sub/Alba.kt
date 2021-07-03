package com.salkcoding.tunalandsbc.commands.sub

import com.google.gson.JsonObject
import com.salkcoding.tunalandsbc.metamorphosis
import com.salkcoding.tunalandsbc.tunaLands
import com.salkcoding.tunalandsbc.util.errorFormat
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class Alba : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        when {
            label == "alba" && args.size == 1 -> {
                val player = sender as? Player
                if (player != null) {
                    workAsync(player, args[0])
                } else sender.sendMessage("콘솔에서는 사용할 수 없는 명령어입니다.".errorFormat())
                return true
            }
        }
        return false
    }

    private fun workAsync(player: Player, targetName: String) {
        Bukkit.getScheduler().runTaskAsynchronously(tunaLands, Runnable {
            val sendJson = JsonObject()
            sendJson.addProperty("uuid", player.uniqueId.toString())
            sendJson.addProperty("targetName", targetName)

            metamorphosis.send("com.salkcoding.tunalands.alba", sendJson.toString())
        })
    }
}