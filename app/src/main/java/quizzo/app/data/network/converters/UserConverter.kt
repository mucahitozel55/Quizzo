package quizzo.app.data.network.converters

import android.annotation.SuppressLint
import android.util.Log
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import quizzo.app.data.database.entity.History
import quizzo.app.data.database.typeconvertors.MatchStatusConverter
import quizzo.app.data.database.typeconvertors.MatchTypeConverter
import quizzo.app.data.network.response.user.UserResponse
import quizzo.app.data.network.response.user.UserWithHistoryList
import java.lang.reflect.Type
import java.text.SimpleDateFormat

class UserConverter : JsonDeserializer<UserResponse> {
    @SuppressLint("SimpleDateFormat")
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): UserResponse {
        val user = json!!.asJsonObject.get("user").asJsonObject
        val historyListArr = user.get("history").asJsonArray
        val historyList = ArrayList<History>()
        for (his in historyListArr) {
            val h = his.asJsonObject
            var opponent = ""
            if (!h.get("opponent").isJsonNull)
                opponent = h.get("opponent").asString

            val history = History(
                score = h.get("score").asInt,
                category = h.get("category").asString!!,
                date = SimpleDateFormat("yyyy MM dd HH mm ss").parse(h.get("date").asString)!!,
                type = MatchTypeConverter().toMatch(h.get("match").asString),
                status = MatchStatusConverter().toMatchStatus(h.get("status").asString),
                opponent = opponent,
                opponentScore = h.get("opponentScore").asInt,
                opponentImageURL = h.get("opponentImageURL").asString
            )
            historyList.add(history)
        }
        return UserResponse(
            UserWithHistoryList(
                user.get("email").asString,
                historyList,
                user.get("id").asString,
                user.get("imageURL").asString,
                user.get("username").asString
            )
        )
    }
}