package quizzo.app.data.network.response.multiplayer

import quizzo.app.data.network.response.question.Question

class Match(
    val matchID: String,
    val questions: List<Question>? = null,
    var player1: Player,
    var player2: Player
)