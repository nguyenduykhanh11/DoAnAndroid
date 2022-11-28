package com.example.doanadroid.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.doanadroid.model.entity.CalendarEntity
import com.example.doanadroid.model.entity.UserEntity
import com.example.doanadroid.model.local.AppDatabase
import com.example.doanadroid.model.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application): AndroidViewModel(application) {
    private val repository : UserRepository
    val readUser: LiveData<List<UserEntity>>
    init {
        val userDao = AppDatabase.getAppDatabase(application).UserDao()
        repository = UserRepository(userDao)
        readUser = repository.readAllData
    }

    fun readSignIn(email: String, password: String): LiveData<List<UserEntity>> {
        return repository.readSignIn(email, password)
    }

    fun insertUser(userEntity: UserEntity){
        viewModelScope.launch(Dispatchers.IO){
            repository.insertUser(userEntity)
        }
    }
    fun updateUser(user: UserEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(user)
        }
    }
}