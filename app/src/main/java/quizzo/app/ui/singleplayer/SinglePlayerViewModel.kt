package quizzo.app.ui.singleplayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import quizzo.app.data.database.entity.History
import quizzo.app.data.network.response.question.Question
import quizzo.app.data.repository.QuizzoRepository
import quizzo.app.util.loading.Loading

class SinglePlayerViewModel(private val repo: QuizzoRepository) : ViewModel() {
    val questionList: LiveData<List<Question>> by lazy {
        repo.questionList
    }

    val loading: LiveData<Loading> by lazy {
        repo.loading
    }

    fun resetLoading() = repo.resetLoading()

    fun getAllQuestions() {
        repo.getAllQuestions()
    }

    fun getQuestionsByCategory(category: String) {
        repo.getQuestionByCategory(category)
    }

    fun addMatchToHistory(history: History) {
        repo.addMatchToHistory(history)
    }

}
