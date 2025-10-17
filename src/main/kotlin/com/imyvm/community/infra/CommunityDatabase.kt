package com.imyvm.community.infra

import com.imyvm.community.domain.Community
import com.imyvm.community.domain.community.CommunityJoinPolicy
import com.imyvm.community.domain.community.CommunityRole
import com.imyvm.community.domain.community.CommunityStatus
import net.fabricmc.loader.api.FabricLoader
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.nio.file.Path
import java.util.*

object CommunityDatabase {

    private const val DATABASE_FILENAME = "iwg_community.db"
    lateinit var communities: MutableList<Community>

    @Throws(IOException::class)
    fun save() {
        val file = this.getDatabasePath()
        DataOutputStream(file.toFile().outputStream()).use { stream ->
            stream.writeInt(communities.size)
            for (community in communities) {
                saveCommunityRegionNumberId(stream,community)
                saveCommunityMember(stream,community)
                stream.writeInt(community.joinPolicy.value)
                stream.writeInt(community.status.value)
            }
        }
    }

    @Throws(IOException::class)
    fun load() {
        val file = this.getDatabasePath()
        if (!file.toFile().exists()) {
            communities = mutableListOf()
            return
        }

        DataInputStream(file.toFile().inputStream()).use { stream ->
            val size = stream.readInt()
            communities = ArrayList(size)
            for (i in 0 until size) {
                val regionNumberId = loadCommunityRegionNumberId(stream)
                val memberCount = stream.readInt()
                val memberMap = loadMemberMap(stream, memberCount)
                val joinPolicy = CommunityJoinPolicy.fromValue(stream.readInt())
                val status = CommunityStatus.fromValue(stream.readInt())

                val community = Community(
                    regionNumberId = regionNumberId,
                    member = memberMap,
                    joinPolicy = joinPolicy,
                    status = status
                )
                communities.add(community)
            }
        }
    }

    fun addCommunity(community: Community) {
        communities.add(community)
    }

    fun getCommunityById(regionId: Int): Community? {
        return communities.find { it.regionNumberId == regionId }
    }

    private fun getDatabasePath(): Path {
        return FabricLoader.getInstance().gameDir
            .resolve("world")
            .resolve(DATABASE_FILENAME)
    }

    private fun saveCommunityRegionNumberId(stream: DataOutputStream, community: Community){
        if (community.regionNumberId == null) {
            stream.writeBoolean(false)
        } else {
            stream.writeBoolean(true)
            stream.writeInt(community.regionNumberId)
        }
    }

    private fun saveCommunityMember(stream: DataOutputStream, community: Community){
        stream.writeInt(community.member.size)
        for ((uuid, role) in community.member) {
            stream.writeUTF(uuid.toString())
            stream.writeInt(role.value)
        }
    }

    private fun loadCommunityRegionNumberId(stream: DataInputStream): Int? {
        return if (stream.readBoolean()) {
            stream.readInt()
        } else {
            null
        }
    }

    private fun loadMemberMap(stream: DataInputStream, memberCount: Int): HashMap<UUID, CommunityRole> {
        val memberMap = HashMap<UUID, CommunityRole>(memberCount)
        for (j in 0 until memberCount) {
            val uuid = UUID.fromString(stream.readUTF())
            val role = CommunityRole.fromValue(stream.readInt())
            memberMap[uuid] = role
        }
        return memberMap
    }
}
