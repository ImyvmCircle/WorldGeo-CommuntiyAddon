package com.imyvm.community.domain

import com.imyvm.community.WorldGeoCommunityAddon
import java.util.*
import kotlin.collections.HashMap

class Community {
    var id: Int = 0
    var regionNumberId: Int? = null

    var member: HashMap<UUID, CommunityRole> = HashMap()
    var joinPolicy: CommunityJoinPolicy = CommunityJoinPolicy.OPEN
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