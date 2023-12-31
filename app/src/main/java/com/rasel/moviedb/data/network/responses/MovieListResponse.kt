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

package com.rasel.moviedb.data.network.responses

import com.google.gson.annotations.SerializedName
import com.rasel.moviedb.data.db.entities.MovieInfo

/**
 * Data class to hold repo responses from searchRepo API calls.
 */
data class MovieListResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val items: List<MovieInfo> = emptyList(),
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int,

    val nextPage: Int? = null
)
