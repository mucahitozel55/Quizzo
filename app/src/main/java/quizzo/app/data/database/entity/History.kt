package quizzo.app.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import quizzo.app.util.enums.MatchStatus
import quizzo.app.util.enums.MatchType
import java.util.*

@Entity(tableName = "history")
data class History(
    val type: MatchType,
    val category: String,
    @PrimaryKey(autoGenerate = false)
    val date: Date,
    val opponent: String? = "",
    val score: Int,
    val opponentScore: Int? = 0,
    val opponentImageURL: String? = "",
    val status: MatchStatus = MatchStatus.NONE
)