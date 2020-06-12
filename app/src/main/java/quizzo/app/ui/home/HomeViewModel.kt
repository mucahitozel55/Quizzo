package quizzo.app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import quizzo.app.data.database.entity.User
import quizzo.app.data.repository.QuizzoRepository

class HomeViewModel(private val repo: QuizzoRepository) : ViewModel() {
    val user: LiveData<User> by lazy {
        repo.user
    }

    val isAnonymousSignIn: Boolean by lazy {
        repo.isAnonymousSignIn
    }

    fun logout() = repo.logout()
}
