package com.imyvm.community.domain

import com.imyvm.community.WorldGeoCommunityAddon
import java.util.*

class Community {
    var name: String = WorldGeoCommunityAddon.MOD_ID
    var id: Int = 0

    var owner: UUID? = null
    var members: List<UUID>? = null

    var regionNumberId: Int? = null
}