package com.example.bookreader.screens.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookreader.data.DataOrExceptionClass
import com.example.bookreader.model.MBook
import com.example.bookreader.repository.BookRepository
import com.example.bookreader.repository.FireRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val repository: FireRepository) :
    ViewModel() {
    val data: MutableState<DataOrExceptionClass<List<MBook>, Boolean, Exception>> = mutableStateOf(
        DataOrExceptionClass(listOf(), true, Exception(""))
    )

    init {
        getAllBooksFromDatabase()
    }

    private fun getAllBooksFromDatabase() {
        viewModelScope.launch {
            data.value.loading = true
            data.value = repository.getAllBooksFromDatabase()
            if (!data.value.data.isNullOrEmpty()) data.value.loading = false
        }
        Log.d("getAllBooksFromDatabase", "getAllBooksFromDatabase: ${data.value.data?.toList().toString()}")
    }
}