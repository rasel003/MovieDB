package com.rasel.moviedb.utils

import android.app.Activity
import android.util.Log
import android.view.WindowManager
import androidx.core.util.Pair
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.datepicker.*
import com.google.android.material.datepicker.CalendarConstraints.DateValidator
import com.rasel.moviedb.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "Utils"

@JvmOverloads
fun Activity.changeStatusBarColor(color: Int, isLight: Boolean) {
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = color

    WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = isLight
}

@JvmOverloads
fun convertDate(date: String?, inFormat: String?, outFormat: String?): String? {
    val sdf = SimpleDateFormat(inFormat, Locale.US)
    var formattedDate: String? = ""
    try {
        val convertedDate = sdf.parse(date)
        formattedDate = if (convertedDate != null) {
            SimpleDateFormat(outFormat, Locale.US).format(convertedDate)
        } else return ""
    } catch (e: ParseException) {
        Log.e(TAG, "convertDate: " + e.message, e)
    } catch (e: NullPointerException) {
        Log.e(TAG, "convertDate: " + e.message, e)
    }
    return formattedDate
}

@JvmOverloads
fun getStringFromDate(date: Date, outFormat: String): String {
    return SimpleDateFormat(outFormat, Locale.US).format(date)
}

@JvmOverloads
fun getDateFromTimeInMillis(time: Long): Date {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = time
    return calendar.time
}

@JvmOverloads
fun getStringDateFromTimeInMillis(time: Long, outFormat: String): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = time
    return SimpleDateFormat(outFormat, Locale.US).format(calendar.time)
}

@JvmOverloads
fun getDateFromString(dateText: String?, inFormat: String?): Date? {
    val sdf = SimpleDateFormat(inFormat, Locale.US)
    return try {
        sdf.parse(dateText)
    } catch (e: ParseException) {
        e.printStackTrace()
        null
    }
}


@JvmOverloads
fun getDatePicker(
    fromNow: Boolean = false,
    futureDate: Int = 0,
    isTodaySelected: Boolean = true
): MaterialDatePicker<Long> {
    val calendar = Calendar.getInstance(Locale.getDefault())
    val constraintsBuilder = CalendarConstraints.Builder()
    val listValidators = ArrayList<DateValidator>()
    if (fromNow) {
        //  CalendarConstraints.DateValidator dateValidatorMin = DateValidatorPointForward.from(calendar.getTimeInMillis());
        listValidators.add(DateValidatorPointForward.now())
        constraintsBuilder.setStart(calendar.timeInMillis)
    }
    if (futureDate > 0) {
        calendar.add(Calendar.DATE, futureDate)
        constraintsBuilder.setEnd(calendar.timeInMillis)
        val dateValidatorMax: DateValidator =
            DateValidatorPointBackward.before(calendar.timeInMillis)
        listValidators.add(dateValidatorMax)
    }
    val validators = CompositeDateValidator.allOf(listValidators)
    constraintsBuilder.setValidator(validators)
    val builder = MaterialDatePicker.Builder.datePicker()
        .setTitleText("Select date")
        .setCalendarConstraints(constraintsBuilder.build())
        .setTheme(R.style.ThemeOverlay_MovieDB_DatePicker)
    if (isTodaySelected) {
        builder.setSelection(MaterialDatePicker.todayInUtcMilliseconds())
    }
    return builder.build()
}

@JvmOverloads
fun getDateRangePicker(
    fromNow: Boolean = false,
    futureDate: Int = 0,
    currentMonthSelected: Boolean = true
): MaterialDatePicker<Pair<Long, Long>> {
    val calendar = Calendar.getInstance(Locale.getDefault())
    val constraintsBuilder = CalendarConstraints.Builder()
    val listValidators = ArrayList<DateValidator>()
    if (fromNow) {
        //  CalendarConstraints.DateValidator dateValidatorMin = DateValidatorPointForward.from(calendar.getTimeInMillis());
        listValidators.add(DateValidatorPointForward.now())
        constraintsBuilder.setStart(calendar.timeInMillis)
    }
    if (futureDate > 0) {
        calendar.add(Calendar.DATE, futureDate)
        constraintsBuilder.setEnd(calendar.timeInMillis)
        val dateValidatorMax: DateValidator =
            DateValidatorPointBackward.before(calendar.timeInMillis)
        listValidators.add(dateValidatorMax)
    }
    val validators = CompositeDateValidator.allOf(listValidators)
    constraintsBuilder.setValidator(validators)
    val builder = MaterialDatePicker.Builder.dateRangePicker()
        .setTitleText("Select dates")
        .setCalendarConstraints(constraintsBuilder.build())
        .setTheme(R.style.ThemeOverlay_MovieDB_DatePicker)
    if (currentMonthSelected) {
        builder.setSelection(
            Pair(
                MaterialDatePicker.thisMonthInUtcMilliseconds(),
                MaterialDatePicker.todayInUtcMilliseconds()
            )
        )
    }
    return builder.build()
}
