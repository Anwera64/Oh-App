package com.anwera64.ohapp.presentation.presenters

import android.text.format.DateFormat
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class FormPresenter(private val view: FormPresenterDelegate) {
    private val db = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()

    private fun saveForm(values: HashMap<String, Any>) {
        auth.currentUser?.let {
            db.child("${it.uid}/${UUID.randomUUID()}")
                .setValue(values)
                .addOnCompleteListener { view.onSaved() }
                .addOnFailureListener { exception -> view.onError(exception.message) }
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun saveForm(name: String, surname: String, age: String, birthDate: Date) {
        val values = HashMap<String, Any>()
        values["name"] = name
        values["surname"] = surname
        values["age"] = age
        values["birthDate"] = DateFormat.format("dd/MM/yy", birthDate)

        saveForm(values)
    }
}

interface FormPresenterDelegate {
    fun onSaved()

    fun onError(e: String?)
}