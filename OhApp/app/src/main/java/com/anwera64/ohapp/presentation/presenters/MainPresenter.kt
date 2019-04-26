package com.anwera64.ohapp.presentation.presenters

import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.*

class MainPresenter(private val view: MainPresenterDelegate) {

    private val mAuth = FirebaseAuth.getInstance()


    fun loginWithFacebook(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        loginWithCredential(credential)
    }

    fun loginWithCredential(credential: AuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnSuccessListener { view.onLogin() }
            .addOnFailureListener { exception -> view.onError(exception.message) }
    }

    fun loginWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        loginWithCredential(credential)
    }

    fun isLogged(): Boolean {
        mAuth.currentUser?.let { return true }
        return false
    }
}

interface MainPresenterDelegate {
    fun onLogin()

    fun onError(e: String?)
}