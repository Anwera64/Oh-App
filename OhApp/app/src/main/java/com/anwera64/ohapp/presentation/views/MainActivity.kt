package com.anwera64.ohapp.presentation.views

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.anwera64.ohapp.presentation.presenters.MainPresenter
import com.anwera64.ohapp.presentation.presenters.MainPresenterDelegate
import com.anwera64.ohapp.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), MainPresenterDelegate {

    private val mPresenter = MainPresenter(this)
    private val checkingNumber = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnCell.setOnClickListener {
            if (!checkingNumber) onLogin()//TODO
        }

        btnFacebook.setOnClickListener {
            onLogin()//TODO
        }
    }

    override fun onLogin() {
        val intent = Intent(this, FormActivity::class.java)
        startActivity(intent)
    }

    private fun cellphoneLogin(phoneNumber: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60,
            TimeUnit.SECONDS,
            this,
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential?) {
                    p0?.let { mPresenter.loginWithCellphone(p0) }
                }

                override fun onVerificationFailed(p0: FirebaseException?) {
                    Log.e("Main", p0?.message)
                }

            }
        )
    }
}
