package com.imyvm.community.inter.helper

import com.mojang.brigadier.suggestion.SuggestionProvider
import net.minecraft.server.command.ServerCommandSource
import java.util.concurrent.CompletableFuture

val LIST_TYPE_PROVIDER: SuggestionProvider<ServerCommandSource> = SuggestionProvider { _, builder ->
    listOf("recruiting", "auditing", "active", "all").forEach { builder.suggest(it) }
    CompletableFuture.completedFuture(builder.build())
}

val COMMUNITY_TYPE_PROVIDER: SuggestionProvider<ServerCommandSource> = SuggestionProvider { _, builder ->
    listOf("manor", "realm").forEach { builder.suggest(it) }
    CompletableFuture.completedFuture(builder.build())
}

val BINARY_CHOICE_SUGGESTION_PROVIDER: SuggestionProvider<ServerCommandSource> = SuggestionProvider { _, builder ->
    listOf("yes", "no").forEach { builder.suggest(it) }
    CompletableFuture.completedFuture(builder.build())
}
