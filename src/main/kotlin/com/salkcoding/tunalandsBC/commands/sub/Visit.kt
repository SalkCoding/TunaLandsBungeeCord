package com.salkcoding.tunalandsbc.commands.sub

import com.salkcoding.tunalandsbc.bungeeApi
import com.salkcoding.tunalandsbc.currentServerName
import com.salkcoding.tunalandsbc.tunaLands
import com.salkcoding.tunalandsbc.util.errorFormat
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException

class Visit : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        when {
            label == "visit" && args.isEmpty() -> {
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
            val messageBytes = ByteArrayOutputStream()
            val messageOut = DataOutputStream(messageBytes)
            try {
                messageOut.writeUTF(player.uniqueId.toString())
                messageOut.writeUTF(currentServerName)
            } catch (exception: IOException) {
                exception.printStackTrace()
            }

            bungeeApi.forward("ALL", "tunalands-visit", messageBytes.toByteArray())
        })
    }
}