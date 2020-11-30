package com.hilt.artemis

import android.os.Build
import com.hilt.artemis.data.BlogRepository
import com.hilt.artemis.data.source.NetworkDataSource
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class BlogRepositoryTest {

    private var mockDataSource = mock(NetworkDataSource::class.java)
    private val scheduler = TestScheduler()
    private val blogRepository = BlogRepository(mockDataSource, scheduler, scheduler)

    @Test
    fun getBlog() {
        whenever(mockDataSource.getBlog()).thenReturn(Single.just(""))
        blogRepository.getBlog({}, {})
        verify(mockDataSource).getBlog()
    }
}