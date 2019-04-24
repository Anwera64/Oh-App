package com.anwera64.ohapp.presentation.presenters

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential

class MainPresenter(view: MainPresenterDelegate) {

    private val mAuth = FirebaseAuth.getInstance()

    fun loginWithFacebook() {

    }

    fun loginWithCellphone(phoneCredential: PhoneAuthCredential) {

    }
}

interface MainPresenterDelegate {
    fun onLogin()
}