package com.chococard.carwash.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.chococard.carwash.data.db.dao.JobDao
import com.chococard.carwash.data.db.dao.UserInfoDao
import com.chococard.carwash.data.db.entities.Job
import com.chococard.carwash.data.db.entities.UserInfo

@Database(entities = [UserInfo::class, Job::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getUserInfoDao(): UserInfoDao
    abstract fun getJobDao(): JobDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "CarWash.db"
        ).fallbackToDestructiveMigration().build()
    }

}
