package com.example.trackertest.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.trackertest.data.interfaces.RecordDAO

@Database(entities = [
    Record::class,
    ], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun recordDAO() : RecordDAO

    companion object {
        @Volatile
        var INSTANCE: AppDatabase? = null

        fun getAppDataBase(context: Context): AppDatabase {
            synchronized(this){
                var instance = INSTANCE
                if(instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext
                        , AppDatabase::class.java
                        , "TRACKER_DATABASE"
                    ).addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                        }
                    }).build()
                }
                return instance
            }
        }

        fun destroyDataBase(){
            INSTANCE = null
        }
    }
}
