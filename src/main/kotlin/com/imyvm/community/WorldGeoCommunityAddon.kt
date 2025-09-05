package com.imyvm.community

import CommunityDatabase
import com.imyvm.community.domain.PendingOperation
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

class WorldGeoCommunityAddon : ModInitializer {

	override fun onInitialize() {
		CommandRegistrationCallback.EVENT.register { dispatcher, registryAccess, _ ->
			register(dispatcher, registryAccess)
		}

		dataLoad()
		dataSave()

		logger.info("$MOD_ID initialized successfully.")

	}

	companion object {
		const val MOD_ID = "community"
		val logger: Logger = LoggerFactory.getLogger(MOD_ID)

		val communityData: CommunityDatabase = CommunityDatabase()
		val pendingOperations: MutableMap<UUID, PendingOperation> = mutableMapOf()

		fun dataLoad() {
			try {
				communityData.load()
			} catch (e: Exception) {
				logger.error("Failed to load community database: ${e.message}", e)
			}
		}

		fun dataSave() {
			ServerLifecycleEvents.SERVER_STOPPING.register { _ ->
				try {
					communityData.save()
				} catch (e: Exception) {
					logger.error("Failed to save community database: ${e.message}", e)
				}
			}
		}
	}
}