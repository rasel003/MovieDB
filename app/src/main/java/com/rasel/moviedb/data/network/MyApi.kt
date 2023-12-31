package com.rasel.moviedb.data.network


import com.google.gson.GsonBuilder
import com.rasel.moviedb.BuildConfig
import com.rasel.moviedb.data.network.responses.MovieDetailsResponse
import com.rasel.moviedb.data.network.responses.MovieListResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


interface MyApi {

    @GET("https://api.themoviedb.org/3/trending/movie/day?language=en-US&api_key=c33832f707ec95387239c7014b8fb76b")
    suspend fun getMovieList(
        @Query("page") page: Int
    ): MovieListResponse

    @GET("https://api.themoviedb.org/3/movie/{movie_id}?language=en-US&api_key=c33832f707ec95387239c7014b8fb76b")
    suspend fun getMovieInfo(
        @Path("movie_id") movieId: String
    ): MovieDetailsResponse

    companion object {

        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ): MyApi {

            val okkHttpclient = OkHttpClient.Builder()
                .connectTimeout(20L, TimeUnit.SECONDS)
                .readTimeout(30L, TimeUnit.SECONDS)
                .writeTimeout(30L, TimeUnit.SECONDS)
                .addInterceptor(networkConnectionInterceptor)
                .also { client ->
                    if (BuildConfig.DEBUG) {
                        val logging = HttpLoggingInterceptor()
                        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                        client.addInterceptor(logging)
                    }
                }
                .build()

//            val baseUrl = BuildConfig.BASE_URL // Live Server
            val baseUrl = "https://api.github.com/" // Live Server

            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl(baseUrl)
                .also { retrofit ->
                    if (BuildConfig.DEBUG) {
                        val gson = GsonBuilder()
                            .setLenient()
                            .create()
                        retrofit.addConverterFactory(GsonConverterFactory.create(gson))
                    } else retrofit.addConverterFactory(GsonConverterFactory.create())
                }
                .build()
                .create(MyApi::class.java)
        }
    }

}

