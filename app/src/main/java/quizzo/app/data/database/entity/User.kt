package quizzo.app.data.database.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val username: String,
    var imageURL: String = "null",
    val email: String
)