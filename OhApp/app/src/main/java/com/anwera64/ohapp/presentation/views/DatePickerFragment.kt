package com.anwera64.ohapp.presentation.views

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.DatePicker
import java.util.*

class DatePickerFragment: DialogFragment(), DatePickerDialog.OnDateSetListener {

    interface DatePickerDeelgate {
        fun onDateSelected(date: Date)
    }

    private lateinit var delegate: DatePickerDeelgate

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(context, this, year, month, day)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            delegate = activity as DatePickerDeelgate
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + " must implement DatePickerDeelgate")
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val date = Calendar.getInstance()
        date.set(year, month, dayOfMonth)

        delegate.onDateSelected(date.time)
    }
}