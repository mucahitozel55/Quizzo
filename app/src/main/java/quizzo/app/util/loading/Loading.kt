package quizzo.app.util.loading

data class Loading(
    val loadingID: Int = 0,
    val loadingState: LoadingState = LoadingState.IDLE
)

/*
    Loading ID
    1: Get all questions / Get Questions by category
    2: Login with google
 */