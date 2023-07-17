package com.example.bookreader.data

data class DataOrExceptionClass<T, Boolean, E : Exception>(
    var data: T? = null,
    var loading: Boolean? = null,
    var e: E? = null,
)
