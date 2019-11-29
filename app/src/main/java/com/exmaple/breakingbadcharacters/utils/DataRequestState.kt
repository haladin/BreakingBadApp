package com.exmaple.breakingbadcharacters.utils

sealed class DataRequestState<T> {
    class Start<T> : DataRequestState<T>()
    data class Success<T>(var data: T) : DataRequestState<T>()
    data class Error<T>(val error: String) : DataRequestState<T>()
    data class NoData<T>(val message: String) : DataRequestState<T>()
}