package quizzo.app.data.network.socket

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import quizzo.app.Contract
import quizzo.app.data.network.response.multiplayer.Match

class MatchMakingSocket {
    private var matchMakingSocket: Socket? = null
    private val gson = GsonBuilder().create()

    fun initMatchmakingSocket(
        matchFoundCallback: (match: Match) -> Unit
    ) {
        if (matchMakingSocket == null) {
            try {
                matchMakingSocket =
                    IO.socket("${Contract.QUIZZO_SERVER_URL}matchmaking")
            } catch (e: Exception) {
                Log.d("TAG", "Socket creation: ${e.message}")
                return
            }

            matchMakingSocket!!.on("matchFound") { data ->
                val jsonElement = JsonParser.parseString(data[0].toString())
                val match = gson.fromJson<Match>(jsonElement, Match::class.java)

                matchFoundCallback(match)
            }

//            matchMakingSocket!!.on("matchFoundBot") { data ->
//                val jsonElement = JsonParser.parseString(data[0].toString())
//                val matchMaking = gson.fromJson<Match>(jsonElement, Match::class.java)
//                matchFoundCallback(matchMaking)
//            }


        }
        matchMakingSocket!!.connect()
    }

    fun findMatch(name: String, imageURL: String, uid: String) {
        val obj = JSONObject()
        obj.put("uid", uid)
        obj.put("name", name)
        obj.put("imageURL", imageURL)

        matchMakingSocket!!.emit("findMatch", obj)
    }

    fun findMatchBot() {
        matchMakingSocket!!.emit("findMatchBot", null)
    }

    fun destroySocket() {
        if (matchMakingSocket != null)
            matchMakingSocket!!.disconnect()
    }
}