package quizzo.app.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import quizzo.app.data.database.entity.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertUser(user: User)

    @Query("SELECT * FROM user WHERE id= :uid")
    fun getUserByID(uid: String): User

    @Query("DELETE FROM user")
    fun deleteUser()
}