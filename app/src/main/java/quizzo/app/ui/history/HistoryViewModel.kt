package quizzo.app.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import quizzo.app.data.database.entity.History
import quizzo.app.data.database.entity.User
import quizzo.app.data.repository.QuizzoRepository
import quizzo.app.util.category.Category
import quizzo.app.util.loading.Loading

class HistoryViewModel(private val repo: QuizzoRepository) : ViewModel() {
    val historyList: LiveData<List<History>> by lazy {
        repo.historyList
    }

    val catList: List<Category> by lazy {
        repo.categoryList
    }

    val user: LiveData<User> by lazy {
        repo.user
    }

    val loading: LiveData<Loading> by lazy {
        repo.loading
    }

    fun getAllHistory() {
        repo.getAllHistory()
    }
}
