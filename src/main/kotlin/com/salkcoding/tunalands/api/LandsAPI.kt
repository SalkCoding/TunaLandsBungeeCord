package com.salkcoding.tunalands.api

import com.salkcoding.tunalands.lands.LandMemberSyncDataService
import com.salkcoding.tunalands.lands.LandType
import com.salkcoding.tunalands.lands.Rank
import com.salkcoding.tunalandsBC.tunaLands
import java.util.*

object LandsAPI {

    fun getPlayerRank(uuid: UUID): Rank? = LandMemberSyncDataService.getPlayerRank(uuid)

    fun getPlayerLandsMemberList(uuid: UUID): List<String>? = LandMemberSyncDataService.getMemberNames(uuid)


    /**
     * Returns the LandType of chunk if that chunk is protected by system
     *
     * @param  worldName  world name
     * @param  chunkData  chunk_x:chunk_z
     * @return the LandType of chunk
     */
    fun getChunkLandType(worldName: String, chunkQuery: String): LandType? {
        return null
    }
}