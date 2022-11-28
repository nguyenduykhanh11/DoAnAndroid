package com.example.doanadroid.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.doanadroid.model.entity.CalendarEntity
import com.example.doanadroid.model.entity.UserEntity

class ObseverViewModel:ViewModel() {
//    Định vị BottomAppbar
    var statusBottomApp = MutableLiveData<String>()

//    Lưu dữ liệu Calendar
    var idMission = MutableLiveData<Int>()
    var nameMission = MutableLiveData<String>()
    var dayMission = MutableLiveData<String>()
    var timeMission = MutableLiveData<String>()
    var categoryMission = MutableLiveData<String>()
    var completeMission = MutableLiveData<String>()

//    Listener click Item drawer layout
    var ActionDrawerItem = MutableLiveData<String>()

//    Trạng thái Loggin
    var ActionLogin = MutableLiveData<String>()

//    Trạng thái Đã xữ lý sự kiện Notification
    var ActionUpdateNotification = MutableLiveData<String>()

//    list Item cancel Notification
    private var mutableCalendar: MutableLiveData<List<CalendarEntity>> = MutableLiveData(listOf())
    val selectedItem: LiveData<List<CalendarEntity>> = mutableCalendar
    fun selectItem(list: List<CalendarEntity>) {
        mutableCalendar.postValue(list)
    }

    //    Save_pref
    var SaveProfile = MutableLiveData<List<UserEntity>>()
}