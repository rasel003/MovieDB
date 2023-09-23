package com.rasel.moviedb.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rasel.moviedb.data.db.entities.MovieInfo

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movieInfos: List<MovieInfo>)

    @Query(
        "SELECT * FROM repos " /*+
            "title LIKE :queryString OR originalTitle LIKE :queryString " +
            "ORDER BY voteAverage DESC, title ASC"*/
    )
    fun reposByName(): PagingSource<Int, MovieInfo>

    @Query("DELETE FROM repos")
    suspend fun clearRepos()
}
