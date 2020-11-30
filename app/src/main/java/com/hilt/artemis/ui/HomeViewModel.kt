package com.hilt.artemis.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hilt.artemis.data.BlogRepository
import com.hilt.artemis.data.Resource
import java.util.Locale

open class HomeViewModel @ViewModelInject constructor(
    private val repository: BlogRepository
) : ViewModel() {

    // Assumption: As per the requirement all are considered to be 3 different requests

    val tenthCharacterLiveData: LiveData<Resource<Char>>
        get() = _tenthCharacterLiveData

    val every10thLiveData: LiveData<Resource<List<Char>>>
        get() = _every10thLiveData

    val wordCounterLiveData: LiveData<Resource<Map<String, Int>>>
        get() = _wordCounterLiveData

    private val _tenthCharacterLiveData = MutableLiveData<Resource<Char>>()
    private val _wordCounterLiveData = MutableLiveData<Resource<Map<String, Int>>>()
    private val _every10thLiveData = MutableLiveData<Resource<List<Char>>>()

    /**
     * Get the 10th character (including whitespace)
     **/

    fun request10thCharacter() {
        // Emit loading state
        _tenthCharacterLiveData.value = Resource.Loading()
        // Fetch blog and 10th char
        repository.getBlog(
            onSuccess = {
                process10thCharacter(it)
            },
            onFailed = {
                _tenthCharacterLiveData.value = Resource.Failed(it.localizedMessage)
            })
    }

    private fun getTenthChar(response: String): Char? =
        try {
            response.toCharArray()[INDEX_TENTH_CHAR]
        } catch (e: IndexOutOfBoundsException) {
            null
        }

    private fun process10thCharacter(response: String) {
        getTenthChar(response)?.let { tenthChar ->
            _tenthCharacterLiveData.value = Resource.Loaded(tenthChar)
        } ?: run {
            _tenthCharacterLiveData.value = Resource.Failed("10th element not found")
        }
    }

    /**
     * Get every 10th character (including whitespaces)
     **/

    fun requestEvery10thCharacter() {
        // Emit loading state
        _every10thLiveData.value = Resource.Loading()
        // fetch blog and every 10th char
        repository.getBlog(
            onSuccess = { response -> processEvery10thCharacter(response) },
            onFailed = { error ->
                _every10thLiveData.value = Resource.Failed(error.localizedMessage)
            }
        )
    }

    private fun getEvery10thChar(response: String?): List<Char>? =
        response?.toCharArray()
            ?.filterIndexed { index, _ -> index != 0 && index % INDEX_TENTH_CHAR == 0 }

    private fun processEvery10thCharacter(response: String?) {
        getEvery10thChar(response)?.let { every10thList ->
            _every10thLiveData.value = Resource.Loaded(every10thList)
        } ?: run {
            _every10thLiveData.value = Resource.Failed("No 10th element")
        }
    }

    /**
     * Get every word count (case-insensitive; no whitespaces)
     **/

    fun requestWordCounter() {
        // Emit loading state
        _wordCounterLiveData.value = Resource.Loading()
        // fetch word count for each word
        repository.getBlog(
            onSuccess = { response ->
                processWordCount(response)
            },
            onFailed = { error ->
                _wordCounterLiveData.value = Resource.Failed(error.localizedMessage)
            }
        )
    }

    private fun processWordCount(response: String?) {
        response?.let { text ->
            _wordCounterLiveData.value = Resource.Loaded(getEachWordCount(text))
        } ?: run {
            _wordCounterLiveData.value = Resource.Failed("Word counter failed")
        }
    }

    private fun getEachWordCount(response: String): Map<String, Int> {
        return response.toLowerCase(Locale.getDefault())
            .split("\\s+".toRegex())
            .groupingBy { it }
            .eachCount()
    }

    override fun onCleared() {
        super.onCleared()
        repository.cancelAll()
    }

    companion object {
        private const val INDEX_TENTH_CHAR = 9
    }
}