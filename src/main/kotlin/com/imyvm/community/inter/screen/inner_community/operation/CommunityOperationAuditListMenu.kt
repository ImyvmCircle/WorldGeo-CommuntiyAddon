package com.imyvm.community.inter.screen.inner_community.operation

import com.imyvm.community.application.interaction.screen.CommunityMenuOpener
import com.imyvm.community.domain.Community
import com.imyvm.community.domain.MemberAccount
import com.imyvm.community.inter.screen.AbstractListMenu
import com.imyvm.community.inter.screen.component.createPlayerHeadItem
import com.imyvm.community.util.Translator
import com.mojang.authlib.GameProfile
import net.minecraft.item.Items
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import java.util.*

class CommunityOperationAuditListMenu(
    syncId: Int,
    private val community: Community,
    private val playerExecutor: ServerPlayerEntity,
    private val page: Int
): AbstractListMenu(
    syncId,
    menuTitle = generateMenuTitle(community),
    page
) {

    private val playersPerPage = 35
    private val startSlot = 10
    private val endSlot = 44

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

        handlePage(applicants.size)
    }

    override fun calculateTotalPages(listSize: Int): Int {
        return (listSize / playersPerPage) + 1
    }

    override fun openNewPage(player: ServerPlayerEntity, newPage: Int) {
        CommunityMenuOpener.open(player) { syncId ->
            CommunityOperationAuditListMenu(
                syncId,
                community = community,
                playerExecutor = playerExecutor,
                page = newPage
            )
        }
    }

    private fun addApplicantButtons(applicants: List<Map.Entry<UUID, MemberAccount>>) {
        val applicantInPage = applicants.drop(page * playersPerPage).take(playersPerPage)

        var slot = startSlot
        val server = playerExecutor.server
        for (applicant in applicantInPage) {
            val uuid = applicant.key
            val name = resolvePlayerName(server, uuid)
            val objectProfile = getPlayerProfileByUuid(server, uuid) ?: continue
            addButton(
                slot = slot,
                name = name,
                itemStack = createPlayerHeadItem(name, uuid)
            ) { runOpenAuditMemberMenu(objectProfile) }
            slot = super.incrementSlotIndex(slot)
            if (slot > endSlot) break
        }
    }

    private fun runOpenAuditMemberMenu(objectProfile: GameProfile){
        CommunityMenuOpener.open(playerExecutor) { syncId ->
            CommunityOperationAuditMenu(
                syncId,
                community = community,
                playerExecutor = playerExecutor,
                playerObject = objectProfile
            )
        }
    }

    @Deprecated(
        "Temporary workaround. Will be replaced by UtilApi.getPlayerName(server: MinecraftServer, uuid: UUID?)",
        ReplaceWith("UtilApi.getPlayerName(server: MinecraftServer, uuid: UUID?)")
    )
    private fun resolvePlayerName(server: MinecraftServer, uuid: UUID?): String {
        if (uuid == null) return "?"
        return server.userCache?.getByUuid(uuid)?.get()?.name ?: uuid.toString()
    }


    @Deprecated(
        "Temporary workaround. Will be replaced by UtilApi.getPlayerProfile(server: MinecraftServer, playerUuid: UUID)",
        ReplaceWith("UtilApi.getPlayerProfile(server: MinecraftServer, playerUuid: UUID)")
    )
    private fun getPlayerProfileByUuid(server: MinecraftServer, playerUuid: UUID) =
        server.userCache?.getByUuid(playerUuid)?.orElse(null)

    companion object {
        fun generateMenuTitle(community: Community): Text = Text.of(community.generateCommunityMark() + " - Audit Requests:")
    }
}