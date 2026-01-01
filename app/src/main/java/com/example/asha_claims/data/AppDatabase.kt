package com.example.asha_claims.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [AshaProfile::class, IncentiveClaim::class, TrainingRecord::class, SchemeEnrollment::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun ashaProfileDao(): AshaProfileDao
    abstract fun incentiveClaimDao(): IncentiveClaimDao
    abstract fun trainingDao(): TrainingDao
    abstract fun schemeDao(): SchemeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "asha_claims_database"
                )
                .fallbackToDestructiveMigration() // Wipes data on schema change (good for dev)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
