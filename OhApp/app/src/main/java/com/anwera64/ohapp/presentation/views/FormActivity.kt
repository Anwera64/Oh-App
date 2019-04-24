package com.anwera64.ohapp.presentation.views

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import com.anwera64.ohapp.R
import com.anwera64.ohapp.presentation.presenters.FormPresenterDelegate
import kotlinx.android.synthetic.main.activity_form.*
import kotlinx.android.synthetic.main.activity_form.view.*
import java.util.*

class FormActivity : AppCompatActivity(), FormPresenterDelegate {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        btnAdd.setOnClickListener { checkForCompletion() }
    }

    fun checkForCompletion() {
        val name: String
        val surname: String
        val age: Int
        val birthDate: Date

        if (checkEditText(tilName)) return
        if (checkEditText(tilSurname)) return
        if (checkEditText(tilAge)) return
    }

    fun checkEditText(view: TextInputLayout): Boolean {
        if (TextUtils.isEmpty(view.editText?.text)) {
            view.error = getString(R.string.obligatory_error)
            view.isErrorEnabled = true
            return false
        }

        return true
    }

    override fun onSaved() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onError(e: String?) {
        Log.e("Form", e ?: "error")
    }

}