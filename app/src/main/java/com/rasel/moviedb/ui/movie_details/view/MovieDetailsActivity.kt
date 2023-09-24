package com.rasel.moviedb.ui.movie_details.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.paperflymerchantapp.data.network.Resource
import com.rasel.moviedb.R
import com.rasel.moviedb.data.preference.AppPrefsManager
import com.rasel.moviedb.databinding.ActivityMovieDetailsBinding
import com.rasel.moviedb.ui.dashboard.view_model.HomeViewModel
import com.rasel.moviedb.utils.KEY_MOVIE_ID
import com.rasel.moviedb.utils.toastError
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailsActivity : AppCompatActivity() {
    private var movieId: String? = null
    private lateinit var binding: ActivityMovieDetailsBinding

    @Inject
    lateinit var appPrefsManager: AppPrefsManager

    private val viewModel: HomeViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details)
        movieId = intent.getStringExtra(KEY_MOVIE_ID)

        movieId?.let { viewModel.getMovieInfo(it) }

        viewModel.movieDetailsResponse.observe(this) { resource ->

            // hiding progress bar for other responses
            binding.progress = false

            when (resource) {
                is Resource.Loading -> {
                    binding.progress = true
                }

                is Resource.Success -> {
                    val response = resource.value
                    binding.tvMovieTitle.text = response.title.ifBlank { response.originalTitle }
                    binding.tvMovieDetails.text = response.overview

                    binding.tvMovieType.text =
                        response.genres.joinToString(separator = " / ") { it.name }

                    /*Glide
                        .with(this)
                        .load("https://image.tmdb.org/t/p/w500/${response.posterPath}")
                        .into(binding.imgMovieCover)*/
                }

                is Resource.Failure -> {
                    resource.errorBody?.let { it1 -> toastError(it1) }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun start(context: Context, movieId: String) {
            val intent = Intent(context, MovieDetailsActivity::class.java)
            intent.putExtra(KEY_MOVIE_ID, movieId)
            context.startActivity(intent)
        }
    }

}