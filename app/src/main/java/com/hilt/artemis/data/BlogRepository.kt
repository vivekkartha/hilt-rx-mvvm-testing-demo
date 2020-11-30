package com.hilt.artemis.data

import com.hilt.artemis.data.source.NetworkDataSource
import com.hilt.artemis.di.NetworkModule
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

open class BlogRepository @Inject constructor(
    private val dataSource: NetworkDataSource,
    @NetworkModule.IOScheduler private val ioScheduler: Scheduler,
    @NetworkModule.UIScheduler private val androidScheduler: Scheduler
) {

    private val compositeDisposable = CompositeDisposable()
    fun getBlog(onSuccess: (String) -> Unit, onFailed: (Throwable) -> Unit) {
        compositeDisposable.add(
            dataSource.getBlog()
                .observeOn(androidScheduler)
                .subscribeOn(ioScheduler)
                .subscribe({
                    onSuccess(it)
                }, {
                    onFailed(it)
                })
        )
    }

    fun cancelAll() {
        compositeDisposable.clear()
    }
}