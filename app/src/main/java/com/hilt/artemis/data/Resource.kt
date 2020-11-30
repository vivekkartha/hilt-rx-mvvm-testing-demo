package com.hilt.artemis.data

// Represents 3 states of data fetch
sealed class Resource<T> {

    // Used to show user feedback during data loading
    class Loading<T> : Resource<T>()

    // Triggered upon successful fetch
    class Loaded<T>(val data: T) : Resource<T>()

    // Triggered when fetch fails
    class Failed<T>(val error: String?) : Resource<T>()
}