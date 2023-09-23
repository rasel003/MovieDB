package com.rasel.moviedb.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.text.Editable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.rasel.moviedb.R
import com.rasel.moviedb.data.preference.AppPrefsManager
import es.dmoral.toasty.Toasty
import java.io.File


fun Context.toastL(message: String) {
    Toasty.info(this, message, Toast.LENGTH_LONG).show()
}

fun Context.toastSystemL(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.toastSystemCopy(message: String) {
    val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
    toast.setGravity(Gravity.TOP, 0, 0);
    toast.show()
}

fun Context.toastS(message: String) {
    Toasty.info(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.toastError(message: String) {
    Toasty.error(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.toastWarning(message: String) {
    Toasty.warning(this, message, Toast.LENGTH_LONG).show()
}

fun Context.toastInfo(message: String) {
    Toasty.info(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.toastLongInfo(message: String) {
    Toasty.info(this, message, Toast.LENGTH_LONG).show()
}

fun Context.toastSuccess(message: String) {
    Toasty.success(this, message, Toast.LENGTH_LONG).show()
}

fun Context.toastWarningLong(message: String) {
    Toasty.warning(this, message, Toast.LENGTH_LONG).show()
}

fun Context.toastSuccessLong(message: String) {
    Toasty.success(this, message, Toast.LENGTH_LONG).show()
}

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

fun Activity.hideKeyboard() {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view = currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun View.hideKeyboardInAndroidFragment() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showKeyboardInAndroidFragment() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(0, 0)
}

fun Context.getFileName(uri: Uri): String? = when (uri.scheme) {
    ContentResolver.SCHEME_CONTENT -> getContentFileName(uri)
    else -> uri.path?.let(::File)?.name
}

private fun Context.getContentFileName(uri: Uri): String? = runCatching {
    contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        cursor.moveToFirst()
        return@use cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME).let(cursor::getString)
    }
}.getOrNull()

fun String.maskNumber(): String {
    return mapIndexed { index, c ->
        if (index > 3 && length - index > 2) "X" else c
    }.joinToString("")
}



