package com.salkcoding.tunalands.lands

import fish.evatuna.metamorphosis.kafka.KafkaReceiveEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.*

object LandMemberSyncDataService : Listener {
    private val EVENT_KEY = "com.salkcoding.tunalands.update_land_member_change"
    private val landByPlayerUUIDMap: HashMap<UUID, List<TunaLandsPlayerDetails>> = hashMapOf()

    data class TunaLandsPlayerDetails(val uuid: UUID, val name: String, val rank: Rank)

    @EventHandler
    fun onReceived(event: KafkaReceiveEvent) {
        if (event.key !== EVENT_KEY) return

        intake(event.value)

    }

    fun getPlayerRank(uuid: UUID): Rank? = landByPlayerUUIDMap[uuid]?.firstOrNull { it.uuid == uuid }?.rank

    fun getMemberNames(uuid: UUID): List<String>? = landByPlayerUUIDMap[uuid]?.map { it.name }

    private fun intake(value: String) {
        // value is in the format of uuid1,name1,rank1;uuid2,name2,rank2
        val players = value.split(";")
            .map { it.split(",") }
            .map { rawData ->
                TunaLandsPlayerDetails(UUID.fromString(rawData[0]), rawData[1], Rank.valueOf(rawData[2]))
            }

        players
            .forEach { playerDetails ->
                landByPlayerUUIDMap[playerDetails.uuid] = players
            }
    }

}