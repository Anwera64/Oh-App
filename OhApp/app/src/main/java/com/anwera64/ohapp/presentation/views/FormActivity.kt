package com.anwera64.ohapp.presentation.views

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.text.format.DateFormat
import android.util.Log
import android.widget.Toast
import com.anwera64.ohapp.R
import com.anwera64.ohapp.presentation.presenters.FormPresenter
import com.anwera64.ohapp.presentation.presenters.FormPresenterDelegate
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_form.*
import kotlinx.android.synthetic.main.activity_form.view.*
import java.util.*

class FormActivity : AppCompatActivity(), FormPresenterDelegate, DatePickerFragment.DatePickerDeelgate {

    private val mPresenter = FormPresenter(this)
    private val RC_SIGN_IN = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        btnAdd.setOnClickListener { checkForCompletion() }
        tilDate.editText?.setOnClickListener {
            val picker = DatePickerFragment()
            picker.show(supportFragmentManager, "DatePickerFragment")

        }
    }

    override fun onDateSelected(date: Date) {
        val dateString = DateFormat.format("dd/MM/yyyy", date).toString()
        tilDate.editText?.setText(dateString)
    }

    private fun checkForCompletion() {
        if (checkEditText(tilName))
            return
        val name = tilName.editText?.text.toString()

        if (checkEditText(tilSurname))
            return
        val surname = tilSurname.editText?.text.toString()

        if (checkEditText(tilAge))
            return
        val age = tilAge.editText?.text.toString()

        if (checkEditText(tilDate))
            return
        val birthDate = Date()

        mPresenter.saveForm(name, surname, age, birthDate)
    }

    private fun checkEditText(view: TextInputLayout): Boolean {
        if (TextUtils.isEmpty(view.editText?.text)) {
            view.error = getString(R.string.obligatory_error)
            view.isErrorEnabled = true
            return true
        }
        return false
    }

    override fun onSaved() {
        Toast.makeText(this, "Se guardo correctamente", Toast.LENGTH_LONG).show()
    }

    override fun onError(e: String?) {
        Log.e("Form", e ?: "error")
    }

}