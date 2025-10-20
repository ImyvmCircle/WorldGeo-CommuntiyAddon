package com.imyvm.community.domain

import com.imyvm.community.domain.community.CommunityJoinPolicy
import com.imyvm.community.domain.community.CommunityRole
import com.imyvm.community.domain.community.CommunityStatus
import com.imyvm.community.infra.CommunityConfig.Companion.TIMEZONE
import com.imyvm.community.util.Translator
import com.imyvm.iwg.application.region.parseFoundingTimeFromRegionId
import com.imyvm.iwg.domain.Region
import com.imyvm.iwg.inter.api.PlayerInteractionApi
import com.imyvm.iwg.inter.api.RegionDataApi
import net.minecraft.server.network.ServerPlayerEntity
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Community(
    val regionNumberId: Int?,
    var member: HashMap<UUID, CommunityRole>,
    var joinPolicy: CommunityJoinPolicy,
    var status: CommunityStatus
) {

    fun generateCommunityMark(): String {
        return RegionDataApi.getRegion(this.regionNumberId!!)?.name ?: "Community #${this.regionNumberId}"
    }

    fun getFormattedFoundingTime(): String {
        val foundingTimeMillis = this.regionNumberId?.let { parseFoundingTimeFromRegionId(it) }

        val timezone = TIMEZONE.value
        val zoneId = ZoneId.of(timezone)

        val dateTime = ZonedDateTime.ofInstant(foundingTimeMillis?.let { Instant.ofEpochMilli(it) }, zoneId)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hha (XXX)")
        return dateTime.format(formatter)
    }

    fun getRegion(): Region? {
        if (regionNumberId == null) return null
        return RegionDataApi.getRegion(regionNumberId)
    }

    fun sendCommunityRegionDescription(player: ServerPlayerEntity) {
        val region = getRegion()
        if(region != null){
            PlayerInteractionApi.queryRegionInfo(player, region)
        } else {
            player.sendMessage(Translator.tr("community.description.no_region", regionNumberId))
        }
    }

    fun getMemberRole(playerUuid: UUID): CommunityRole? {
        return member[playerUuid]
    }
}