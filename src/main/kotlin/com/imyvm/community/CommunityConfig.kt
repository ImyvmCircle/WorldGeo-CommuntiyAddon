package com.imyvm.community

import com.imyvm.hoki.config.ConfigOption
import com.imyvm.hoki.config.HokiConfig
import com.imyvm.hoki.config.Option

class CommunityConfig : HokiConfig("Community.conf") {
    companion object {
        @JvmField
        @ConfigOption
        val PRICE_MANOR = Option(
            "economy.price_manor",
            1500000L,
            "the price to create a manor."
        ) { obj, path ->
            obj.getLong(path)
        }

        @JvmField
        @ConfigOption
        val PRICE_REALM = Option(
            "economy.price_realm",
            3000000L,
            "the price to create a realm."
        ) { obj, path ->
            obj.getLong(path)
        }
    }
}