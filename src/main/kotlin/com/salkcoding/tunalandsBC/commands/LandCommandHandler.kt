package com.salkcoding.tunalandsbc.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class LandCommandHandler : CommandExecutor {

    private val commandMap = HashMap<String, CommandExecutor>()

    //IgnoreCases
    fun register(command: String, executor: CommandExecutor) {
        val lowerCase = command.toLowerCase()
        if (lowerCase !in commandMap) {
            commandMap[lowerCase] = executor
        } else
            throw IllegalArgumentException("Command: $lowerCase is already registered.")
    }

    //IgnoreCases
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) return false

        //Sub command
        val newLabel = args[0].toLowerCase()
        if (newLabel in commandMap) {
            val newArgs = args.toMutableList()
            newArgs.removeFirst()
            return commandMap[newLabel]!!.onCommand(sender, command, newLabel, newArgs.toTypedArray())
        }
        return false
    }
}