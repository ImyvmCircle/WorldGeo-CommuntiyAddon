package com.imyvm.community.inter.screen.inner_community

import com.imyvm.community.application.interaction.screen.CommunityMenuOpener
import com.imyvm.community.domain.Community
import com.imyvm.community.domain.MemberAccount
import com.imyvm.community.inter.screen.AbstractMenu
import com.imyvm.community.inter.screen.component.createPlayerHeadItem
import com.imyvm.community.util.Translator
import net.minecraft.item.Items
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import java.util.*

class CommunityOperationAuditListMenu(
    syncId: Int,
    private val community: Community,
    private val playerExecutor: ServerPlayerEntity
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
            addApplicantButtons(applicants)
        }
    }

    private fun addApplicantButtons(applicants: List<Map.Entry<java.util.UUID, MemberAccount>>) {
        var slot = 10
        val server = playerExecutor.server
        for (applicant in applicants) {
            if (slot == 17) slot = 19
            if (slot == 26) slot = 28

            val uuid = applicant.key
            val name = resolvePlayerName(server, uuid)
            addButton(
                slot = slot,
                name = name,
                itemStack = createPlayerHeadItem(name, uuid)
            ) { runOpenAuditMemberMenu() }
            slot++
        }
    }

    private fun runOpenAuditMemberMenu(){
        CommunityMenuOpener.open(playerExecutor) { syncId ->
            CommunityOperationAuditMenu(
                syncId,
                community = community,
                playerExecutor = playerExecutor
            )
        }
    }

    private fun resolvePlayerName(server: MinecraftServer, uuid: UUID?): String {
        if (uuid == null) return "?"
        return server.userCache?.getByUuid(uuid)?.get()?.name ?: uuid.toString()
    }

    companion object {
        fun generateMenuTitle(community: Community): Text = Text.of(community.generateCommunityMark() + " - Audit Requests:")
    }
}