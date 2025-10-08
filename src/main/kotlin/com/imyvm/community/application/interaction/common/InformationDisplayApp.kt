package com.imyvm.community.application.interaction.common

import com.imyvm.community.domain.Community
import com.imyvm.community.domain.CommunityJoinPolicy
import com.imyvm.community.domain.CommunityStatus
import com.imyvm.community.infra.CommunityDatabase

fun filterCommunitiesByType(type: String): List<Community> {
    return when (type) {
        "ALL" -> CommunityDatabase.communities
        "JOIN-ABLE" -> CommunityDatabase.communities.filter {
            (it.status == CommunityStatus.RECRUITING_REALM || it.status == CommunityStatus.PENDING_MANOR
                    || it.status == CommunityStatus.PENDING_REALM || it.status == CommunityStatus.ACTIVE_MANOR
                    || it.status == CommunityStatus.ACTIVE_REALM) &&
                    (it.joinPolicy == CommunityJoinPolicy.OPEN || it.joinPolicy == CommunityJoinPolicy.APPLICATION)
        }
        "RECRUITING" -> CommunityDatabase.communities.filter { it.status == CommunityStatus.RECRUITING_REALM }
        "AUDITING" -> CommunityDatabase.communities.filter {
            it.status == CommunityStatus.PENDING_MANOR || it.status == CommunityStatus.PENDING_REALM
        }
        "ACTIVE" -> CommunityDatabase.communities.filter {
            it.status == CommunityStatus.ACTIVE_MANOR || it.status == CommunityStatus.ACTIVE_REALM
        }
        "REVOKED" -> CommunityDatabase.communities.filter {
            it.status == CommunityStatus.REVOKED_MANOR || it.status == CommunityStatus.REVOKED_REALM
        }
        else -> emptyList()
    }
}