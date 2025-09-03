package com.imyvm.community

import com.imyvm.iwg.RegionDatabase
import com.imyvm.iwg.domain.PlayerRegionChecker
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.util.math.BlockPos
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class WorldGeoCommunityAddon : ModInitializer {

	override fun onInitialize() {

		dataLoad()
		dataSave()

		logger.info("$MOD_ID initialized.")

	}

	companion object {
		const val MOD_ID = "community"
		val logger: Logger = LoggerFactory.getLogger(MOD_ID)
		val data: CommunityDatabase = CommunityDatabase()

		fun dataLoad() {
			try {
				data.load()
			} catch (e: Exception) {
				logger.error("Failed to load community database: ${e.message}", e)
			}
		}

		fun dataSave() {
			ServerLifecycleEvents.SERVER_STOPPING.register { _ ->
				try {
					data.save()
				} catch (e: Exception) {
					logger.error("Failed to save community database: ${e.message}", e)
				}
			}
		}
	}
}