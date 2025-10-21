package com.imyvm.community.util

import com.imyvm.community.WorldGeoCommunityAddon.Companion.MOD_ID
import com.imyvm.community.infra.CommunityConfig
import com.imyvm.hoki.i18n.HokiLanguage
import com.imyvm.hoki.i18n.HokiTranslator
import net.minecraft.text.Text

object Translator : HokiTranslator() {
    private var languageInstance = createLanguage(CommunityConfig.LANGUAGE.value)

    init {
        CommunityConfig.LANGUAGE.changeEvents.register { option, _, _ ->
            languageInstance = createLanguage(option.value)
        }
    }

    fun tr(key: String?, vararg args: Any?): Text? {
        val raw = key?.let { languageInstance.get(it) }
        val formatted = if (args.isNotEmpty()) {
            raw?.let { java.text.MessageFormat.format(it, *args) }
        } else {
            raw
        }
        return formatted?.let { TextParser.parse(it) }
    }

    private fun createLanguage(languageId: String) = HokiLanguage.create(
        HokiLanguage.getResourcePath(MOD_ID, languageId)
            .let { Translator::class.java.getResourceAsStream(it) }
    )
}
