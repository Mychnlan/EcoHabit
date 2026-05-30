package com.team4.ecohabit.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

object FirebaseAuthManager {

    private val auth = FirebaseAuth.getInstance()

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->

                val user = result.user

                if (user != null) {

                    user.reload().addOnSuccessListener {

                        if (user.isEmailVerified) {
                            onSuccess()
                        } else {
                            auth.signOut()
                            onError("Email belum diverifikasi. Cek inbox email kamu.")
                        }
                    }

                } else {
                    onError("User tidak ditemukan")
                }
            }
            .addOnFailureListener {
                onError(it.localizedMessage ?: "Login failed")
            }
    }

    fun register(
        fullName: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        auth.createUserWithEmailAndPassword(
            email,
            password
        )
            .addOnSuccessListener { result ->

                val user = result.user

                val profileUpdates =
                    UserProfileChangeRequest.Builder()
                        .setDisplayName(fullName)
                        .build()

                user?.updateProfile(profileUpdates)

                user?.sendEmailVerification()
                    ?.addOnSuccessListener {

                        onSuccess()
                    }
                    ?.addOnFailureListener {

                        onError(
                            "Failed to send verification email"
                        )
                    }
            }
            .addOnFailureListener {

                onError(
                    it.localizedMessage
                        ?: "Registration failed"
                )
            }
    }
}