package com.salkcoding.tunalandsbc

import com.salkcoding.tunalandsbc.bungee.*
import com.salkcoding.tunalandsbc.commands.LandCommandHandler
import com.salkcoding.tunalandsbc.commands.sub.*
import com.salkcoding.tunalandsbc.gui.GuiManager
import com.salkcoding.tunalandsbc.listener.*
import com.salkcoding.tunalandsbc.bungee.channelapi.BungeeChannelApi
import org.bukkit.plugin.java.JavaPlugin

lateinit var tunaLands: TunaLands
lateinit var guiManager: GuiManager
lateinit var bungeeApi: BungeeChannelApi

lateinit var currentServerName: String

class TunaLands : JavaPlugin() {

    override fun onEnable() {
        tunaLands = this

        saveDefaultConfig()
        currentServerName = config.getString("serverName")!!

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

        getCommand("tunaland")!!.setExecutor(handler)

        server.pluginManager.registerEvents(InventoryClickListener(), this)
        server.pluginManager.registerEvents(InventoryCloseListener(), this)
        server.pluginManager.registerEvents(InventoryDragListener(), this)

        logger.info("Plugin is Enabled")
    }

    override fun onDisable() {
        guiManager.allClose()

        logger.warning("Plugin is Disabled")
    }
}