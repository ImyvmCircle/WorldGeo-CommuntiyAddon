package com.imyvm.community

import com.imyvm.community.application.pending.checkMemberNumber
import com.imyvm.community.application.pending.removeExpiredApplication
import com.imyvm.community.domain.PendingOperation
import com.imyvm.community.domain.PendingOperationType
import com.imyvm.community.infra.CommunityConfig
import com.imyvm.community.infra.CommunityDatabase
import com.imyvm.community.inter.command.register
import com.imyvm.community.util.Translator
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

class WorldGeoCommunityAddon : ModInitializer {

	override fun onInitialize() {
		CommandRegistrationCallback.EVENT.register { dispatcher, _, _ -> register(dispatcher) }
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
			if (tickCounter >= CommunityConfig.PENDING_CHECK_INTERVAL_SECONDS.value * 20 ) {
				tickCounter = 0
				val now = System.currentTimeMillis()
				val iterator = pendingOperations.iterator()
				while (iterator.hasNext()) {
					val (uuid, operation) = iterator.next()
					if (operation.expireAt <= now) {
						val operationType = operation.type
						when(operationType) {
							PendingOperationType.CREATE_COMMUNITY_RECRUITMENT -> {
								checkMemberNumber(uuid, iterator)
								removeExpiredApplication(uuid, server)
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