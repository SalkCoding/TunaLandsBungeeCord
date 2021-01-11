package com.salkcoding.tunalandsBC.commands.sub

import com.salkcoding.tunalandsBC.bungeeApi
import com.salkcoding.tunalandsBC.tunaLands
import com.salkcoding.tunalandsBC.util.errorFormat
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException

class Kick : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        when {
            label == "kick" && args.size == 1 -> {
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
            val messageBytes = ByteArrayOutputStream()
            val messageOut = DataOutputStream(messageBytes)
            try {
                messageOut.writeUTF(player.uniqueId.toString())
                messageOut.writeUTF(targetName)
            } catch (exception: IOException) {
                exception.printStackTrace()
            }

            bungeeApi.forward("ALL", "tunalands-kick", messageBytes.toByteArray())
        })
    }
}