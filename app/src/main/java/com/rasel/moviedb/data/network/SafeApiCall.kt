package com.rasel.moviedb.data.network

import com.orhanobut.logger.Logger
import com.paperflymerchantapp.data.network.Resource
import com.paperflymerchantapp.utils.NoInternetException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException

interface SafeApiCall {

    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                throwable.message?.let { Logger.e(throwable, it) }
                when (throwable) {
                    is NoInternetException -> {
                        Resource.Failure(true, null, "Please check internet connection")
                    }
                    is ConnectException -> {
                        Resource.Failure(false, null, "Connection Exception")
                    }
                    is SocketTimeoutException -> {
                        Resource.Failure(false, null, "Connection timeout")
                    }
                    is IOException -> {
                        Resource.Failure(false, null, "Something went wrong... Please try again")
                    }
                    is HttpException -> {

                        val error = throwable.response()?.errorBody()?.string()


                        val message = StringBuilder()
                        error?.let {
                            try {
                                message.append(JSONObject(it).getString("error"))
                            } catch (e: JSONException) {
//                                message.append("Empty error body")
                                try {
                                    message.append(JSONObject(it).get("non_field_errors"))
                                } catch (e: JSONException) {
                                    message.append("Sorry! Something went wrong")
                                }

                            }
                        } ?: kotlin.run {
                            message.append("Empty error body")
                        }

                        Resource.Failure(
                            false,
                            throwable.code(),
                            message.toString().replace("[\"", "").replace("\"]", "")
                        )
                    }
                    else -> {
                        Resource.Failure(false, null, "Something went wrong... Please try again")
                    }
                }
            }
        }
    }
}