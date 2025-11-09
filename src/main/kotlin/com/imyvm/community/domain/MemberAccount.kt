package com.imyvm.community.domain

import com.imyvm.community.domain.community.CommunityRoleType

data class MemberAccount (
    val joinedTime: Long,
    val basicRoleType: CommunityRoleType,
    val isCouncilMember: Boolean = false,
    val governorship: Int = -1
)