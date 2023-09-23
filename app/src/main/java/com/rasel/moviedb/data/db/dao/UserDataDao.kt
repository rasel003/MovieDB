package com.paperflymerchantapp.data.db.dao

import androidx.room.*
import com.paperflymerchantapp.data.db.entities.UserData

/**
 * The Data Access Object for the user class.
 */
@Dao
interface UserDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: UserData?)

    @Query("DELETE FROM user")
    fun deleteUser()

    @Update
    fun update(user: UserData?)

    @Delete
    fun delete(user: UserData?)

    @Query("SELECT * FROM user")
    fun getAllUser(): List<UserData>

    @Query("SELECT * FROM user WHERE merchant_code = :merchantCode")
    fun getUserWithCode(merchantCode: String?): UserData?
}
