package com.example.bookreader.repository

import com.example.bookreader.data.DataOrExceptionClass
import com.example.bookreader.model.MBook
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireRepository @Inject constructor(private val queryBookRepository: Query) {

    suspend fun getAllBooksFromDatabase(): DataOrExceptionClass<List<MBook>, Boolean, Exception> {
        val dataOreException = DataOrExceptionClass<List<MBook>, Boolean, Exception>()

        try {
            dataOreException.loading = true
            dataOreException.data =
                queryBookRepository.get().await().documents.map { documentSnapshot ->
                    documentSnapshot.toObject(MBook::class.java)!!
                }
            if(!dataOreException.data.isNullOrEmpty()) dataOreException.loading = false
        } catch (exception: FirebaseFirestoreException) {
            dataOreException.e = exception
        }
        return dataOreException
    }
}