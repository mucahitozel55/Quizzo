package quizzo.app.ui.multiplayer.matchmaking

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import quizzo.app.data.database.entity.User
import quizzo.app.data.network.response.multiplayer.Match
import quizzo.app.data.repository.QuizzoRepository

class MatchMakingViewModel(private val repo: QuizzoRepository) : ViewModel() {
    val match: LiveData<Match> by lazy {
        repo.match
    }

    val user:LiveData<User> by lazy{
        repo.user
    }

    fun findMatch() = repo.findMatch()
    fun findMatchBot() = repo.findMatchBot()
    fun destroyMatchMakingSocket() = repo.destroyMatchMakingSocket()


}
