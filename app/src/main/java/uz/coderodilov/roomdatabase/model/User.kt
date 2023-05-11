package uz.coderodilov.roomdatabase.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    val name: String,
    val lastName: String,
    val time: String,
    val image: Bitmap
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
