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
            10000.0,
            "the price to create a manor."
        ) { obj, path ->
            obj.getDouble(path)
        }

        @JvmField
        @ConfigOption
        val PRICE_REALM = Option(
            "economy.price_realm",
            20000.0,
            "the price to create a realm."
        ) { obj, path ->
            obj.getDouble(path)
        }
    }
}