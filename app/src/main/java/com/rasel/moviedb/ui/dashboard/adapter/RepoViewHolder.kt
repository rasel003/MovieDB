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

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rasel.moviedb.data.db.entities.MovieInfo
import com.rasel.moviedb.databinding.RepoViewItemBinding
import com.rasel.moviedb.ui.movie_details.view.MovieDetailsActivity

/**
 * View Holder for a [MovieInfo] RecyclerView list item.
 */
class RepoViewHolder(private val binding: RepoViewItemBinding) : RecyclerView.ViewHolder(binding.root) {

    private var movieInfo: MovieInfo? = null

    init {
        binding.rootLayout.setOnClickListener {
            movieInfo?.id?.let { movieId ->
               MovieDetailsActivity.start(it.context, movieId.toString())
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

        Glide
            .with(binding.rootLayout.context)
            .load("https://image.tmdb.org/t/p/w1000/${movieInfo.posterPath}")
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

    companion object {
        fun create(parent: ViewGroup): RepoViewHolder {
//            val view = LayoutInflater.from(parent.context).inflate(R.layout.repo_view_item, parent, false)
            val binding = RepoViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return RepoViewHolder(binding)
        }
    }
}