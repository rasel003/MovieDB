package com.rasel.moviedb.data.preference

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

class AppPrefsManager @SuppressLint("CommitPrefEdits") constructor(var mContext: Context) {
    var preferences: SharedPreferences = mContext.getSharedPreferences(DB_NAME, 0)
    var editor: SharedPreferences.Editor = preferences.edit()

    var fullName: String?
        get() = preferences.getString(FULL_NAME, "")
        set(Id) {
            editor.putString(FULL_NAME, Id)
            editor.apply()
        }

    var userName: String?
        get() = preferences.getString(USER_NAME, "")
        set(userName) {
            editor.putString(USER_NAME, userName)
            editor.apply()
        }

    var password: String?
        get() = preferences.getString(KEY_PASSWORD, "")
        set(password) {
            editor.putString(KEY_PASSWORD, password)
            editor.apply()
        }

    var isLogin: Boolean?
        get() = preferences.getBoolean(KEY_IS_LOGIN, false)
        set(loginStatus) {
            editor.putBoolean(KEY_IS_LOGIN, loginStatus!!)
            editor.apply()
        }

    var isOtpVerified: Boolean?
        get() = preferences.getBoolean(OTP_VERIFICATION_STATUS, false)
        set(loginStatus) {
            editor.putBoolean(OTP_VERIFICATION_STATUS, loginStatus!!)
            editor.apply()
        }

    var merchantCode: String?
        get() = preferences.getString(MERCHANT_CODE, "")
        set(code) {
            editor.putString(MERCHANT_CODE, code)
            editor.apply()
        }

    var userToken: String?
        get() = preferences.getString(TOKEN, "")
        set(token) {
            editor.putString(TOKEN, token)
            editor.apply()
        }

    var email: String?
        get() = preferences.getString(EMAIL, "")
        set(email) {
            editor.putString(EMAIL, email)
            editor.apply()
        }

    var phone: String?
        get() = preferences.getString(PHONE_NO, "")
        set(phone) {
            editor.putString(PHONE_NO, phone)
            editor.apply()
        }

    var userType: String?
        get() = preferences.getString(USER_TYPE, "")
        set(userType) {
            editor.putString(USER_TYPE, userType)
            editor.apply()
        }

    var isCompletedAccount: Boolean?
        get() = preferences.getBoolean(ACCOUNT_COMPLETED_INFO, false)
        set(isCompleted) {
            editor.putBoolean(ACCOUNT_COMPLETED_INFO, isCompleted!!)
            editor.apply()
        }

    var category: String?
        get() = preferences.getString(CATEGORY, "")
        set(value) {
            editor.putString(CATEGORY, value)
            editor.apply()
        }

    var language: String?
        get() = preferences.getString(KEY_LANGUAGE, "en")
        set(lan) {
            editor.putString(KEY_LANGUAGE, lan)
            editor.apply()
        }

    var pickOfficerName: String?
        get() = preferences.getString(PICK_OFFICER_NAME, "")
        set(name) {
            editor.putString(PICK_OFFICER_NAME, name)
            editor.apply()
        }

    var pickOfficerPhone: String?
        get() = preferences.getString(PICK_OFFICER_PHONE, "")
        set(phone) {
            editor.putString(PICK_OFFICER_PHONE, phone)
            editor.apply()
        }

    private fun clearSession() {
        editor.clear()
        editor.apply()
    }


    fun userSingOut() {
        clearSession()
//        val intent = Intent(mContext, PreLoginActivity::class.java)
        // Closing all the Activities
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//        mContext.startActivity(intent)
    }

    fun userSessionExpired() {
        clearSession()
//        val intent = Intent(mContext, PreLoginActivity::class.java)
//        // Closing all the Activities
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//        mContext.startActivity(intent)
    }


    companion object {
        private const val DB_NAME = "merchant_go.db"

        private const val KEY_IS_LOGIN = "is_login"

        private const val USER_NAME = "user_name"
        private const val KEY_PASSWORD = "user_password"
        private const val KEY_LANGUAGE = "user_language_kay"
        private const val PICK_OFFICER_NAME = "pick_officer_name"
        private const val PICK_OFFICER_PHONE = "pick_officer_phone"

        private const val OTP_VERIFICATION_STATUS = "otp_verification_status"

        private const val TOKEN = "user_token_credential"
        private const val FULL_NAME = "user_full_name"
        private const val CATEGORY = "account_category"
        private const val MERCHANT_CODE = "merchant_code"
        private const val EMAIL = "user_email"
        private const val PHONE_NO = "phone_no"
        private const val USER_TYPE = "user_type"
        private const val ACCOUNT_COMPLETED_INFO = "account_completed_info"

    }

}