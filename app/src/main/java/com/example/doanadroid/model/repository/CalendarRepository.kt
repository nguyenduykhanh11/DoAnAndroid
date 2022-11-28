package com.example.doanadroid.model.repository

import androidx.lifecycle.LiveData
import com.example.doanadroid.model.entity.CalendarEntity
import com.example.doanadroid.model.local.CalendarDao

class CalendarRepository(private val calendarDao: CalendarDao) {
    val readAllData: LiveData<List<CalendarEntity>> = calendarDao.readAllData()
    val readAllDataComplete: LiveData<List<CalendarEntity>> = calendarDao.readAllDataComplete()

    fun readCategory(category :String):LiveData<List<CalendarEntity>>{
       return calendarDao.readCategory(category)
    }
    fun searchData(searchQuery: String): LiveData<List<CalendarEntity>>{
        return calendarDao.searchData(searchQuery)
    }

    suspend fun insertCalendar(calendarEntity: CalendarEntity){
        calendarDao.insert(calendarEntity)
    }

    suspend fun updateCalendar(calendarEntity: CalendarEntity){
        calendarDao.update(calendarEntity)
    }
    suspend fun deleteCalendar(calendarEntity: CalendarEntity){
        calendarDao.delete(calendarEntity)
    }
}