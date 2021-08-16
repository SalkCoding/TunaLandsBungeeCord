package com.salkcoding.tunalands.api

import com.salkcoding.tunalands.lands.LandMemberSyncDataService
import com.salkcoding.tunalands.lands.Rank
import java.util.*

object LandsAPI {

    fun getPlayerRank(uuid: UUID): Rank? =  LandMemberSyncDataService.getPlayerRank(uuid)

    fun getPlayerLandsMemberList(uuid: UUID): List<String>? = LandMemberSyncDataService.getMemberNames(uuid)
}