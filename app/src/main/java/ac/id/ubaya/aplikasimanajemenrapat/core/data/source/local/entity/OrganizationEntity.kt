package ac.id.ubaya.aplikasimanajemenrapat.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "organizations")
data class OrganizationEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "code")
    val code: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "profilePic")
    var profilePic: String,

    @ColumnInfo(name = "leaderboard_start")
    var leaderboardStart: String?,

    @ColumnInfo(name = "leaderboard_end")
    var leaderboardEnd: String?,
)
