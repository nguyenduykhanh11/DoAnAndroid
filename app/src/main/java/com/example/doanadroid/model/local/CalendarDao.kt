package com.example.doanadroid.model.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.doanadroid.model.entity.CalendarEntity
import com.example.todolist.utils.Constants

@Dao
interface CalendarDao{
    @Query("SELECT * FROM ${Constants.CALENDAR_TABLE_NAME} WHERE complete = '${Constants.NO_YET}'ORDER BY day DESC,id ASC")
    fun readAllData(): LiveData<List<CalendarEntity>>

    @Query("SELECT * FROM ${Constants.CALENDAR_TABLE_NAME} WHERE complete = '${Constants.NO_YET}'AND categoty =:category ORDER BY day DESC,id ASC")
    fun readCategory(category: String): LiveData<List<CalendarEntity>>

    @Query("SELECT * FROM ${Constants.CALENDAR_TABLE_NAME} WHERE complete = '${Constants.COMPLETE}'ORDER BY day DESC,id ASC")
    fun readAllDataComplete(): LiveData<List<CalendarEntity>>

    @Query("SELECT * FROM ${Constants.CALENDAR_TABLE_NAME} WHERE name LIKE :searchQuery")
    fun searchData(searchQuery: String): LiveData<List<CalendarEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(calendar: CalendarEntity)

    @Delete
    suspend fun delete(calendar: CalendarEntity?)

    @Update
    suspend fun update(calendar: CalendarEntity?)
}