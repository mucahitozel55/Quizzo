package quizzo.app.data.network.response.multiplayer

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Player(
    val uid: String,
    val name: String,
    val imageURL: String,
    var score: Int,
    var isOpponent: Boolean? = false
) : Parcelable
