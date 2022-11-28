package com.example.doanadroid.model.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.doanadroid.model.entity.CalendarEntity
import com.example.doanadroid.model.entity.UserEntity
import com.example.todolist.utils.Constants

@Dao
interface UserDao {
    @Query("SELECT * FROM USER_TABLE_NAME WHERE email LIKE :email AND password LIKE :password")
    fun setSignIn(email: String, password: String): LiveData<List<UserEntity>>

    @Query("SELECT * FROM USER_TABLE_NAME ")
    fun readUser(): LiveData<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun SignUp(user: UserEntity)

    @Update
    suspend fun update(user: UserEntity)
}