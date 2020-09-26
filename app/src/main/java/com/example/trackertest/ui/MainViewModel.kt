package com.example.trackertest.ui

import android.location.Location
import android.util.Log
import androidx.lifecycle.*
import com.example.trackertest.data.repository.IRecordRepo
import kotlinx.coroutines.Dispatchers
import com.example.trackertest.data.database.Record
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel(private val repo: IRecordRepo) : ViewModel() {

    private val _recordList = MutableLiveData(emptyList<Record>())
    val recordList : LiveData<List<Record>>
        get() = _recordList

    private val _record = MutableLiveData(Record())
    val record : LiveData<Record>
        get() = _record

    private val _lastSpeedMin = MutableLiveData<Float>(0F)
    val lastSpeedMin : LiveData<Float>
        get() = _lastSpeedMin

    private val _lastSpeedMax = MutableLiveData<Float>(0F)
    val lastSpeedMax : LiveData<Float>
        get() = _lastSpeedMax

    private val _lastSpeedAvg = MutableLiveData<Double>(0.0)
    val lastSpeedAvg : LiveData<Double>
        get() = _lastSpeedAvg


    fun createRecord(location: Location){
        val record =  Record(0,location.speed,location.accuracy,"",location.latitude,location.longitude)
        viewModelScope.launch(Dispatchers.Main) {
            try {
                var result: List<Record>?
                withContext(Dispatchers.IO) {
                    result = repo.insertEntityAndGetLast100(record)
                }
                _recordList.value = result
                updateVariables()
            } catch (e: Exception){

            }
        }
    }

    private fun updateVariables(){
        if(_recordList.value != null && _recordList.value!!.isNotEmpty()){
             val arrayList = ArrayList(_recordList.value!!)

            _record.value = arrayList.last()
            _lastSpeedMin.value = arrayList.minByOrNull { it.speed!! }?.speed!!
            _lastSpeedMax.value = arrayList.maxByOrNull { it.speed!! }?.speed!!
            _lastSpeedAvg.value = arrayList.map { it -> it.speed!!}.average()
        }
    }
}