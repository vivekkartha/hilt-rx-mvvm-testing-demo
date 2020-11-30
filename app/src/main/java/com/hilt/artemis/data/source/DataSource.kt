package com.hilt.artemis.data.source

import io.reactivex.rxjava3.core.Single

interface DataSource {

    fun getBlog(): Single<String>
}