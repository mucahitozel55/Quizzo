package quizzo.app.data.network.service

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import quizzo.app.Contract
import quizzo.app.data.database.entity.History
import quizzo.app.data.database.entity.User
import quizzo.app.data.network.converters.HistoryConverter
import quizzo.app.data.network.converters.UserConverter
import quizzo.app.data.network.response.question.QuestionResponse
import quizzo.app.data.network.response.user.UserResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface QuizzoApiService {

    @GET("question")
    fun getAllQuestionAsync(): Deferred<QuestionResponse>

    @GET("question")
    fun getQuestionByCategoryAsync(@Query("category") category: String): Deferred<QuestionResponse>

    @POST("user")
    fun createUserAsync(@Body user: User): Deferred<UserResponse>

    @POST("user/history")
    fun postHistoryAsync(@Body history: History, @Query("email") email: String): Deferred<Unit>

    companion object {
        operator fun invoke(context: Context): QuizzoApiService {
            val gb = GsonBuilder()
            gb.registerTypeAdapter(UserResponse::class.java, UserConverter())
            gb.registerTypeAdapter(History::class.java, HistoryConverter())
            val customGson = gb.create()

            val writeKeyInterceptor = Interceptor { chain ->

                val url = chain.request()
                    .url
                    .newBuilder()
                    .addQueryParameter("key", Contract.WRITE_KEY)
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(writeKeyInterceptor)
                .build() //TODO: Add network interceptor

            return Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create(customGson))
                .baseUrl(Contract.QUIZZO_SERVER_URL)
                .build()
                .create(QuizzoApiService::class.java)
        }
    }
}