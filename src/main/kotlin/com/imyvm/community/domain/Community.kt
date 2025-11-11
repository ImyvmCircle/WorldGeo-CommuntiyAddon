package com.imyvm.community.domain

import com.imyvm.community.domain.community.CommunityJoinPolicy
import com.imyvm.community.domain.community.CommunityStatus
import com.imyvm.community.domain.community.MemberRoleType
import com.imyvm.community.util.Translator
import com.imyvm.community.util.getFormattedMillsHour
import com.imyvm.iwg.application.region.parseFoundingTimeFromRegionId
import com.imyvm.iwg.domain.Region
import com.imyvm.iwg.inter.api.PlayerInteractionApi
import com.imyvm.iwg.inter.api.RegionDataApi
import net.minecraft.server.network.ServerPlayerEntity
import java.util.*

class Community(
    val regionNumberId: Int?,
    var member: HashMap<UUID, MemberAccount>,
    var joinPolicy: CommunityJoinPolicy,
    var status: CommunityStatus
) {
    fun generateCommunityMark(): String {
        return RegionDataApi.getRegion(this.regionNumberId!!)?.name ?: "Community #${this.regionNumberId}"
    }

    fun getFormattedFoundingTime(): String {
        val foundingTimeMillis = this.regionNumberId?.let { parseRegionFoundingTime(it) }
        return getFormattedMillsHour(foundingTimeMillis ?: 0L)
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

    fun getOwnerUUID(): UUID? {
        return this.member.entries.filter { it.value.basicRoleType.name == "OWNER" }
            .map { it.key }
            .firstOrNull()
    }

    fun getAdminUUIDs(): List<UUID> {
        return this.member.entries.filter { it.value.basicRoleType.name == "ADMIN" }.map { it.key }
    }

    fun getMemberUUIDs(): List<UUID> {
        return this.member.entries
            .filter { it.value.basicRoleType.name == "MEMBER" }
            .map { it.key }
    }

    fun getMemberRole(playerUuid: UUID): MemberRoleType? {
        return member[playerUuid]?.basicRoleType
    }

    fun isManageable(playerExecutor: ServerPlayerEntity, targetPlayerUuid: UUID): Boolean {
        val executorRole = getMemberRole(playerExecutor.uuid) ?: return false
        val targetRole = getMemberRole(targetPlayerUuid) ?: return false
        if (!analyzeByRole(executorRole, targetRole)) return false
        if (!analyzeByPrivilegeStatus(executorRole, targetPlayerUuid)) return false
        return true
    }

    private fun analyzeByRole(executorRole: MemberRoleType, targetRole: MemberRoleType): Boolean {
        return when (executorRole) {
            MemberRoleType.OWNER -> targetRole != MemberRoleType.OWNER
            MemberRoleType.ADMIN -> targetRole == MemberRoleType.MEMBER
            MemberRoleType.MEMBER -> false
            MemberRoleType.APPLICANT -> false
            MemberRoleType.REFUSED -> false
        }
    }

    private fun analyzeByPrivilegeStatus(executorRole: MemberRoleType, targetPlayerUuid: UUID): Boolean {
        if (executorRole == MemberRoleType.OWNER) return true
        else if (executorRole == MemberRoleType.ADMIN) {
            val memberAccount = member[targetPlayerUuid] ?: return false
            if (memberAccount.isCouncilMember || memberAccount.governorship != -1) {
                return false
            }
        } else {
            return false
        }
        return true
    }

    @Deprecated("Temporary workaround. Will be replaced by UtilApi.parseRegionFoundingTime()",
        ReplaceWith("UtilApi.parseRegionFoundingTime(regionNumberId)")
    )
    private fun parseRegionFoundingTime(regionNumberId: Int): Long {
        return parseFoundingTimeFromRegionId(regionNumberId)
    }
}