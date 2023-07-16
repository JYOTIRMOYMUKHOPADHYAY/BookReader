package com.example.bookreader.screens.login


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookreader.model.MUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginScreenViewModel : ViewModel() {
    // val loadingState = MutableStateFlow(LoadingState.IDLE)
    private val auth: FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    fun signInWithEmailAndPassword(email: String, password: String, home: () -> Unit)
       = viewModelScope.launch{
            try {
                auth.signInWithEmailAndPassword(
                    email,
                    password
                ).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("FB_ON_SUCCESS_LOGIN", "$task")
                        home()
                    } else {
                        Log.d("FB_ON_FAILED_LOGIN", "$task")
                    }
                }
            } catch (ex: Exception) {
                Log.d("FB_LOGIN_ERROR", "signInWithEmailAndPassword: ${ex.message}")
            }
        }


    fun createUserWithEmailAndPassword(email: String, password: String, home: () -> Unit){
        if (_loading.value == false) {
            _loading.value = true
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {task -> 
                if(task.isSuccessful) {
                    val displayName = task.result.user?.email?.split("@")?.get(0)
                    createUser(displayName)
                    home()
                }else{
                    Log.d("FB_USER_CREATE_ERROR", "createUserWithEmailAndPassword: ${task.toString()}")
                }
                _loading.value = false
            }
        }
    }

    private fun createUser(displayName: String?) {
        val userId = auth.currentUser?.uid
        val user = MUser(userId = userId.toString(), displayName = displayName.toString(), avatarUrl = "", quote = "Life quotes", profession = "Andriod developer", id = null).toMap()

        FirebaseFirestore.getInstance().collection("users").add(user).addOnCompleteListener {
            Log.d("ON_CREATE_USER_DB_SYNC", "createUser: ${it.toString()}")
        }
    }


}