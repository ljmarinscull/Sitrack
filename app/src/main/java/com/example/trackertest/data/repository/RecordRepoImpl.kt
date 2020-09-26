package com.example.trackertest.data.repository

import com.example.trackertest.data.database.Record
import com.example.trackertest.data.interfaces.RecordDAO
import com.example.trackertest.vo.Resource

class RecordRepoImpl(private val recordDao: RecordDAO) : IRecordRepo {

    override suspend fun insertEntityAndGetLast100(record: Record): List<Record> {
        return recordDao.insertEntityAndGetLast100(
            record.speed!!,
            record.accuracy!!,
            record.latitude!!,
            record.longitude!!
        )
    }

    override suspend fun getRecords(limit: Int): Resource<List<Record>> {
        val records = recordDao.getRecords(limit)
        return Resource.Success(records)
    }
}