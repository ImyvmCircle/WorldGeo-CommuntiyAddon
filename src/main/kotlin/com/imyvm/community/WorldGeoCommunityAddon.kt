package com.imyvm.community

import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object WorldGeoCommunityAddon : ModInitializer {

    private val logger = LoggerFactory.getLogger("community")

	override fun onInitialize() {

		logger.info("Hello Fabric world!")

	}
}