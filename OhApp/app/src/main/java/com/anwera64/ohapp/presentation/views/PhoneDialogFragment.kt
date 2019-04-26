package com.anwera64.ohapp.presentation.views

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.DialogFragment
import android.text.TextUtils
import android.view.View
import android.widget.Button
import com.anwera64.ohapp.R
import kotlinx.android.synthetic.main.fragment_phone_dialog.*
import kotlin.properties.ObservableProperty
import kotlin.reflect.KProperty


class PhoneDialogFragment : DialogFragment() {

    lateinit var delegate: PhoneDialogDelegate
    private var tilPhone: TextInputLayout? = null

    enum class DialogState { UNSENT, SENT }

    var state: DialogState by object : ObservableProperty<DialogState>(DialogState.UNSENT) {
        override fun afterChange(property: KProperty<*>, oldValue: DialogState, newValue: DialogState) {
            when (state) {
                PhoneDialogFragment.DialogState.UNSENT -> {
                    tilPhone?.editText?.text?.clear()
                    tilPhone?.hint = getString(R.string.hint_phone_number)
                }
                PhoneDialogFragment.DialogState.SENT -> {
                    tilPhone?.editText?.text?.clear()
                    tilPhone?.hint = getString(R.string.hint_code)
                }
            }
        }
    }

    interface PhoneDialogDelegate {
        fun onPhoneInput(phone: String)

        fun onCodeInput(code: String)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            delegate = activity as PhoneDialogDelegate
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + " must implement PhoneDialogDelegate")
        }

    }

    private fun onClickUnsent() {
        if (TextUtils.isEmpty(tilPhone?.editText?.text)) {
            tilPhone?.error = getString(com.anwera64.ohapp.R.string.obligatory_error)
        } else {
            delegate.onPhoneInput(tilPhone?.editText?.text.toString())
        }
    }

    private fun onClickSent() {
        if (TextUtils.isEmpty(tilPhone?.editText?.text)) {
            tilPhone?.error = getString(com.anwera64.ohapp.R.string.obligatory_error)
        } else {
            delegate.onPhoneInput(tilPhone?.editText?.text.toString())
            dismiss()
        }
    }

    fun setError(error: String) {
        tilPhone?.let {
            it.error = error
            it.isErrorEnabled = true
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.confirmation_phone))
        val layout = activity?.layoutInflater?.inflate(com.anwera64.ohapp.R.layout.fragment_phone_dialog, dialogRoot)
        builder.setView(layout)
            .setPositiveButton("Confirmar") { _, _ ->}
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.cancel()
            }

        val dialog = builder.create()

        //De esta manera al presional onClick no se cierra automaticamente el dialog
        dialog.setOnShowListener {
            val positiveButton: Button = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                tilPhone = layout?.findViewById(R.id.tilPhone)
                when (state) {
                    DialogState.UNSENT -> onClickUnsent()
                    DialogState.SENT -> onClickSent()
                }
            }
        }

        return dialog
    }
}
