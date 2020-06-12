package quizzo.app.data.network.response.user

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("user")
    val userWithHistoryList: UserWithHistoryList
)