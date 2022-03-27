package com.salkcoding.tunalandsBC.commands.sub

import com.google.gson.JsonObject
import com.salkcoding.tunalandsBC.metamorphosis
import com.salkcoding.tunalandsBC.tunaLands
import com.salkcoding.tunalandsBC.util.errorFormat
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class Unban : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        when {
            label == "unban" && args.size == 1 -> {
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

            metamorphosis.send("com.salkcoding.tunalands.unban", sendJson.toString())
        })
    }
}