package com.example.trackertest.data.interfaces

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.trackertest.data.database.Record
import com.example.trackertest.data.database.Record.Companion.RECORD_TABLE_NAME

@Dao
interface RecordDAO {

    @Query("INSERT INTO $RECORD_TABLE_NAME (speed,accuracy,latitude,longitude) VALUES(:speed,:accuracy,:latitude,:longitude)")
    fun insertEntity(speed: Float,accuracy: Float, latitude: Double, longitude: Double)

    @Query("SELECT * FROM $RECORD_TABLE_NAME ORDER BY _id DESC LIMIT :limit")
    suspend fun getRecords(limit: Int = 100): List<Record>

    @Transaction
    suspend fun insertEntityAndGetLast100(speed: Float, accuracy: Float, latitude: Double, longitude: Double) : List<Record> {
        insertEntity(speed,accuracy, latitude, longitude)
        return getRecords()
    }
}