package com.team4.ecohabit.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

import com.team4.ecohabit.R

class GoogleAuthManager(
    private val context: Context
) {

    private val credentialManager =
        CredentialManager.create(context)

    suspend fun signIn(): FirebaseUser? {

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(
                context.getString(
                    R.string.default_web_client_id
                )
            )
            .build()

        val request =
            GetCredentialRequest.Builder()
                .addCredentialOption(
                    googleIdOption
                )
                .build()

        val result =
            credentialManager.getCredential(
                context,
                request
            )

        val credential =
            result.credential as CustomCredential

        val googleCredential =
            GoogleIdTokenCredential.createFrom(
                credential.data
            )

        val firebaseCredential =
            GoogleAuthProvider.getCredential(
                googleCredential.idToken,
                null
            )

        return FirebaseAuth
            .getInstance()
            .signInWithCredential(
                firebaseCredential
            )
            .await()
            .user
    }
}