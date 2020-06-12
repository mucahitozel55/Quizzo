package quizzo.app.data.network

import androidx.lifecycle.LiveData
import quizzo.app.data.database.entity.History
import quizzo.app.data.database.entity.User
import quizzo.app.data.network.response.multiplayer.Match
import quizzo.app.data.network.response.multiplayer.Player
import quizzo.app.data.network.response.question.Question

interface NetworkDataSource {

    val questionList: LiveData<List<Question>>
    val historyList: LiveData<List<History>>
    val user: LiveData<User>
    val match: LiveData<Match>

    suspend fun getAllQuestions()
    suspend fun getQuestionByCategory(category: String)

    suspend fun createUser(id: String, username: String, email: String, imageURL: String)
    //TODO: Upload image

    suspend fun postHistory(history: History, email: String)

    fun initMatchMakingSocket(uid: String)
    fun findMatch(name: String, imageURL: String, uid: String)
    fun findMatchBot()
    fun resetMatchMaking()
    fun destroyMatchMakingSocket()

    fun initMatchSocket(uid: String)
    fun matchComplete(user: Player)
    fun destroyMatchSocket()
}