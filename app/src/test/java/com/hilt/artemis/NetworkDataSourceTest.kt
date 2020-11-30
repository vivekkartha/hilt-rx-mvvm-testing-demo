package com.hilt.artemis

import android.os.Build
import com.nhaarman.mockitokotlin2.verify
import com.hilt.artemis.data.BlogApi
import com.hilt.artemis.data.source.NetworkDataSource
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class NetworkDataSourceTest {

    private val mockBlogApi = mock(BlogApi::class.java)
    private val networkDataSource = NetworkDataSource(mockBlogApi)

    @Test
    fun getBlog() {
        networkDataSource.getBlog()
        verify(mockBlogApi).getBlog()
    }
}