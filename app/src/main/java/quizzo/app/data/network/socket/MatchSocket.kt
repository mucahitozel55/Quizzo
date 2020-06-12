package quizzo.app.data.network.socket

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import quizzo.app.Contract
import quizzo.app.data.network.response.multiplayer.MatchComplete
import quizzo.app.data.network.response.multiplayer.Player

class MatchSocket {
    private var matchSocket: Socket? = null
    private val gson = GsonBuilder().create()

    fun initMatchSocket(
        matchID: String,
        uid: String,
        matchCompleteCallback: (score: Int) -> Unit
    ) {
        if (matchSocket == null) {
            try {
                val options = IO.Options()
                options.query = "matchID=$matchID"
                matchSocket =
                    IO.socket("${Contract.QUIZZO_SERVER_URL}match", options)
            } catch (e: Exception) {
                Log.d("TAG", "Socket creation: ${e.message}")
                return
            }

            matchSocket!!.on("matchComplete") { data ->
                val jsonElement = JsonParser.parseString(data[0].toString())
                val matchComplete =
                    gson.fromJson<MatchComplete>(jsonElement, MatchComplete::class.java)

                if(matchComplete.player1==uid){
                    matchCompleteCallback(matchComplete.score2)
                }else{
                    matchCompleteCallback(matchComplete.score1)
                }
            }
        }
        matchSocket!!.connect()
    }

    fun matchComplete(user: Player, matchID: String) {
        val obj = JSONObject()
        obj.put("uid", user.uid)
        obj.put("score", user.score)
        obj.put("matchID", matchID)

        matchSocket!!.emit("complete", obj)
    }

    fun destroySocket() {
        if (matchSocket != null)
            matchSocket!!.disconnect()
    }
}