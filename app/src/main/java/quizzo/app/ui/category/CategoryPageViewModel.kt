package quizzo.app.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import quizzo.app.data.repository.QuizzoRepository
import quizzo.app.util.category.Category

class CategoryPageViewModel(private val repo: QuizzoRepository) : ViewModel() {
    val categoryList: List<Category> by lazy {
        repo.categoryList
    }
}
