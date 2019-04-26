package com.anwera64.ohapp.presentation.views

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.text.format.DateFormat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.anwera64.ohapp.R
import com.anwera64.ohapp.presentation.extensions.checkEditText
import com.anwera64.ohapp.presentation.presenters.FormPresenter
import com.anwera64.ohapp.presentation.presenters.FormPresenterDelegate
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_form.*
import kotlinx.android.synthetic.main.activity_form.view.*
import java.util.*

class FormActivity : AppCompatActivity(), FormPresenterDelegate {

    private val mPresenter = FormPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        btnAdd.setOnClickListener { checkForCompletion() }
        datePicker.maxDate = Calendar.getInstance().time.time
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.logout_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.logout -> {
                mPresenter.logout()
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkForCompletion() {
        if (tilName.checkEditText(getString(R.string.obligatory_error)))
            return
        val name = tilName.editText?.text.toString()

        if (tilSurname.checkEditText(getString(R.string.obligatory_error)))
            return
        val surname = tilSurname.editText?.text.toString()

        if (tilAge.checkEditText(getString(R.string.obligatory_error)))
            return
        val age = tilAge.editText?.text.toString()

        val calendar = Calendar.getInstance()
        calendar.set(datePicker.year, datePicker.month, datePicker.dayOfMonth)

        mPresenter.saveForm(name, surname, age, calendar.time)
    }

    override fun onSaved() {
        Toast.makeText(this, "Se guardo correctamente", Toast.LENGTH_LONG).show()
        tilName.editText?.text?.clear()
        tilSurname.editText?.text?.clear()
        tilAge.editText?.text?.clear()

        val date = Calendar.getInstance().time
        datePicker.updateDate(date.year, date.month, date.day)
    }

    override fun onError(e: String?) {
        Log.e("Form", e ?: "error")
    }

}

