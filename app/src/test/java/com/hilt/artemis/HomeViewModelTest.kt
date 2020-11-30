package com.hilt.artemis

import android.os.Build
import com.hilt.artemis.data.BlogRepository
import com.hilt.artemis.data.Resource
import com.hilt.artemis.ui.HomeViewModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Suppress("UNCHECKED_CAST")
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class HomeViewModelTest {

    private lateinit var mockRepository: BlogRepository
    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setup() {
        mockRepository = mock()
        homeViewModel = HomeViewModel(mockRepository)
    }

    @Test
    fun request10thCharacterSuccess() {
        whenever(mockRepository.getBlog(any(), any())).thenAnswer {
            (it.arguments.first() as ((String) -> Unit)).invoke("1234567899")
        }

        homeViewModel.request10thCharacter()

        homeViewModel.tenthCharacterLiveData.observeForever {
            assertEquals('9', (it as Resource.Loaded).data)
        }
    }

    @Test
    fun request10thCharacterFailed() {

        whenever(mockRepository.getBlog(any(), any())).thenAnswer {
            (it.arguments[1] as ((Throwable) -> Unit)).invoke(Exception("Failed"))
        }

        homeViewModel.request10thCharacter()

        homeViewModel.tenthCharacterLiveData.observeForever {
            assertEquals("Failed", (it as Resource.Failed<*>).error)
        }
    }

    @Test
    fun requestEvery10thCharacterSuccess() {
        whenever(mockRepository.getBlog(any(), any())).thenAnswer {
            (it.arguments.first() as ((String) -> Unit)).invoke("The quickest brown fox jumped over the lazy dog")
        }

        homeViewModel.requestEvery10thCharacter()

        homeViewModel.every10thLiveData.observeForever { result ->
            val actual = (result as Resource.Loaded<List<Char>>).data
            assertEquals('e', actual.first())
            assertEquals('o', actual.last())
        }
    }

    @Test
    fun requestEvery10thCharacterFailed() {
        whenever(mockRepository.getBlog(any(), any())).thenAnswer {
            (it.arguments[1] as ((Throwable) -> Unit)).invoke(Exception("Failed"))
        }
        homeViewModel.requestEvery10thCharacter()

        homeViewModel.every10thLiveData.observeForever {
            assertEquals("Failed", (it as Resource.Failed<*>).error)
        }
    }

    @Test
    fun wordCounterSuccess() {
        whenever(mockRepository.getBlog(any(), any())).thenAnswer {
            (it.arguments.first() as ((String) -> Unit)).invoke("The quickest brown fox jumped over the lazy lazy dog")
        }

        homeViewModel.requestWordCounter()

        homeViewModel.wordCounterLiveData.observeForever {
            assertEquals(2, (it as Resource.Loaded<Map<String, Int>>).data["lazy"])
        }
    }

    @Test
    fun wordCounterFailed() {
        whenever(mockRepository.getBlog(any(), any())).thenAnswer {
            (it.arguments[1] as ((Throwable) -> Unit)).invoke(Exception("Word counter failed"))
        }

        homeViewModel.requestWordCounter()

        homeViewModel.wordCounterLiveData.observeForever {
            assertEquals("Word counter failed", (it as Resource.Failed<*>).error)
        }
    }
}