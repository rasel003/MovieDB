package com.paperflymerchantapp.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserData(
    @PrimaryKey
    var merchant_code: String,
    val user_name: String,
    val token: String,
    val password: String,
    val full_name: String,
    val email: String,
    val category: String,
    val phone: String,
    val user_type: String,
    val is_completed_account: Boolean,

    )