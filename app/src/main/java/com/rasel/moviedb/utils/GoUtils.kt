package com.paperflymerchantapp.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object GoUtils {
    var TAG = "GoUtils"
    fun convertReadableDate(inputTime: String?): String {
        var result = ""
        val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.US)
        val myFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.US)
        try {
            result = myFormat.format(Objects.requireNonNull(simpleDateFormat.parse(inputTime)))
        } catch (e: ParseException) {
            // TODO Auto-generated catch block
            Log.e(TAG, "convertReadableDate: " + e.message, e)
        }
        return result
    }

    @JvmStatic
    fun convert12HourTime(time24: String?): String {
        var _12HourTime = ""
        try {
            val _24HourSDF = SimpleDateFormat("HH:mm", Locale.US)
            val _12HourSDF = SimpleDateFormat("hh:mm a", Locale.US)
            val _24HourDt = _24HourSDF.parse(time24)
            if (_24HourDt != null) {
                _12HourTime = _12HourSDF.format(_24HourDt)
            }
        } catch (e: Exception) {
            Log.e(TAG, "convert12HourTime: " + e.message, e)
        }
        return _12HourTime
    }

    fun readableDataFormat(inputTime: String?): String {
        var newdate = ""
        //2022-10-27 17:31:51.000000
        try {
            var result = ""
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSSSS", Locale.US)
            val myFormat = SimpleDateFormat("MMMM dd, yyyy HH:mm:ss", Locale.US)
            try {
                result = simpleDateFormat.parse(inputTime)?.let { myFormat.format(it) }.toString()
            } catch (e: ParseException) {
                // TODO Auto-generated catch block
                Log.e(TAG, "convertReadableDate: " + e.message, e)
            }
            return result


        } catch (e: Exception) {
            newdate = "00-00-0000 00:00"
        }
        return newdate
    }

    fun composeEmail(addresses: String, subject: String?, context: Context?) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:") // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(addresses))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        if (intent.resolveActivity(context!!.packageManager) != null) {
            context.startActivity(intent)
        }
    }
}