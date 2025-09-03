package com.imyvm.community

import com.imyvm.community.domain.Community
import net.fabricmc.loader.api.FabricLoader
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.nio.file.Path
import java.util.*
import kotlin.collections.ArrayList

class CommunityDatabase {
    @Throws(IOException::class)
    fun save() {
        val file = this.getDatabasePath()
        DataOutputStream(file.toFile().outputStream()).use { stream ->
            stream.writeInt(communities.size)
            for (community in communities) {
                stream.writeUTF(community.name)
                stream.writeInt(community.id)

                if (community.owner == null) {
                    stream.writeBoolean(false)
                } else {
                    stream.writeBoolean(true)
                    stream.writeUTF(community.owner.toString())
                }

                if (community.members == null) {
                    stream.writeInt(0)
                } else {
                    stream.writeInt(community.members!!.size)
                    for (m in community.members!!) {
                        stream.writeUTF(m.toString())
                    }
                }

                if (community.regionNumberId == null) {
                    stream.writeBoolean(false)
                } else {
                    stream.writeBoolean(true)
                    stream.writeInt(community.regionNumberId!!)
                }
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
                val community = Community()

                community.name = stream.readUTF()
                community.id = stream.readInt()

                if (stream.readBoolean()) {
                    community.owner = UUID.fromString(stream.readUTF())
                }

                val memberCount = stream.readInt()
                val memberList = mutableListOf<UUID>()
                for (j in 0 until memberCount) {
                    memberList.add(UUID.fromString(stream.readUTF()))
                }
                community.members = memberList

                if (stream.readBoolean()) {
                    community.regionNumberId = stream.readInt()
                }

                communities.add(community)
            }
        }
    }

    private fun getDatabasePath(): Path {
        return FabricLoader.getInstance().gameDir
            .resolve("world")
            .resolve(DATABASE_FILENAME)
    }

    companion object {
        private const val DATABASE_FILENAME = "iwg_community.db"
        lateinit var communities: MutableList<Community>
    }
}