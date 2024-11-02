package com.example.deltatestapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance() // For Realtime Database

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState
    var currentUsername: String? = null

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        if (auth.currentUser == null) {
            _authState.value = AuthState.Unauthenticated
        } else {
            _authState.value = AuthState.Authenticated
            currentUsername = auth.currentUser?.displayName
        }
    }

    fun signin(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Fetch username after successful sign-in
                    val userId = auth.currentUser?.uid
                    database.reference.child("users").child(userId!!)
                        .child("username").get()
                        .addOnSuccessListener { snapshot ->
                            currentUsername = snapshot.value as? String
                            _authState.value = AuthState.Authenticated
                        }
                        .addOnFailureListener {
                            _authState.value = AuthState.Error("Failed to retrieve username")
                        }
                } else {
                    _authState.value =
                        AuthState.Error(task.exception?.message ?: "Invalid email or password")
                }
            }
    }

    fun signup(email: String, password: String, username: String) {
        if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
            _authState.value = AuthState.Error("Email, Password, and Username can't be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Update user profile with username
                    auth.currentUser?.updateProfile(
                        UserProfileChangeRequest.Builder()
                            .setDisplayName(username)
                            .build()
                    )?.addOnCompleteListener { updateTask ->
                        if (updateTask.isSuccessful) {
                            val userId = auth.currentUser?.uid
                            val userData = hashMapOf("username" to username)
                            database.reference.child("users").child(userId!!)
                                .setValue(userData)
                                .addOnSuccessListener {
                                    currentUsername = username // Set the current username
                                    _authState.value = AuthState.Authenticated
                                }
                                .addOnFailureListener {
                                    _authState.value = AuthState.Error("Failed to save username")
                                }
                        } else {
                            _authState.value = AuthState.Error("Failed to set username")
                        }
                    }
                } else {
                    _authState.value =
                        AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    fun signout() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }
}


sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}