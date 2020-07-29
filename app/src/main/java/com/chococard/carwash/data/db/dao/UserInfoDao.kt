package com.chococard.carwash.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chococard.carwash.data.db.entities.UserInfo

@Dao
interface UserInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserInfo(user: UserInfo)

    @Query("SELECT * FROM user_info")
    fun getDbUserInfoLiveData(): LiveData<UserInfo>

    @Query("SELECT * FROM user_info")
    suspend fun getDbUserInfo(): UserInfo?

    @Query("DELETE FROM user_info")
    suspend fun deleteUserInfo()

}
