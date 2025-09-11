package com.imyvm.community.domain

import com.imyvm.iwg.ImyvmWorldGeo
import com.imyvm.iwg.domain.Region
import net.minecraft.text.Text
import java.util.*
import kotlin.collections.HashMap

class Community(
    val id: Int,
    val regionNumberId: Int?,
    val foundingTimeSeconds: Long,
    var member: HashMap<UUID, CommunityRole>,
    var joinPolicy: CommunityJoinPolicy,
    var status: CommunityStatus
) {
    fun getRegion(): Region? {
        val targetRegion = ImyvmWorldGeo.data.getRegionList().find {
            it.numberID == id
        } ?: return null
        return targetRegion
    }

    fun getCommunityText(): Text{
        val regionName = this.getRegion()?.name ?: "N/A"
        val memberCount = this.member.size
        val roleCounts = this.member.values.groupingBy { it }.eachCount()
        val ownerCount = roleCounts[CommunityRole.OWNER] ?: 0
        val adminCount = roleCounts[CommunityRole.ADMIN] ?: 0
        val memberOnlyCount = roleCounts[CommunityRole.MEMBER] ?: 0
        val applicantCount = roleCounts[CommunityRole.APPLICANT] ?: 0

        return Text.literal("Community ID: $id\n")
            .append(Text.literal("Region: $regionName\n"))
            .append(Text.literal("Founded: ${Date(foundingTimeSeconds * 1000)}\n"))
            .append(Text.literal("Status: ${status.name}\n"))
            .append(Text.literal("Join Policy: ${joinPolicy.name}\n"))
            .append(Text.literal("Members: $memberCount (Owners: $ownerCount, Admins: $adminCount, Members: $memberOnlyCount, Applicants: $applicantCount)\n"))
    }

}

enum class CommunityRole(val value: Int) {
    OWNER(0),
    ADMIN(1),
    MEMBER(2),
    APPLICANT(3);

    companion object {
        fun fromValue(value: Int): CommunityRole {
            return entries.firstOrNull { it.value == value }
                ?: throw IllegalArgumentException("Invalid CommunityRole value: $value")
        }
    }
}

enum class CommunityJoinPolicy(val value: Int) {
    OPEN(0),
    APPLICATION(1),
    INVITE_ONLY(2);

    companion object {
        fun fromValue(value: Int): CommunityJoinPolicy {
            return entries.firstOrNull { it.value == value }
                ?: throw IllegalArgumentException("Invalid CommunityJoinPolicy value: $value")
        }
    }
}

enum class CommunityStatus(val value: Int) {
    RECRUITING_REALM(0),
    PENDING_MANOR(1),
    PENDING_REALM(2),
    ACTIVE_MANOR(3),
    ACTIVE_REALM(4),
    REVOKED_MANOR(5),
    REVOKED_REALM(6);
    companion object {
        fun fromValue(value: Int): CommunityStatus {
            return CommunityStatus.entries.firstOrNull { it.value == value }
                ?: throw IllegalArgumentException("Invalid CommunityStatus value: $value")
        }
    }
}