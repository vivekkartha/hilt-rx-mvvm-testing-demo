package com.hilt.artemis.data.source

import com.hilt.artemis.data.BlogApi
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject


open class NetworkDataSource @Inject constructor(private val api: BlogApi) :
    DataSource {

    override fun getBlog(): Single<String> = api.getBlog()
}