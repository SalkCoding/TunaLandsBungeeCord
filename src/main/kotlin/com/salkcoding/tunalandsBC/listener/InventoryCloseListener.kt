package com.salkcoding.tunalandsbc.listener

import com.salkcoding.tunalandsbc.guiManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent

class InventoryCloseListener : Listener {

    @EventHandler
    fun onClose(event: InventoryCloseEvent) {
        guiManager.onClose(event)
    }

}