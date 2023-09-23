package com.rasel.moviedb.data.repository

import com.rasel.moviedb.data.network.MyApi
import com.rasel.moviedb.data.network.SafeApiCall
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: MyApi,
) : SafeApiCall {



}