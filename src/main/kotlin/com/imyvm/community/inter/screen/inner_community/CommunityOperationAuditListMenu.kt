package com.imyvm.community.inter.screen.inner_community

import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.AbstractMenu
import com.imyvm.community.util.Translator
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

class CommunityOperationAuditListMenu(
    syncId: Int,
    community: Community,
    playerExecutor: ServerPlayerEntity
): AbstractMenu(
    syncId,
    menuTitle = generateMenuTitle(community)
) {
    init {
        val applicants = community.member.entries.filter { it.value.basicRoleType.name == "APPLICANT" }
        if (applicants.isEmpty()) {
            addButton(
                slot = 10,
                name = Translator.tr("ui.community.operation.audit_list.no_requests")?.string ?: "No Audit Requests",
                item = Items.DARK_OAK_SIGN
            ) {}
        } else {
            addApplicantButtons()
        }
    }

    private fun addApplicantButtons() {

    }

    companion object {
        fun generateMenuTitle(community: Community): Text = Text.of(community.generateCommunityMark() + " - Audit Requests:")
    }
}