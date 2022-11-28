package com.example.doanadroid.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.doanadroid.model.entity.CalendarEntity
import com.example.doanadroid.model.entity.UserEntity
import com.example.todolist.utils.Constants

@Database(entities = [CalendarEntity::class, UserEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun CalendarDao(): CalendarDao
    abstract fun UserDao(): UserDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase?=null

        fun getAppDatabase(context: Context):AppDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    Constants.DATABASE_TABLE,
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}