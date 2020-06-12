package quizzo.app.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.asDeferred
import quizzo.app.data.database.dao.HistoryDao
import quizzo.app.data.database.dao.UserDao
import quizzo.app.data.database.entity.History
import quizzo.app.data.database.entity.User
import quizzo.app.data.database.typeconvertors.DateConverter
import quizzo.app.data.network.NetworkDataSource
import quizzo.app.data.network.response.multiplayer.Match
import quizzo.app.data.network.response.multiplayer.Player
import quizzo.app.data.network.response.question.Question
import quizzo.app.util.category.Category
import quizzo.app.util.category.getAllCategories
import quizzo.app.util.loading.Loading
import quizzo.app.util.loading.LoadingState

class QuizzoRepositoryImpl(
    private val networkSource: NetworkDataSource,
    private val historyDao: HistoryDao,
    private val userDao: UserDao
) : QuizzoRepository {
    private val mutableQuestionList = MutableLiveData<List<Question>>()
    private val mutableLoading = MutableLiveData<Loading>()
    private val mutableHistoryList = MutableLiveData<List<History>>()
    private val mutableUser = MutableLiveData<User>()
    private var catList: List<Category>
    private var anonymousSignIn: Boolean = false
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    init {
        networkSource.questionList.observeForever { questionList ->
            mutableQuestionList.postValue(questionList)  //TODO: improve
        }

        networkSource.historyList.observeForever { historyList ->
            GlobalScope.launch(Dispatchers.IO) {
                val newHistoryList = historyList.distinctBy { it.date.toString() }
                historyDao.addAllHistory(newHistoryList)
                mutableHistoryList.postValue(newHistoryList)
            }
        }

        networkSource.user.observeForever { user ->
            GlobalScope.launch(Dispatchers.IO) {
                userDao.upsertUser(user)
                mutableUser.postValue(user)
            }

        }

        catList = getAllCategories()
    }

    override val loading: LiveData<Loading>
        get() = mutableLoading

    override val questionList: LiveData<List<Question>>
        get() = mutableQuestionList

    override val historyList: LiveData<List<History>>
        get() = mutableHistoryList

    override val categoryList: List<Category>
        get() = catList

    override val user: LiveData<User>
        get() = mutableUser

    override val isAnonymousSignIn: Boolean
        get() = anonymousSignIn

    override val match: LiveData<Match>
        get() = networkSource.match

    override fun resetLoading() {
        mutableLoading.postValue(Loading(-1, LoadingState.IDLE))
    }

    override fun getAllQuestions() {
        GlobalScope.launch(Dispatchers.IO) {
            mutableLoading.postValue(Loading(1, LoadingState.LOADING))
            networkSource.getAllQuestions()
            mutableLoading.postValue(Loading(1, LoadingState.COMPLETED))
        }
    }

    override fun getQuestionByCategory(category: String) {
        GlobalScope.launch(Dispatchers.IO) {
            mutableLoading.postValue(Loading(1, LoadingState.LOADING))
            networkSource.getQuestionByCategory(category)
            mutableLoading.postValue(Loading(1, LoadingState.COMPLETED))
        }
    }

    override fun getAllHistory() {
        GlobalScope.launch(Dispatchers.IO) {
            val historyList = historyDao.getAllHistory()
            mutableHistoryList.postValue(historyList)
        }
    }

    override fun addMatchToHistory(history: History) {
        GlobalScope.launch(Dispatchers.IO) {

            val his = historyDao.getSingleHistory(DateConverter().fromDate(history.date))
            mutableQuestionList.postValue(null)
            networkSource.resetMatchMaking()
            if (his == null) {
                historyDao.addHistory(history)
                if (user.value != null)
                    networkSource.postHistory(history, user.value!!.email)
            }
        }
    }


    override fun signInUser(googleSignInAccount: GoogleSignInAccount) {
        GlobalScope.launch(Dispatchers.IO) {
            mutableLoading.postValue(Loading(2, LoadingState.LOADING))
            auth.signInWithCredential(
                GoogleAuthProvider.getCredential(
                    googleSignInAccount.idToken!!,
                    null
                )
            ).asDeferred().await()
            networkSource.createUser(
                auth.uid!!,
                googleSignInAccount.displayName!!,
                googleSignInAccount.email!!,
                googleSignInAccount.photoUrl!!.toString()
            )
            anonymousSignIn = false
            mutableLoading.postValue(Loading(2, LoadingState.COMPLETED))
        }
    }

    override fun signInAnonymously() {
        anonymousSignIn = true
    }

    override fun getUser() {
        GlobalScope.launch(Dispatchers.IO) {
            if (auth.currentUser == null) {
                mutableUser.postValue(null)
            } else {
                val userInfo = userDao.getUserByID(auth.currentUser!!.uid)
                mutableUser.postValue(userInfo)
            }
        }
    }

    override fun logout() {
        GlobalScope.launch(Dispatchers.IO) {
            historyDao.deleteAllHistory()
            userDao.deleteUser()
            auth.signOut()
            mutableHistoryList.postValue(null)
            mutableUser.postValue(null)
        }
    }

    override fun findMatch() {
        networkSource.initMatchMakingSocket(user.value!!.id)
        networkSource.findMatch(user.value!!.username, user.value!!.imageURL, user.value!!.id)
    }

    override fun findMatchBot() = networkSource.findMatchBot()

    override fun destroyMatchMakingSocket() = networkSource.destroyMatchMakingSocket()


    override fun initMatch() = networkSource.initMatchSocket(user.value!!.id)

    override fun matchComplete(score: Int) {
        networkSource.matchComplete(
            Player(
                user.value!!.id,
                user.value!!.username,
                user.value!!.imageURL,
                score
            )
        )
    }

    override fun destroyMatchSocket() = networkSource.destroyMatchSocket()
}