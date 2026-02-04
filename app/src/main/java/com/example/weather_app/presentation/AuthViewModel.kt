package com.example.weather_app.presentation

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    init {
        signInAnonymously()
    }

    private fun signInAnonymously() {
        if (auth.currentUser == null) {
            auth.signInAnonymously()
        }
    }

    fun getUserId(): String {
        return auth.currentUser?.uid ?: ""
    }
}
