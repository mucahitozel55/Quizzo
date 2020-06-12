package quizzo.app.data.network.converters

import android.annotation.SuppressLint
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import quizzo.app.data.database.entity.History
import quizzo.app.data.database.typeconvertors.MatchStatusConverter
import quizzo.app.data.database.typeconvertors.MatchTypeConverter
import java.lang.reflect.Type
import java.text.SimpleDateFormat

class HistoryConverter : JsonSerializer<History> {
    @SuppressLint("SimpleDateFormat")
    override fun serialize(
        history: History?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty("match", MatchTypeConverter().fromMatch(history!!.type))
        jsonObject.addProperty("category",history.category)
        jsonObject.addProperty("date", SimpleDateFormat("yyyy MM dd HH mm ss").format(history.date))
        jsonObject.addProperty("opponent", history.opponent)
        jsonObject.addProperty("score", history.score)
        jsonObject.addProperty("opponentScore", history.opponentScore)
        jsonObject.addProperty("opponentImageURL",history.opponentImageURL)
        jsonObject.addProperty("status", MatchStatusConverter().fromMatchStatus(history.status))

        return jsonObject
    }
}