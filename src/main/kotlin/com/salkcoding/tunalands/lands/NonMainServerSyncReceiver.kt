//package com.salkcoding.tunalands.lands
//
//import com.google.gson.Gson
//import com.google.gson.reflect.TypeToken
//import com.salkcoding.tunalandsBC.metamorphosis
//import com.salkcoding.tunalandsBC.tunaLands
//import fish.evatuna.metamorphosis.redis.MetamorphosisReceiveEvent
//import org.bukkit.event.EventHandler
//import org.bukkit.event.Listener
//import org.bukkit.scheduler.BukkitRunnable
//
//class NonMainServerSyncReceiver : Listener {
//    /** Over, Nether -> Main */
//    private val CHUNK_QUERY_UPDATE_REQUEST = "com.salkcoding.tunalands.chunk_query_update_request"
//
//    /** Main -> Over, Nether
//     * Does NOT require a CHUNK_QUERY_UPDATE_REQUEST to send CHUNK_QUERY_UPDATE_RESPONSE */
//    private val CHUNK_QUERY_UPDATE_RESPONSE = "com.salkcoding.tunalands.chunk_query_update_response"
//
//    /** HashMap<ChunkQuery, Pair<WorldName, LandType>?>*/
//    val chunkQueryToLandTypeMap: HashMap<String, Pair<String, LandType>> = hashMapOf()
//
//    private val gson = Gson()
//
//    @EventHandler
//    fun onMetaReceive(e: MetamorphosisReceiveEvent) {
//        if (e.key == CHUNK_QUERY_UPDATE_RESPONSE) {
//            val typeToken = object : TypeToken<List<ChunkQueryUpdate>>() {}.type
//            val updates = gson.fromJson<List<ChunkQueryUpdate>>(e.value, typeToken)
//            updates.forEach { update ->
//                if (update.world == null || update.landType == null) {
//                    chunkQueryToLandTypeMap.remove(update.chunkQuery)
//                } else {
//                    chunkQueryToLandTypeMap[update.chunkQuery] = Pair(update.world, update.landType)
//                }
//            }
//        }
//    }
//
//    fun requestToReceiveAllData() {
//        val currentHash = chunkQueryToLandTypeMap.hashCode()
//        val currentSize = chunkQueryToLandTypeMap.size
//
//        metamorphosis.send(CHUNK_QUERY_UPDATE_REQUEST, "")
//        // unlimited retry logic
//        object : BukkitRunnable() {
//            override fun run() {
//                val futureMap = tunaLands.nonMainServerSyncReceiver!!.chunkQueryToLandTypeMap
//                if (futureMap.hashCode() == currentHash && futureMap.size == currentSize) {
//                    metamorphosis.send(CHUNK_QUERY_UPDATE_REQUEST, "")
//                } else {
//                    cancel()
//                }
//            }
//        }.runTaskTimer(tunaLands, 100, 100)
//    }
//
//    data class ChunkQueryUpdate(
//        val chunkQuery: String,
//        val world: String?,
//        val landType: LandType?
//    )
//}