package uz.coderodilov.roomdatabase.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import uz.coderodilov.roomdatabase.model.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user_table ORDER BY id DESC")
    fun getAllUsers():List<User>

    @Insert
    fun insertUser(user: User)

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user: User)
}