package com.example.trackertest.data.repository

import com.example.trackertest.data.database.Record
import com.example.trackertest.vo.Resource

interface IRecordRepo {
    suspend fun insertEntityAndGetLast100(record: Record) : List<Record>
    suspend fun getRecords(limit:Int = 100) : Resource<List<Record>>
}