package com.salkcoding.tunalandsBC.lands

import java.util.*

class Lands(
    val ownerUUID: UUID,
    val open: Boolean,
    val memberSize: Int,
    val visitorCount: Long,
    val createdMillisecond: Long,
    val lore: MutableList<String>
)