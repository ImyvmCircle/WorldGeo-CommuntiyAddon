package com.imyvm.community

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import org.slf4j.Logger
import org.slf4j.LoggerFactory

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