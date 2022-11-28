package com.example.doanadroid.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.doanadroid.model.entity.CalendarEntity
import com.example.doanadroid.model.local.AppDatabase
import com.example.doanadroid.model.repository.CalendarRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CalendarViewModel(application: Application): AndroidViewModel(application) {
    val readAllData: LiveData<List<CalendarEntity>>
    val readAllDataComplete: LiveData<List<CalendarEntity>>
    private val repository: CalendarRepository

    init {
        val calendarDao = AppDatabase.getAppDatabase(application).CalendarDao()
        repository = CalendarRepository(calendarDao)
        readAllData = repository.readAllData
        readAllDataComplete = repository.readAllDataComplete

    }

    fun readCategory(category: String): LiveData<List<CalendarEntity>> {
        return repository.readCategory(category)
    }

    fun searchData(searchQuery: String): LiveData<List<CalendarEntity>>{
        return repository.searchData(searchQuery )
    }

    fun insertCalendar(calendarEntity: CalendarEntity){
        viewModelScope.launch(Dispatchers.IO){
            repository.insertCalendar(calendarEntity)
        }
    }
    fun updateCalendar(calendarEntity: CalendarEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateCalendar(calendarEntity)
        }
    }
    fun deleteCalendar(calendarEntity: CalendarEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCalendar(calendarEntity)
        }
    }
}