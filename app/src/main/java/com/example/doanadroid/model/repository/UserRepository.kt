package com.example.doanadroid.model.repository

import androidx.lifecycle.LiveData
import com.example.doanadroid.model.entity.CalendarEntity
import com.example.doanadroid.model.entity.UserEntity
import com.example.doanadroid.model.local.UserDao

class UserRepository(private val userDao: UserDao) {

    val readAllData: LiveData<List<UserEntity>> = userDao.readUser()

    fun readSignIn(email :String, password: String): LiveData<List<UserEntity>>{
        return userDao.setSignIn(email, password)
    }

    suspend fun insertUser(userEntity: UserEntity){
        userDao.SignUp(userEntity)
    }

    suspend fun update(user: UserEntity){
        userDao.update(user)
    }
}