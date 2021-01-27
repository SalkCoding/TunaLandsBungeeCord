package com.salkcoding.tunalandsbc.listener

import com.salkcoding.tunalandsbc.guiManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class InventoryClickListener : Listener {

    @EventHandler
    fun onClick(event: InventoryClickEvent) {
        guiManager.onClick(event)
    }

}