package com.example.mytodoapp.presentation.featureAddEditTodoItem

import android.app.DatePickerDialog
import android.widget.DatePicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mytodoapp.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * This class is a helper class for handling date picking functionality in an Android application.
 * It provides a method to set the date data on a TextView, based on the selected date from a DatePickerDialog.
 * The class follows the single responsibility principle by focusing on the specific task of date picking.
 *
 * @param activity The AppCompatActivity instance associated with the date picker.
 */

class DatePickerHelper(private val activity: AppCompatActivity) {

    private val dateFormat = "dd-MMMM-yyyy"
    private val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())

    fun setData(tvCalendar: TextView, datePicker: Calendar, isCalendarVisible: Boolean,
        onDateSet: (java.sql.Date) -> Unit) {
        val dateListener =
            DatePickerDialog.OnDateSetListener { _: DatePicker?, year: Int, month: Int, dayOfMonth:
            Int -> datePicker[Calendar.YEAR] = year
                datePicker[Calendar.MONTH] = month
                datePicker[Calendar.DAY_OF_MONTH] = dayOfMonth
                val selectedDate = datePicker.time
                tvCalendar.text = simpleDateFormat.format(selectedDate)
                val sqlDate = java.sql.Date(selectedDate.time)
                onDateSet(sqlDate)
            }
        if (isCalendarVisible) {
            val initialYear = datePicker[Calendar.YEAR]
            val initialMonth = datePicker[Calendar.MONTH]
            val initialDay = datePicker[Calendar.DAY_OF_MONTH]
            DatePickerDialog(activity, R.style.MyDatePickerDialog, dateListener, initialYear,
                initialMonth, initialDay).show()
        }
    }
}