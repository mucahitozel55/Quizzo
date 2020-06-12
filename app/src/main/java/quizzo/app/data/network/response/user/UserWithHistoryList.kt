package quizzo.app.data.network.response.user

import quizzo.app.data.database.entity.History

data class UserWithHistoryList(
    val email: String,
    val history: List<History> = ArrayList<History>(),
    val id: String,
    val imageURL: String,
    val username: String
)