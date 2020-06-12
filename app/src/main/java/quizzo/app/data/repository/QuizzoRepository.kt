package quizzo.app.data.repository

import androidx.lifecycle.LiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import quizzo.app.data.database.entity.History
import quizzo.app.data.database.entity.User
import quizzo.app.data.network.response.multiplayer.Match
import quizzo.app.data.network.response.question.Question
import quizzo.app.util.category.Category
import quizzo.app.util.loading.Loading

interface QuizzoRepository {
    val questionList: LiveData<List<Question>>
    val loading: LiveData<Loading>
    val historyList: LiveData<List<History>>
    val categoryList: List<Category>
    val user: LiveData<User>
    val isAnonymousSignIn: Boolean
    val match: LiveData<Match>

    fun resetLoading()

    fun getAllQuestions()
    fun getQuestionByCategory(category: String)

    fun addMatchToHistory(history: History)
    fun getAllHistory()

    fun signInUser(googleSignInAccount: GoogleSignInAccount)
    fun signInAnonymously()
    fun getUser()
    fun logout()

    fun findMatch()
    fun findMatchBot()
    fun destroyMatchMakingSocket()

    fun initMatch()
    fun matchComplete(score: Int)
    fun destroyMatchSocket()
}