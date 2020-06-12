package quizzo.app.ui.multiplayer.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import quizzo.app.data.database.entity.History
import quizzo.app.data.database.entity.User
import quizzo.app.data.network.response.multiplayer.Match
import quizzo.app.data.repository.QuizzoRepository

class MultiplayerResultViewModel(private val repo: QuizzoRepository) : ViewModel() {
    val match: LiveData<Match> by lazy {
        repo.match
    }

    val user: LiveData<User> by lazy {
        repo.user
    }

    fun addMatchToHistory(history: History) = repo.addMatchToHistory(history)

    fun destroyMatchSocket() = repo.destroyMatchSocket()
}
