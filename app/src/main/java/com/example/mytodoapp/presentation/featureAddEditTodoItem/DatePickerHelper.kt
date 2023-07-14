package com.example.mytodoapp.presentation.featureAddEditTodoItem

import android.app.DatePickerDialog
import android.widget.DatePicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mytodoapp.R
import java.sql.Date
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

    private val dateFormat = "yyyy-MMMM-dd"
    private val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())

    private var onDateSetListener: ((java.sql.Date) -> Unit)? = null
    private val outputDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    fun formatDateString(date: Date): String {
        return outputDateFormat.format(date)
    }
    fun showDatePicker(onDateSet: (java.sql.Date) -> Unit) {
        onDateSetListener = onDateSet
        val datePicker = Calendar.getInstance()
        val initialYear = datePicker[Calendar.YEAR]
        val initialMonth = datePicker[Calendar.MONTH]
        val initialDay = datePicker[Calendar.DAY_OF_MONTH]

        DatePickerDialog(
            activity,
            R.style.MyDatePickerDialog,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                datePicker[Calendar.YEAR] = year
                datePicker[Calendar.MONTH] = month
                datePicker[Calendar.DAY_OF_MONTH] = dayOfMonth
                val selectedDate = datePicker.time
                val sqlDate = java.sql.Date(selectedDate.time)
                onDateSetListener?.invoke(sqlDate)
            },
            initialYear,
            initialMonth,
            initialDay
        ).show()
        }
    }
