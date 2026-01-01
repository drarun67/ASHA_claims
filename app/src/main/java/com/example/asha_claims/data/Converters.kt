package com.example.asha_claims.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromActivityEntryList(value: List<ActivityEntry>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toActivityEntryList(value: String): List<ActivityEntry>? {
        val type = object : TypeToken<List<ActivityEntry>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromBankDetails(value: BankDetails?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toBankDetails(value: String?): BankDetails? {
        return value?.let { gson.fromJson(it, BankDetails::class.java) }
    }

    @TypeConverter
    fun fromClaimStatus(value: ClaimStatus): String {
        return value.name
    }

    @TypeConverter
    fun toClaimStatus(value: String): ClaimStatus {
        return ClaimStatus.valueOf(value)
    }

    @TypeConverter
    fun fromActivityCategory(value: ActivityCategory): String {
        return value.name
    }

    @TypeConverter
    fun toActivityCategory(value: String): ActivityCategory {
        return ActivityCategory.valueOf(value)
    }
}
