package com.paperflymerchantapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.paperflymerchantapp.data.db.dao.UserDataDao
import com.paperflymerchantapp.data.db.entities.UserData

@Database(
    entities = [UserData::class], version = 5,
)

//Converter class is used to store and retrieve data in the database when is not storable  in their original format
//@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDataDao

    companion object {

        // singleton prevents multiple instances of database opening at the same time
        @Volatile
        private var instance: AppDatabase? = null

        operator fun invoke(context: Context) = instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "paperfly_go_database.db"
            ).allowMainThreadQueries().build()
    }
}