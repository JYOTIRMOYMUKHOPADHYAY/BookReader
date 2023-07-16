package com.example.bookreader.screens.search


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookreader.data.Resource
import com.example.bookreader.model.Item
import com.example.bookreader.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookSearchViewModel @Inject constructor(private val repository: BookRepository) :
    ViewModel() {
//    val listOfBooks: MutableState<DataOrException<List<Item>, Boolean, Exception>> = mutableStateOf(
//        DataOrException(null, true, Exception("")))

    var list: List<Item> by mutableStateOf(listOf())
    var isLoading: Boolean by mutableStateOf(true)

    init {
        searchBooks("android")
    }

    fun searchBooks(query: String) {
        viewModelScope.launch() {
            isLoading = true
            if (query.isEmpty()) {
                return@launch
            }
            try {
                when (val response = repository.getBooks(query)) {
                    is Resource.Success -> {
                        list = response.data!!
                        Log.d("LISTDATA", "searchBooks: ${list.toString()}")
                        if (list.isNotEmpty()) isLoading = false
                    }

                    is Resource.Error -> {
                        isLoading = false

                    }

                    else -> {
                        isLoading = false
                    }
                }
            } catch (e: Exception) {
                isLoading = false
                Log.d("searchBooks_ERR", "searchBooks: FAILED TO GET BOOKS ${e.message.toString()}")
            }
        }

    }


//        fun searchBooks(query: String) {
//        viewModelScope.launch() {
//            if (query.isEmpty()) {
//                return@launch
//            }
//            listOfBooks.value.loading = true
//            listOfBooks.value = repository.getBooks((query))
//            Log.d("ReaderSearchScreen", "ReaderSearchScreen: ${listOfBooks.value.toString()}")
//            Log.d("ReaderSearchScreen", "ReaderSearchScreen: ${listOfBooks.value.data?.size}")
//            if(listOfBooks.value.data.toString().isNotEmpty()) {
//                listOfBooks.value.loading = false
//
//            }
//        }

}