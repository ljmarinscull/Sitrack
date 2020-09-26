package com.example.trackertest.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trackertest.data.repository.IRecordRepo

class MainViewModelFactory(private val useCase: IRecordRepo) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass : Class<T>) : T {
        return modelClass.getConstructor(IRecordRepo::class.java).newInstance(useCase)
    }

}