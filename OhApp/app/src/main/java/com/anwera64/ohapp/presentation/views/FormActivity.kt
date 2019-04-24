package com.anwera64.ohapp.presentation.views

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.anwera64.ohapp.R
import com.anwera64.ohapp.presentation.presenters.FormPresenter
import com.anwera64.ohapp.presentation.presenters.FormPresenterDelegate
import kotlinx.android.synthetic.main.activity_form.*
import kotlinx.android.synthetic.main.activity_form.view.*
import java.util.*

class FormActivity : AppCompatActivity(), FormPresenterDelegate {

    private val mPresenter = FormPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        btnAdd.setOnClickListener { checkForCompletion() }
    }

    private fun checkForCompletion() {
        if (checkEditText(tilName)) return
        val name = tilName.editText?.text.toString()

        if (checkEditText(tilSurname)) return
        val surname = tilSurname.editText?.text.toString()

        if (checkEditText(tilAge)) return
        val age = tilAge.editText?.text.toString()

        if (TextUtils.isEmpty(etDate.text)) return
        val birthDate = Date()

        mPresenter.saveForm(name, surname, age, birthDate)
    }

    private fun checkEditText(view: TextInputLayout): Boolean {
        if (TextUtils.isEmpty(view.editText?.text)) {
            view.error = getString(R.string.obligatory_error)
            view.isErrorEnabled = true
            return false
        }
        return true
    }

    override fun onSaved() {
        Toast.makeText(this, "Se guardo correctamente", Toast.LENGTH_LONG).show()
    }

    override fun onError(e: String?) {
        Log.e("Form", e ?: "error")
    }

}