package com.anwera64.ohapp.presentation.views

import android.content.Intent
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
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_form.*
import kotlinx.android.synthetic.main.activity_form.view.*
import java.util.*

class FormActivity : AppCompatActivity(), FormPresenterDelegate {

    private val mPresenter = FormPresenter(this)
    private val RC_SIGN_IN = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.FacebookBuilder().build()
        )

        if (mPresenter.currentUser == null) {
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(),
                RC_SIGN_IN
            )
        } else {
            btnAdd.setOnClickListener { checkForCompletion() }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) btnAdd.setOnClickListener { checkForCompletion() }
        else Toast.makeText(this, "No se logueo correctamente", Toast.LENGTH_LONG).show()

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

        if (TextUtils.isEmpty(etDate.text))
            return
        val birthDate = Date()

        val values = HashMap<String, Any>()
        values["name"] = name
        values["surname"] = surname
        values["age"] = age
        values["birthDate"] = birthDate

        mPresenter.saveForm(values)
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