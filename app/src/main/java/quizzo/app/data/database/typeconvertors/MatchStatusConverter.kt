package quizzo.app.data.database.typeconvertors

import androidx.room.TypeConverter
import quizzo.app.util.enums.MatchStatus

class MatchStatusConverter {

    @TypeConverter
    fun toMatchStatus(status: String): MatchStatus {
        return when (status) {
            "win" -> MatchStatus.WIN
            "lose" -> MatchStatus.LOSE
            "draw" -> MatchStatus.DRAW
            "none" -> MatchStatus.NONE
            else -> MatchStatus.NONE
        }
    }

    @TypeConverter
    fun fromMatchStatus(status: MatchStatus): String {
        return when (status) {
            MatchStatus.WIN -> "win"
            MatchStatus.LOSE -> "lose"
            MatchStatus.DRAW -> "draw"
            MatchStatus.NONE -> "none"
        }
    }

}