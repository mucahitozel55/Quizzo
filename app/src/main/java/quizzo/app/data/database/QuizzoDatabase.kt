package quizzo.app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import quizzo.app.data.database.dao.HistoryDao
import quizzo.app.data.database.dao.UserDao
import quizzo.app.data.database.entity.History
import quizzo.app.data.database.entity.User
import quizzo.app.data.database.typeconvertors.DateConverter
import quizzo.app.data.database.typeconvertors.MatchStatusConverter
import quizzo.app.data.database.typeconvertors.MatchTypeConverter

@Database(
    entities = [History::class, User::class],
    version = 1
)
@TypeConverters(*[DateConverter::class, MatchTypeConverter::class, MatchStatusConverter::class])
abstract class QuizzoDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var instance: QuizzoDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context): QuizzoDatabase {
            return instance ?: synchronized(LOCK) {
                instance ?: Room.databaseBuilder(
                    context,
                    QuizzoDatabase::class.java,
                    "quizzo.db"
                ).build()
            }
        }
    }
}
