package quizzo.app.data.network.response.question

data class QuestionResponse(
    val count: Int,
    val questions: List<Question>
)