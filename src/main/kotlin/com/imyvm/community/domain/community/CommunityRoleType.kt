package com.imyvm.community.domain.community

enum class CommunityRoleType(val value: Int) {
    OWNER(0),
    ADMIN(1),
    MEMBER(2),
    APPLICANT(3);

    companion object {
        fun fromValue(value: Int): CommunityRoleType {
            return entries.firstOrNull { it.value == value }
                ?: throw IllegalArgumentException("Invalid CommunityRole value: $value")
        }
    }
}