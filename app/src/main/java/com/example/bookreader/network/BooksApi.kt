package com.example.bookreader.network

import com.example.bookreader.model.Book
import com.example.bookreader.model.Item
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface BooksApi {
    @GET("volumes")
    suspend fun getAllBooks(@Query("q") query: String): Book

    @GET("volumes/{bookId}")
    suspend fun getBOokInfo(@Path("bookId") bookId: String): Item
}