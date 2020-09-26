package com.example.trackertest.data.database

import androidx.room.*
import com.example.trackertest.data.database.Record.Companion.RECORD_TABLE_NAME

@Entity(tableName = RECORD_TABLE_NAME)
data class Record(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = "_id") var id: Int = 0,
    @ColumnInfo(name =  COLUMN_NAME_SPEED) var speed: Float?,
    @ColumnInfo(name =  COLUMN_NAME_ACCURACY) var accuracy: Float?,
    @ColumnInfo(name = COLUMN_NAME_DATE, defaultValue = "CURRENT_TIMESTAMP")
    var createdAt: String = "",
    @ColumnInfo(name =  COLUMN_NAME_LAT) var latitude: Double?,
    @ColumnInfo(name =  COLUMN_NAME_LONG) var longitude: Double? ){

    constructor() : this(0,0F,0F,"",0.0,0.0)

    companion object{
        const val RECORD_TABLE_NAME = "record"
        const val COLUMN_NAME_SPEED = "speed"
        const val COLUMN_NAME_ACCURACY = "accuracy"
        const val COLUMN_NAME_DATE = "createdAt"
        const val COLUMN_NAME_LAT = "latitude"
        const val COLUMN_NAME_LONG = "longitude"
    }

    override fun toString(): String {
        return "Record {" +
                "speed=" + speed +
                ", precision=" + accuracy +
                ", createdAt=" + createdAt +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}'
    }
}