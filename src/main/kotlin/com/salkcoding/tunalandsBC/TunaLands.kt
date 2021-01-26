package com.salkcoding.tunalandsBC

import com.salkcoding.tunalandsBC.bungee.*
import com.salkcoding.tunalandsBC.commands.LandCommandHandler
import com.salkcoding.tunalandsBC.commands.sub.*
import com.salkcoding.tunalandsBC.gui.GuiManager
import com.salkcoding.tunalandsBC.listener.*
import com.salkcoding.tunalandsBC.bungee.channelapi.BungeeChannelApi
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

lateinit var tunaLands: TunaLands
lateinit var guiManager: GuiManager
lateinit var bungeeApi: BungeeChannelApi

lateinit var currentServerName: String

class TunaLands : JavaPlugin() {

    override fun onEnable() {
        tunaLands = this

        saveDefaultConfig()

        guiManager = GuiManager()

        bungeeApi = BungeeChannelApi.of(this)
        //Visit
        bungeeApi.registerForwardListener("tunalands-banlist", BanListReceiver())
        bungeeApi.registerForwardListener("tunalands-visit", VisitReceiver())
        bungeeApi.registerForwardListener("tunalands-visit-connect", VisitCooldownReceiver())
        //Spawn
        bungeeApi.registerForwardListener("tunalands-spawn", SpawnCooldownReceiver())
        //Reload
        bungeeApi.registerForwardListener("tunalands-reload", ReloadReceiver())

        Bukkit.getScheduler().runTaskAsynchronously(this, Runnable {
            val future = bungeeApi.server
            currentServerName = future.get()
        })

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

        getCommand("land")!!.setExecutor(handler)

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