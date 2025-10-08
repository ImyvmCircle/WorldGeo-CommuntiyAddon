package com.imyvm.community

import com.imyvm.community.domain.PendingOperation
import com.imyvm.community.infra.CommunityDatabase
import com.imyvm.community.inter.command.register
import com.imyvm.community.inter.event.registerExpireCheck
import com.imyvm.community.inter.registerDataLoadAndSave
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

class WorldGeoCommunityAddon : ModInitializer {

	override fun onInitialize() {
		registerDataLoadAndSave()
		registerExpireCheck()

		CommandRegistrationCallback.EVENT.register { dispatcher, _, _ -> register(dispatcher) }
		logger.info("$MOD_ID initialized successfully.")
	}

	companion object {
		const val MOD_ID = "community"
		val logger: Logger = LoggerFactory.getLogger(MOD_ID)

		val communityData: CommunityDatabase = CommunityDatabase()
		val pendingOperations: MutableMap<UUID, PendingOperation> = mutableMapOf()
	}
}