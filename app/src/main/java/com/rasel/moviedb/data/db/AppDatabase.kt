package com.rasel.moviedb.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rasel.moviedb.data.db.dao.MovieDao
import com.rasel.moviedb.data.db.dao.RemoteKeysDao
import com.rasel.moviedb.data.db.entities.MovieInfo

@Database(
    entities = [ MovieInfo::class, RemoteKeys::class], version = 5, exportSchema = false
)

//Converter class is used to store and retrieve data in the database when is not storable  in their original format
//@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun reposDao(): MovieDao
    abstract fun remoteKeysDao(): RemoteKeysDao

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
                "movie_database.db"
            ).allowMainThreadQueries().build()
    }
}