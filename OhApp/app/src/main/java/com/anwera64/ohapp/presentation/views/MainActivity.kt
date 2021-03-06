package com.anwera64.ohapp.presentation.views

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.anwera64.ohapp.R
import com.anwera64.ohapp.presentation.presenters.MainPresenter
import com.anwera64.ohapp.presentation.presenters.MainPresenterDelegate
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity(), MainPresenterDelegate, PhoneDialogFragment.PhoneDialogDelegate {

    private val RC_SIGN_IN = 0

    private val mPresenter = MainPresenter(this)
    private var verificationId: String? = null
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val callbackManager by lazy { CallbackManager.Factory.create() }
    private val dialog by lazy { PhoneDialogFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        if (mPresenter.isLogged()) onLogin()

        btnCell.setOnClickListener {
            dialog.show(supportFragmentManager, "PhoneDialogFragment")
        }

        btnGoogle.setOnClickListener { googleSignIn() }

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

    private fun googleSignIn() {
        val intent = mGoogleSignInClient.signInIntent
        startActivityForResult(intent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                mPresenter.loginWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("Main", "Google sign in failed", e)
            }
        }
    }

    override fun onLogin() {
        val intent = Intent(this, FormActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
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
                    dialog.setError(p0?.message ?: "Revisa bien el numero")
                }

                override fun onCodeSent(p0: String?, p1: PhoneAuthProvider.ForceResendingToken?) {
                    super.onCodeSent(p0, p1)
                    dialog.state = PhoneDialogFragment.DialogState.SENT
                    verificationId = p0
                }

            }
        )
    }

    override fun onPhoneInput(phone: String) {
        cellphoneLogin(phone)
    }

    override fun onCodeInput(code: String) {
        verificationId?.let {
            val credentials = PhoneAuthProvider.getCredential(it, code)
            mPresenter.loginWithCredential(credentials)
        }
    }
}
