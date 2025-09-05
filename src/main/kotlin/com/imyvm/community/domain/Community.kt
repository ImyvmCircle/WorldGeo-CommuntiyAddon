package com.imyvm.community.domain

import com.imyvm.iwg.ImyvmWorldGeo
import com.imyvm.iwg.domain.Region
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
    PENDING_MANOR(0),
    PENDING_REALM(1),
    ACTIVE_MANOR(2),
    ACTIVE_REALM(3),
    REVOKED_MANOR(4),
    REVOKED_REALM(5);
    companion object {
        fun fromValue(value: Int): CommunityStatus {
            return CommunityStatus.entries.firstOrNull { it.value == value }
                ?: throw IllegalArgumentException("Invalid CommunityStatus value: $value")
        }
    }
}