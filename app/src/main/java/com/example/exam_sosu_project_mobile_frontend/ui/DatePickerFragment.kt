package com.example.exam_sosu_project_mobile_frontend.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //arguments.getInt("year")
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        if(arguments!=null&&arguments?.getLong("time")!=null){
            val currDate: Long = arguments?.getLong("time")!!
            c.timeInMillis = currDate;
        }
        val year = c[Calendar.YEAR]
        val month = c[Calendar.MONTH]
        val day = c[Calendar.DAY_OF_MONTH]

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(requireActivity(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // Do something with the date chosen by the user
        val c = Calendar.getInstance()
        c.set(year,month,day)
        setFragmentResult("time",bundleOf("bundleKey" to c.timeInMillis))
    }
}
