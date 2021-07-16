package com.example.internapp.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FirebaseRepository {
    private val fbAuth = FirebaseAuth.getInstance()

    fun isUserLoggedIn(): Boolean {
        Firebase.auth.currentUser?.let {
            return true
        }
        return false
    }

    fun currentUser(): FirebaseUser? {
        return Firebase.auth.currentUser
    }

    fun signUp(email: String, password: String): Task<AuthResult> {
        return fbAuth.createUserWithEmailAndPassword(email, password)
    }

    fun signIn(email: String, password: String): Task<AuthResult> {
        return fbAuth.signInWithEmailAndPassword(email, password)
    }

    fun signOut() {
        fbAuth.signOut()
    }
}