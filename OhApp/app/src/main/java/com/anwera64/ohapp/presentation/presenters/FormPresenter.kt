package com.anwera64.ohapp.presentation.presenters

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class FormPresenter(private val view: FormPresenterDelegate) {
    private val db = FirebaseDatabase.getInstance().reference
    private val currentUser = FirebaseAuth.getInstance().currentUser

    private fun saveForm(values: HashMap<String, Any>) {
        currentUser?.let {
            db.child(it.uid)
                .setValue(values)
                .addOnCompleteListener { view.onSaved() }
                .addOnFailureListener { exception ->  view.onError(exception.message) }
        }
    }

    fun saveForm(name: String, surname: String, age: String, birthDate: Date) {
        val values = HashMap<String, Any>()
        values["name"] = name
        values["surname"] = surname
        values["age"] = age
        values["birthDate"] = birthDate

        saveForm(values)
    }
}

interface FormPresenterDelegate {
    fun onSaved()

    fun onError(e: String?)
}