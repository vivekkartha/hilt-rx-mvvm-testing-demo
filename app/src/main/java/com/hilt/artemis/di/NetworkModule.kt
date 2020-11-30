package com.hilt.artemis.di

import com.hilt.artemis.data.BlogApi
import com.hilt.artemis.data.source.DataSource
import com.hilt.artemis.data.source.NetworkDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class NetworkModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class IOScheduler

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class UIScheduler

    @Provides
    @UIScheduler
    fun provideAndroidScheduler(): Scheduler = AndroidSchedulers.mainThread()

    @Provides
    @IOScheduler
    fun provideIOScheduler(): Scheduler = Schedulers.io()

    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesBlogApi(retrofit: Retrofit): BlogApi = retrofit.create(BlogApi::class.java)

    @Provides
    @Singleton
    fun provideNetworkDataSource(api: BlogApi): NetworkDataSource {
        return NetworkDataSource(api)
    }

    companion object {
        private const val BASE = "http://vivekkartha.com"
    }
}