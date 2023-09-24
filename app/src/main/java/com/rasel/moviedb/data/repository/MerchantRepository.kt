package com.rasel.moviedb.data.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rasel.moviedb.data.db.AppDatabase
import com.rasel.moviedb.data.db.entities.MovieInfo
import com.rasel.moviedb.data.network.MyApi
import com.rasel.moviedb.data.network.SafeApiCall
import com.rasel.moviedb.data.preference.AppPrefsManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MerchantRepository @Inject constructor(
    private val api: MyApi,
    private val preferences: AppPrefsManager,
    private val appDatabase: AppDatabase
) : SafeApiCall {

    /**
     * Search repositories whose names match the query, exposed as a stream of data that will emit
     * every time we get more data from the network.
     */
    fun getSearchResultStream(query: String): Flow<PagingData<MovieInfo>> {
        Log.d("GithubRepository", "New query: $query")

        // appending '%' so we can allow other characters to be before and after the query string
        val dbQuery = "%${query.replace(' ', '%')}%"
        val pagingSourceFactory = { appDatabase.reposDao().reposByName(dbQuery) }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = GithubRemoteMediator(
                query,
                api,
                appDatabase
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    suspend fun getMovieInfo(movieId: String) = safeApiCall {
        api.getMovieInfo(movieId)
    }

    fun addAsFavorite(id: Int, checked: Boolean) {
        appDatabase.reposDao().addAsFavorite(id, checked)
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }

}