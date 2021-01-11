package com.salkcoding.tunalandsBC

import com.salkcoding.tunalandsBC.bungee.BanListReceiver
import com.salkcoding.tunalandsBC.bungee.SpawnCooldownReceiver
import com.salkcoding.tunalandsBC.bungee.VisitCooldownReceiver
import com.salkcoding.tunalandsBC.bungee.VisitReceiver
import com.salkcoding.tunalandsBC.commands.LandCommandHandler
import com.salkcoding.tunalandsBC.commands.sub.*
import com.salkcoding.tunalandsBC.gui.GuiManager
import com.salkcoding.tunalandsBC.listener.*
import io.github.leonardosnt.bungeechannelapi.BungeeChannelApi
import org.bukkit.plugin.java.JavaPlugin

const val channelName = "BungeeCord"

lateinit var tunaLands: TunaLands
lateinit var guiManager: GuiManager

lateinit var bungeeApi: BungeeChannelApi


class TunaLands : JavaPlugin() {

    lateinit var serverName:String

    override fun onEnable() {
        tunaLands = this

        saveDefaultConfig()
        serverName = config.getString("serverName")!!

        guiManager = GuiManager()

        bungeeApi = BungeeChannelApi.of(this)
        //Visit
        bungeeApi.registerForwardListener("tunalands-banlist", BanListReceiver())
        bungeeApi.registerForwardListener("tunalands-visit", VisitReceiver())
        bungeeApi.registerForwardListener("tunalands-visit-connect", VisitCooldownReceiver())
        //Spawn
        bungeeApi.registerForwardListener("tunalands-spawn", SpawnCooldownReceiver())

        val handler = LandCommandHandler()
        handler.register("accept", Accept())
        handler.register("alba", Alba())
        handler.register("ban", Ban())
        handler.register("banlist", BanList())
        handler.register("cancel", Cancel())
        handler.register("delete", Delete())
        handler.register("demote", Demote())
        handler.register("deny", Deny())
        handler.register("hego", Hego())
        handler.register("help", Help())
        handler.register("invite", Invite())
        handler.register("kick", Kick())
        handler.register("leave", Leave())
        handler.register("promote", Promote())
        handler.register("setleader", SetLeader())
        handler.register("spawn", Spawn())
        handler.register("unban", Unban())
        handler.register("visit", Visit())

        //handler.register("debug", Debug())

        getCommand("land")!!.setExecutor(handler)

        server.pluginManager.registerEvents(InventoryClickListener(), this)
        server.pluginManager.registerEvents(InventoryCloseListener(), this)
        server.pluginManager.registerEvents(InventoryDragListener(), this)
        server.pluginManager.registerEvents(PlayerConnectListener(), this)
        server.messenger.registerOutgoingPluginChannel(this, channelName)

        logger.info("Plugin is Enabled")
    }

    override fun onDisable() {
        guiManager.allClose()

        logger.warning("Plugin is Disabled")
    }
}