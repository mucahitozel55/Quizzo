package quizzo.app.data.network.response.multiplayer

data class MatchComplete(
    val player1: String,
    val player2: String,
    val score1: Int,
    val score2: Int
)