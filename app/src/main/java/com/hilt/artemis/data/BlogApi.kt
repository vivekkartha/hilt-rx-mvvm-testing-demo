package com.hilt.artemis.data

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface BlogApi {

    @GET("/")
    fun getBlog(): Single<String>
}