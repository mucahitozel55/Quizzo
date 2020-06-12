package quizzo.app.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import quizzo.app.data.database.entity.History
import quizzo.app.data.database.entity.User
import quizzo.app.data.network.response.multiplayer.Match
import quizzo.app.data.network.response.multiplayer.Player
import quizzo.app.data.network.response.question.Question
import quizzo.app.data.network.service.QuizzoApiService
import quizzo.app.data.network.socket.MatchMakingSocket
import quizzo.app.data.network.socket.MatchSocket

class NetworkDataSourceImpl(
    private val quizzoApiService: QuizzoApiService,
    private val matchMakingSocket: MatchMakingSocket,
    private val matchSocket: MatchSocket
) : NetworkDataSource {

    private val mutableQuestionList = MutableLiveData<List<Question>>()
    private val mutableHistoryList = MutableLiveData<List<History>>()
    private val mutableUser = MutableLiveData<User>()
    private val mutableMatch = MutableLiveData<Match>()

    override val questionList: LiveData<List<Question>>
        get() = mutableQuestionList
    override val historyList: LiveData<List<History>>
        get() = mutableHistoryList
    override val user: LiveData<User>
        get() = mutableUser
    override val match: LiveData<Match>
        get() = mutableMatch

    override suspend fun getAllQuestions() {
        val questionResponse = quizzoApiService.getAllQuestionAsync().await()
        mutableQuestionList.postValue(questionResponse.questions)
    }

    override suspend fun getQuestionByCategory(category: String) {
        val questionResponse = quizzoApiService.getQuestionByCategoryAsync(category).await()
        mutableQuestionList.postValue(questionResponse.questions)
    }

    override suspend fun createUser(id: String, username: String, email: String, imageURL: String) {
        var user = User(
            id,
            username,
            imageURL,
            email
        )
        val res = quizzoApiService.createUserAsync(user).await()
        user = User(
            res.userWithHistoryList.id,
            res.userWithHistoryList.username,
            res.userWithHistoryList.imageURL,
            res.userWithHistoryList.email
        )
        mutableHistoryList.postValue(res.userWithHistoryList.history)
        mutableUser.postValue(user)
    }

    override suspend fun postHistory(history: History, email: String) {
        quizzoApiService.run { postHistoryAsync(history, email) }
    }


    override fun initMatchMakingSocket(uid: String) {
        matchMakingSocket.initMatchmakingSocket { match ->
            Log.d("TAG", "$match")
            if (match.player1.uid == uid || match.player2.uid == uid) {
                if (match.player1.uid == uid) {
                    match.player2.isOpponent = true
                    match.player1.isOpponent = false
                } else {
                    match.player1.isOpponent = true
                    match.player2.isOpponent = false
                }

                mutableMatch.postValue(match)
                mutableQuestionList.postValue(match.questions!!)
            }
        }
    }

    override fun resetMatchMaking() = mutableMatch.postValue(null)

    override fun findMatch(name: String, imageURL: String, uid: String) =
        matchMakingSocket.findMatch(name, imageURL, uid)

    override fun findMatchBot() = matchMakingSocket.findMatchBot()

    override fun destroyMatchMakingSocket() = matchMakingSocket.destroySocket()


    override fun initMatchSocket(uid: String) {
        matchSocket.initMatchSocket(mutableMatch.value!!.matchID, uid) { score ->
            val match = mutableMatch.value!!
            if (match.player1.isOpponent!!)
                match.player1.score = score
            else
                match.player2.score = score
            mutableMatch.postValue(match)
        }
    }

    override fun matchComplete(user: Player) {
        matchSocket.matchComplete(user, mutableMatch.value!!.matchID)
    }

    override fun destroyMatchSocket() = matchSocket.destroySocket()
}