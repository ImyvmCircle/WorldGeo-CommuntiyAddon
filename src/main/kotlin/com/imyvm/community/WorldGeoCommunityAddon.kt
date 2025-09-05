package com.imyvm.community

import CommunityDatabase
import CommunityDatabase.Companion.communities
import com.imyvm.community.application.removeExpiredApplication
import com.imyvm.community.domain.CommunityStatus
import com.imyvm.community.domain.PendingOperation
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
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

		registerExpireCheck()

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

	private fun registerExpireCheck() {
		var tickCounter = 0

		ServerTickEvents.END_SERVER_TICK.register {server ->
			tickCounter++

			if (tickCounter >= 20 * 10) {
				tickCounter = 0
				val now = System.currentTimeMillis()
				val iterator = pendingOperations.iterator()
				while (iterator.hasNext()) {
					val (uuid, operation) = iterator.next()
					if (operation.expireAt <= now) {
						val operationType = operation.type
						when(operationType) {
							com.imyvm.community.domain.PendingOperationType.CREATE_COMMUNITY_RECRUITMENT -> {
								removeExpiredApplication(uuid)
							}
							else -> {
								logger.info("Unhandled expired operation type: $operationType for player $uuid")
							}
						}
						iterator.remove()
						logger.info("Removed expired pending operation for player $uuid")
						server.playerManager.getPlayer(uuid)
							?.sendMessage(Translator.tr("pending.expired", operationType), false)
					}
				}
			}
		}
	}
}