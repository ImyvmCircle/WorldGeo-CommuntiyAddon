package com.imyvm.community.domain

import com.imyvm.community.domain.community.CommunityRoleType

data class MemberAccount (
    val joinedTime: Long,
    var basicRoleType: CommunityRoleType,
    val isCouncilMember: Boolean = false,
    var governorship: Int = -1
)