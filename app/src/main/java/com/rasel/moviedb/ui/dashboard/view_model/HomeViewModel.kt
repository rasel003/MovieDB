package com.rasel.moviedb.ui.dashboard.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.paperflymerchantapp.data.network.Resource
import com.rasel.moviedb.data.db.entities.MovieInfo
import com.rasel.moviedb.data.network.responses.MovieDetailsResponse
import com.rasel.moviedb.data.preference.AppPrefsManager
import com.rasel.moviedb.data.repository.MerchantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val preferences: AppPrefsManager,
    private val repository: MerchantRepository
) : ViewModel() {

    private val _movieDetailsResponse: MutableLiveData<Resource<MovieDetailsResponse>> =
        MutableLiveData()
    val movieDetailsResponse: LiveData<Resource<MovieDetailsResponse>> = _movieDetailsResponse
    private var currentQueryValue: String? = null

    private var currentSearchResult: Flow<PagingData<UiModel>>? = null

    fun searchRepo(queryString: String): Flow<PagingData<UiModel>> {
        val lastResult = currentSearchResult
        if (queryString == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = queryString
        val newResult: Flow<PagingData<UiModel>> = repository.getSearchResultStream(queryString)
            .map { pagingData -> pagingData.map { UiModel.RepoItem(it) } }
            .map {
                it.insertSeparators<UiModel.RepoItem, UiModel> { before, after ->
                    if (after == null) {
                        // we're at the end of the list
                        return@insertSeparators null
                    }

                    if (before == null) {
                        // we're at the beginning of the list
                        return@insertSeparators UiModel.SeparatorItem("${roundedStarCount}0.000+ stars")
                    }
                    // check between 2 items
                    if (roundedStarCount > roundedStarCount) {
                        if (roundedStarCount >= 1) {
                            UiModel.SeparatorItem("${roundedStarCount}0.000+ stars")
                        } else {
                            UiModel.SeparatorItem("< 10.000+ stars")
                        }
                    } else {
                        // no separator
                        null
                    }
                }
            }
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }

    fun getMovieInfo(movieId: String) = viewModelScope.launch {
        _movieDetailsResponse.value = Resource.Loading
        _movieDetailsResponse.value = repository.getMovieInfo(movieId)
    }

    fun addAsFavorite(id: Int, checked: Boolean) {
        repository.addAsFavorite(id, checked)
    }
}

sealed class UiModel {
    data class RepoItem(val movieInfo: MovieInfo) : UiModel()
    data class SeparatorItem(val description: String) : UiModel()
}

private val roundedStarCount: Int =4