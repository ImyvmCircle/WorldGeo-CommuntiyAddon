package com.imyvm.community.domain

import com.imyvm.community.domain.community.MemberRoleType
import net.minecraft.text.Text

data class MemberAccount (
    val joinedTime: Long,
    var basicRoleType: MemberRoleType,
    val isCouncilMember: Boolean = false,
    var governorship: Int = -1,
    var mail: ArrayList<Text> = arrayListOf()
)