package quizzo.app.ui.multiplayer.match

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import quizzo.app.data.database.entity.User
import quizzo.app.data.network.response.multiplayer.Player
import quizzo.app.data.network.response.question.Question
import quizzo.app.data.repository.QuizzoRepository

class MultiplayerViewModel(private val repo: QuizzoRepository) : ViewModel() {
    val opponentPlayer: Player by lazy {
        if (repo.match.value!!.player1.isOpponent!!)
            repo.match.value!!.player1
        else
            repo.match.value!!.player2
    }

    val questionList: LiveData<List<Question>> by lazy {
        repo.questionList
    }

    val user: LiveData<User> by lazy {
        repo.user
    }

    fun initMatch() = repo.initMatch()
    fun matchComplete(score: Int) = repo.matchComplete(score)

}
