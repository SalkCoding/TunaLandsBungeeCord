package com.salkcoding.tunalandsBC.commands.sub

import com.google.gson.JsonObject
import com.salkcoding.tunalandsBC.currentServerName
import com.salkcoding.tunalandsBC.metamorphosis
import com.salkcoding.tunalandsBC.util.errorFormat
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class Visit : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        when {
            label == "visit" -> {
                val player = sender as? Player
                if (player == null) {
                    sender.sendMessage("콘솔에서는 사용할 수 없는 명령어입니다.".errorFormat())
                    return true
                }
                when (args.size) {
                    1 -> {
                        val json = JsonObject()
                        json.addProperty("uuid", player.uniqueId.toString())
                        json.addProperty("serverName", currentServerName)
                        json.addProperty("ownerName", args[0])

                        metamorphosis.send("com.salkcoding.tunalands.visit_specific", json.toString())
                    }

                    else -> {
                        val json = JsonObject()
                        json.addProperty("uuid", player.uniqueId.toString())
                        json.addProperty("serverName", currentServerName)

                        metamorphosis.send("com.salkcoding.tunalands.visit", json.toString())
                        return true
                    }
                }
            }
        }
        return false
    }
}