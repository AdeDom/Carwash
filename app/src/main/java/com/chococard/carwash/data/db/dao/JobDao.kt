package com.chococard.carwash.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chococard.carwash.data.db.entities.Job

@Dao
interface JobDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveJob(job: Job)

    @Query("SELECT * FROM job")
    fun getJob(): LiveData<Job>

    @Query("SELECT * FROM job")
    suspend fun getDbJob(): Job?

    @Query("DELETE FROM job")
    suspend fun deleteJob()

}
