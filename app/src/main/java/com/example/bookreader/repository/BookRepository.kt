package com.example.bookreader.repository


import android.util.Log
import com.example.bookreader.data.DataOrException
import com.example.bookreader.data.Resource
import com.example.bookreader.model.Item
import com.example.bookreader.network.BooksApi
import javax.inject.Inject

class BookRepository @Inject constructor(private val api: BooksApi) {
//    private val dataOrException = DataOrException<List<Item>, Boolean, Exception>()
//    private val bookInfoDataOrException = DataOrException<Item, Boolean, Exception>()
//    suspend fun getBooks(searchQuery: String): DataOrException<List<Item>, Boolean, Exception> {
//        try {
//            dataOrException.loading = true
//            dataOrException.data = api.getAllBooks(searchQuery).items
//            if (dataOrException.data!!.isNotEmpty()) dataOrException.loading = false
//        } catch (e: Exception) {
//            dataOrException.e = e
//        }
//        return dataOrException
//    }

    suspend fun getBooks(searchQuery: String): Resource<List<Item>> {
        return try {
            Resource.Loading(data = true)
            val itemList = api.getAllBooks(searchQuery).items
            Log.d("LISTDATA", "searchBooks: ${itemList.toString()}")
            if(itemList.isNotEmpty()) Resource.Loading(data = false)
            Resource.Success(data = itemList)
        } catch (e: Exception) {
            Log.d("TAG", "getBooks: ${e.message.toString()}")
            Resource.Error(message = e.message.toString())

        }
    }


    //    suspend fun getBookInfo(bookId: String): DataOrException<Item, Boolean, Exception> {
//        val response = try {
//            bookInfoDataOrException.loading = true
//            bookInfoDataOrException.data = api.getBOokInfo(bookId)
//            if (bookInfoDataOrException.data.toString().isNotEmpty()) {
//                bookInfoDataOrException.loading = false
//            } else {
//                bookInfoDataOrException.loading = false
//            }
//        } catch (e: Exception) {
//            bookInfoDataOrException.e = e
//        }
//        return bookInfoDataOrException
//    }
    suspend fun getBookInfo(bookId: String): Resource<Item> {
        val response = try {
            Resource.Loading(data = true)
            api.getBOokInfo(bookId)
        } catch (e: Exception) {
            return Resource.Error(message = e.message.toString())
        }
        Resource.Loading(data = false)
        return Resource.Success(data = response)
    }
}