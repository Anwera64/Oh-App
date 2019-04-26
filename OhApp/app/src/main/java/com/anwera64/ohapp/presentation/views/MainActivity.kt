package com.anwera64.ohapp.presentation.views

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.anwera64.ohapp.presentation.presenters.MainPresenter
import com.anwera64.ohapp.presentation.presenters.MainPresenterDelegate
import com.anwera64.ohapp.R
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), MainPresenterDelegate {

    private val mPresenter = MainPresenter(this)
    private val checkingNumber = false
    private val callbackManager by lazy { CallbackManager.Factory.create() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnCell.setOnClickListener {
            if (!checkingNumber) onLogin()//TODO
        }

        btnFacebook.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                result?.let {
                    mPresenter.loginWithFacebook(result.accessToken)
                }
            }

            override fun onCancel() {
                Log.e("Main facebook", "Cancelled")
            }

            override fun onError(error: FacebookException?) {
                Log.e("Main facebook", error?.message ?: "Error")
            }

        })
    }

    override fun onLogin() {
        val intent = Intent(this, FormActivity::class.java)
        startActivity(intent)
    }

    override fun onError(e: String?) {
        Log.e("Main", e)
    }

    private fun cellphoneLogin(phoneNumber: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60,
            TimeUnit.SECONDS,
            this,
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential?) {
                    p0?.let { mPresenter.loginWithCredential(p0) }
                }

                override fun onVerificationFailed(p0: FirebaseException?) {
                    Log.e("Main", p0?.message)
                }

                override fun onCodeSent(p0: String?, p1: PhoneAuthProvider.ForceResendingToken?) {
                    super.onCodeSent(p0, p1)
                }

            }
        )
    }
}
