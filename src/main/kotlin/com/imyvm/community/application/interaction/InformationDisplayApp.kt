package com.imyvm.community.application.interaction

import com.imyvm.community.domain.Community
import com.imyvm.community.domain.CommunityStatus
import com.imyvm.community.infra.CommunityDatabase

fun filterCommunitiesByType(type: String): List<Community> {
    return when (type) {
        "RECRUITING" -> CommunityDatabase.communities.filter { it.status == CommunityStatus.RECRUITING_REALM }
        "AUDITING" -> CommunityDatabase.communities.filter {
            it.status == CommunityStatus.PENDING_MANOR || it.status == CommunityStatus.PENDING_REALM
        }
        "ACTIVE" -> CommunityDatabase.communities.filter {
            it.status == CommunityStatus.ACTIVE_MANOR || it.status == CommunityStatus.ACTIVE_REALM
        }
        "ALL" -> CommunityDatabase.communities
        else -> emptyList()
    }
}