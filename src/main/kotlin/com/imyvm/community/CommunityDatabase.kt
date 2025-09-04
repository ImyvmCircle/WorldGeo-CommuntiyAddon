import com.imyvm.community.domain.Community
import com.imyvm.community.domain.CommunityJoinPolicy
import com.imyvm.community.domain.CommunityRole
import com.imyvm.community.domain.CommunityStatus
import net.fabricmc.loader.api.FabricLoader
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.nio.file.Path
import java.util.*

class CommunityDatabase {

    @Throws(IOException::class)
    fun save() {
        val file = this.getDatabasePath()
        DataOutputStream(file.toFile().outputStream()).use { stream ->
            stream.writeInt(communities.size)
            for (community in communities) {
                stream.writeInt(community.id)

                if (community.regionNumberId == null) {
                    stream.writeBoolean(false)
                } else {
                    stream.writeBoolean(true)
                    stream.writeInt(community.regionNumberId)
                }

                stream.writeLong(community.foundingTimeSeconds)

                stream.writeInt(community.member.size)
                for ((uuid, role) in community.member) {
                    stream.writeUTF(uuid.toString())
                    stream.writeInt(role.value)
                }

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
                val id = stream.readInt()

                val regionNumberId = if (stream.readBoolean()) {
                    stream.readInt()
                } else {
                    null
                }

                val foundingTimeSeconds = stream.readLong()

                val memberCount = stream.readInt()
                val memberMap = HashMap<UUID, CommunityRole>(memberCount)
                for (j in 0 until memberCount) {
                    val uuid = UUID.fromString(stream.readUTF())
                    val role = CommunityRole.fromValue(stream.readInt())
                    memberMap[uuid] = role
                }

                val joinPolicy = CommunityJoinPolicy.fromValue(stream.readInt())
                val status = CommunityStatus.fromValue(stream.readInt())

                val community = Community(
                    id = id,
                    regionNumberId = regionNumberId,
                    foundingTimeSeconds = foundingTimeSeconds,
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
