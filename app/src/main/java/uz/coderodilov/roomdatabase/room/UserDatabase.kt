package uz.coderodilov.roomdatabase.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import uz.coderodilov.roomdatabase.model.User

@Database(entities = [User::class], version = 1, exportSchema = false)
@TypeConverters(UserTypeConverter::class)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object{
        @Volatile
        private var INSTANCE : UserDatabase? = null

        fun getDatabaseInstance(context: Context): UserDatabase {
            if (INSTANCE == null){
                INSTANCE = createDatabaseInstance(context)
            }
            return INSTANCE as UserDatabase
        }

        private fun createDatabaseInstance(context: Context) : UserDatabase {
            return Room.databaseBuilder(
                context,
                UserDatabase::class.java,
                "user_database"
            ).allowMainThreadQueries().build()
        }
    }

}