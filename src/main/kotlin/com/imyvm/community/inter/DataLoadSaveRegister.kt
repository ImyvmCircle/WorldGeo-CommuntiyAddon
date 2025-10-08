package com.imyvm.community.inter

import com.imyvm.community.WorldGeoCommunityAddon
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents

fun registerDataLoadAndSave(){
    dataLoad()
}

fun dataLoad() {
    try {
        WorldGeoCommunityAddon.communityData.load()
    } catch (e: Exception) {
        WorldGeoCommunityAddon.logger.error("Failed to load community database: ${e.message}", e)
    }
}

fun dataSave() {
    ServerLifecycleEvents.SERVER_STOPPING.register { _ ->
        try {
            WorldGeoCommunityAddon.communityData.save()
        } catch (e: Exception) {
            WorldGeoCommunityAddon.logger.error("Failed to save community database: ${e.message}", e)
        }
    }
}