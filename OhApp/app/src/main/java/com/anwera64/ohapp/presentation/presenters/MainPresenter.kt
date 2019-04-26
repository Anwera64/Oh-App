package com.anwera64.ohapp.presentation.presenters

import com.facebook.AccessToken
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential

class MainPresenter(val view: MainPresenterDelegate) {

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
}

interface MainPresenterDelegate {
    fun onLogin()

    fun onError(e: String?)
}