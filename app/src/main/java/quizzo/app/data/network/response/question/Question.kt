package quizzo.app.data.network.response.question

data class Question(
    val answers: List<String>,
    val category: String,
    val correct: Int,
    val id: Int,
    val image: String? = null,
    val question: String
)