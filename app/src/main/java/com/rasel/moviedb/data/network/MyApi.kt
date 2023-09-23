package com.rasel.moviedb.data.network


import com.google.gson.GsonBuilder
import com.rasel.moviedb.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


interface MyApi {

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

            val baseUrl = BuildConfig.BASE_URL // Live Server

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

