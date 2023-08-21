package com.salkcoding.tunalandsBC.commands.debug

import com.salkcoding.tunalands.lands.LandMemberSyncDataService
import com.salkcoding.tunalandsBC.bukkitLinkedAPI
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class Debug : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.isOp || args.isEmpty())
            return false

        when {
            args.size == 2 && args[0] == "api" && args[1] == "show" -> {
                sender.sendMessage("SyncedMap: $LandMemberSyncDataService")
            }

            args.size == 3 && args[0] == "api" && args[1] == "rank" -> {
                sender.sendMessage(
                    "getMember: ${
                        LandMemberSyncDataService.getPlayerRank(
                            bukkitLinkedAPI.getPlayerInfo(
                                args[2]
                            ).playerUUID
                        )
                    }"
                )
            }

        }

        return true
    }
}