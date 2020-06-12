package quizzo.app.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import quizzo.app.data.database.entity.User
import quizzo.app.data.repository.QuizzoRepository
import quizzo.app.util.loading.Loading

class AuthViewModel(private val repo: QuizzoRepository) : ViewModel() {
    val loading: LiveData<Loading> by lazy {
        repo.loading
    }

    val user: LiveData<User> by lazy {
        repo.user
    }

    fun resetLoading() = repo.resetLoading()

    fun getUser() = repo.getUser()

    fun signInUser(account: GoogleSignInAccount) {
        repo.signInUser(account)
    }

    fun signInAnonymously() = repo.signInAnonymously()
}
