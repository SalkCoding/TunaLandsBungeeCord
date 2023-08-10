package com.salkcoding.tunalandsBC

import com.salkcoding.tunalands.lands.LandMemberSyncDataService
import com.salkcoding.tunalands.lands.NonMainServerSyncReceiver
import com.salkcoding.tunalandsBC.bungee.*
import com.salkcoding.tunalandsBC.commands.LandCommandHandler
import com.salkcoding.tunalandsBC.commands.sub.*
import com.salkcoding.tunalandsBC.gui.GuiManager
import com.salkcoding.tunalandsBC.listener.*
import fish.evatuna.metamorphosis.Metamorphosis
import me.baiks.bukkitlinked.BukkitLinked
import me.baiks.bukkitlinked.api.BukkitLinkedAPI
import org.bukkit.plugin.java.JavaPlugin

lateinit var tunaLands: TunaLands
lateinit var guiManager: GuiManager
lateinit var metamorphosis: Metamorphosis
lateinit var bukkitLinkedAPI: BukkitLinkedAPI

lateinit var currentServerName: String

class TunaLands : JavaPlugin() {
    var nonMainServerSyncReceiver: NonMainServerSyncReceiver? = null

    override fun onEnable() {
        tunaLands = this

        saveDefaultConfig()
        currentServerName = config.getString("serverName")!!

        guiManager = GuiManager()

        val tempMetamorphosis = server.pluginManager.getPlugin("Metamorphosis") as? Metamorphosis
        if (tempMetamorphosis == null) {
            server.pluginManager.disablePlugin(this)
            logger.warning("Metamorphosis is not running on this server!")
            return
        }
        metamorphosis = tempMetamorphosis

        val tempBukkitLinked = server.pluginManager.getPlugin("BukkitLinked") as? BukkitLinked
        if (tempBukkitLinked == null) {
            server.pluginManager.disablePlugin(this)
            logger.warning("BukkitLinked is not running on this server!")
            return
        }
        bukkitLinkedAPI = tempBukkitLinked.api

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
        handler.register("recommend", Recommend())
        handler.register("setleader", SetLeader())
        handler.register("spawn", Spawn())
        handler.register("unban", Unban())
        handler.register("visit", Visit())

        getCommand("tunaland")!!.setExecutor(handler)

        //Banlist
        server.pluginManager.registerEvents(BanListReceiver(), this)
        //Recommend
        server.pluginManager.registerEvents(RecommendReceiver(), this)
        //Visit
        server.pluginManager.registerEvents(VisitReceiver(), this)
        server.pluginManager.registerEvents(VisitCooldownReceiver(), this)
        //Spawn
        server.pluginManager.registerEvents(SpawnCooldownReceiver(), this)

        server.pluginManager.registerEvents(InventoryClickListener(), this)
        server.pluginManager.registerEvents(InventoryCloseListener(), this)
        server.pluginManager.registerEvents(InventoryDragListener(), this)

        // used for getting members list

        server.pluginManager.registerEvents(LandMemberSyncDataService, this)

        nonMainServerSyncReceiver = NonMainServerSyncReceiver()
        nonMainServerSyncReceiver!!.requestToReceiveAllData()
        server.pluginManager.registerEvents(nonMainServerSyncReceiver!!, this)

        logger.info("Plugin is Enabled")
    }

    override fun onDisable() {
        guiManager.allClose()

        logger.warning("Plugin is Disabled")
    }
}