/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rasel.moviedb.ui.dashboard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.orhanobut.logger.Logger
import com.rasel.moviedb.R
import com.rasel.moviedb.data.db.entities.MovieInfo
import com.rasel.moviedb.databinding.RepoViewItemBinding
import com.rasel.moviedb.ui.dashboard.view_model.UiModel
import com.rasel.moviedb.ui.movie_details.view.MovieDetailsActivity

/**
 * Adapter for the list of repositories.
 */
class MovieListAdapter(
    private var onItemClicked: ((movieId: Int, checked: Boolean, position : Int) -> Unit)
) : PagingDataAdapter<UiModel, ViewHolder>(UIMODEL_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RepoViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is UiModel.RepoItem -> R.layout.repo_view_item
            is UiModel.SeparatorItem -> R.layout.separator_view_item
            else -> throw UnsupportedOperationException("Unknown view")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val uiModel = getItem(position)
        uiModel.let {
            when (uiModel) {
                is UiModel.RepoItem -> (holder as MovieViewHolder).bind(uiModel.movieInfo)
                is UiModel.SeparatorItem -> (holder as SeparatorViewHolder).bind(uiModel.description)
                else -> {}
            }
        }
    }


    /**
     * View Holder for a [MovieInfo] RecyclerView list item.
     */
    inner class MovieViewHolder(private val binding: RepoViewItemBinding) :
        ViewHolder(binding.root) {

        private var movieInfo: MovieInfo? = null

        init {
            binding.rootLayout.setOnClickListener {
                movieInfo?.id?.let { movieId ->
                    MovieDetailsActivity.start(it.context, movieId.toString())
                }
            }
            binding.cbFavorite.setOnCheckedChangeListener { checkBox, checked ->
                if(checkBox.isPressed) {
                    movieInfo?.let { it1 -> onItemClicked(it1.id, checked, bindingAdapterPosition) } ?: kotlin.run { }
                    Logger.t("rsl").d("clicked checkbox ${movieInfo?.id}")
                }
            }
        }

        fun bind(movieInfo: MovieInfo?) {
            if (movieInfo == null) {
                val resources = itemView.resources
                /*name.text = resources.getString(R.string.loading)
                description.visibility = View.GONE
                language.visibility = View.GONE
                stars.text = resources.getString(R.string.unknown)
                forks.text = resources.getString(R.string.unknown)*/
            } else {
                showRepoData(movieInfo)
            }
        }

        private fun showRepoData(movieInfo: MovieInfo) {
            this.movieInfo = movieInfo
            binding.tvMovieTitle.text = movieInfo.title
            binding.repoStars.text = "%.2f".format(movieInfo.voteAverage)

            binding.cbFavorite.isChecked = !movieInfo.isFavorite


             Glide
                 .with(binding.rootLayout.context)
                 .load("https://image.tmdb.org/t/p/w500/${movieInfo.posterPath}")
                 .into(binding.imgMovieCover)

            // if the description is missing, hide the TextView
            var descriptionVisibility = View.GONE
            /* if (movieInfo.description != null) {
                 description.text = movieInfo.description
                 descriptionVisibility = View.VISIBLE
             }*/
            // description.visibility = descriptionVisibility

            // stars.text = movieInfo.voteAverage.toString()
//        forks.text = movieInfo.forks.toString()

            /* // if the language is missing, hide the label and the value
             var languageVisibility = View.GONE
             if (!movieInfo.language.isNullOrEmpty()) {
                 val resources = this.itemView.context.resources
                 language.text = resources.getString(R.string.language, movieInfo.language)
                 languageVisibility = View.VISIBLE
             }*/
//        language.visibility = languageVisibility
        }

    }


    companion object {
        private val UIMODEL_COMPARATOR = object : DiffUtil.ItemCallback<UiModel>() {
            override fun areItemsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
                return (oldItem is UiModel.RepoItem && newItem is UiModel.RepoItem &&
                        oldItem.movieInfo.id == newItem.movieInfo.id) ||
                        (oldItem is UiModel.SeparatorItem && newItem is UiModel.SeparatorItem &&
                                oldItem.description == newItem.description)
            }

            override fun areContentsTheSame(oldItem: UiModel, newItem: UiModel): Boolean =
                oldItem == newItem
        }
    }
}