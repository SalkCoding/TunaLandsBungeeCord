package com.salkcoding.tunalands.lands

import fish.evatuna.metamorphosis.redis.MetamorphosisReceiveEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.*

class DeleteSyncReceiver : Listener {

    @EventHandler
    fun onReceive(event: MetamorphosisReceiveEvent) {
        when (event.key) {
            "com.salkcoding.tunalands.sync_ban",
            "com.salkcoding.tunalands.sync_kick",
            "com.salkcoding.tunalands.sync_leave",
            "com.salkcoding.tunalands.sync_delete"-> {
                val uuid = UUID.fromString(event.value)
                LandMemberSyncDataService.removeData(uuid)
            }

            else -> return
        }
    }
}