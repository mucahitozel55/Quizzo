package quizzo.app.data.database.typeconvertors

import androidx.room.TypeConverter
import quizzo.app.util.enums.MatchType

class MatchTypeConverter {
    @TypeConverter
    fun fromMatch(matchType: MatchType): String {
        return when (matchType) {
            MatchType.SINGLE_PLAYER -> "singleplayer"
            MatchType.MULTI_PLAYER -> "multiplayer"
            MatchType.MULTI_PLAYER_BOT-> "multiplayer_bot"
        }
    }

    @TypeConverter
    fun toMatch(matchType: String): MatchType {
        return when(matchType){
            "singleplayer"-> MatchType.SINGLE_PLAYER
            "multiplayer"-> MatchType.MULTI_PLAYER
            "multiplayer_bot"->MatchType.MULTI_PLAYER_BOT
            else -> MatchType.SINGLE_PLAYER
        }
    }
}