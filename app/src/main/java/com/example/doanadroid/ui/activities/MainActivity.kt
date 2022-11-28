package com.example.doanadroid.ui.activities

import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.doanadroid.*
import com.example.doanadroid.databinding.ActivityMainBinding
import com.example.doanadroid.extensions.Category
import com.example.doanadroid.extensions.CategoryName
import com.example.doanadroid.model.entity.CalendarEntity
import com.example.doanadroid.model.entity.UserEntity
import com.example.doanadroid.viewModel.CalendarViewModel
import com.example.doanadroid.viewModel.ObseverViewModel
import com.example.doanadroid.viewModel.UserViewModel
import com.example.todolist.utils.Constants
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

//val Any?.Unit: Unit
//    get() = Unit
//
//fun View.visiableIf(condition: Boolean, default: Int = View.GONE) {
//    if(condition) {
//        visiable()
//    } else {
//        this.visibility = default
//    }
//}
//fun View.visiable() {
//    this.visibility = View.VISIBLE
//}
//fun View.gone() {
//    this.visibility = View.GONE
//}

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var binding: ActivityMainBinding
    private val mCalendarViewModel: CalendarViewModel by lazy {
        ViewModelProvider(this).get(
            CalendarViewModel::class.java
        )
    }
    private val mUserViewModel: UserViewModel by lazy { ViewModelProvider(this).get(UserViewModel::class.java) }
    private val navGraph by lazy { Navigation.findNavController(this, R.id.host_fragment) }
    private val mObsevedViewModel: ObseverViewModel by viewModels()
    private var alarmManager: AlarmManager? = null
    private var checkStatusBottomNavigation = 0
    private var checkEnbleMission = ""
    private var id: Int? = null
    private var name = ""
    private var day = ""
    private var time = ""
    private var category = ""
    private var complete = ""
    private var statusSave = ""

    private var proFile: UserEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
        createNotificationChanel()
        setUpViewNavigationDrawer()
        setUpNavigationBackStack()
        setUpClickFloatBottomAddOrSave()
        setUponBackPresseDispatcher()
        setUpdateItemMission()
        setDataHeadLayout()
        obseverNotification()

    }

    private fun obseverNotification() {

//        binding.toolbar.visiableIf(a == true)
        mObsevedViewModel.ActionUpdateNotification.observe(this@MainActivity) { actionItemMenu ->
            when (actionItemMenu) {
                Constants.StatusNotification.DELETE -> {
                    deleteItem()
                }
                Constants.StatusNotification.COMPLETE -> {
                    completeItem()
                }
                Constants.StatusNotification.NO_YET -> {
                    noYetItem()
                }
            }
        }
    }

    private fun noYetItem() {
        mObsevedViewModel.selectedItem.observe(this@MainActivity) { calendarList ->
            calendarList.forEach { idCalendar ->
                pushNotification(idCalendar)
            }
        }
        mObsevedViewModel.ActionUpdateNotification.value = ""
    }

    private fun completeItem() {
        mObsevedViewModel.selectedItem.observe(this@MainActivity) { calendarList ->
            calendarList.forEach { idCalendar ->
                cancelNotification(idCalendar.id!!)
            }
        }
        mObsevedViewModel.ActionUpdateNotification.value = ""
    }

    private fun deleteItem() {
        mObsevedViewModel.selectedItem.observe(this@MainActivity) { calendarList ->
            calendarList.forEach { idCalendar ->
                cancelNotification(idCalendar.id!!)
            }
        }
        mObsevedViewModel.ActionUpdateNotification.value = ""
    }

    private fun createNotificationChanel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "Notif Chanel"
            val desc = "A Description of the Chanel"
            val importence = NotificationManager.IMPORTANCE_HIGH
            val chanel = NotificationChannel(chanelID, name, importence)
            chanel.description = desc
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(chanel)
        }
    }

    private fun setDataHeadLayout() {
        val SharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val idSave = SharedPref.getInt("Save_pref", -1)
        if (idSave != -1) {
            showActionAccont(true)
            mUserViewModel.readUser.observe(this) { user ->
                user.forEach { item ->
                    if (item.id == idSave) {
                        proFile = item
                        setDataForDrawerLayout(item.name, item.brithday)
                    }
                }
            }
        } else {
            showActionAccont(false)
        }
    }

    private fun showActionAccont(boolean: Boolean) {
        val navigationView: NavigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.menu.apply {
            findItem(R.id.sub_logout).isVisible = boolean
            findItem(R.id.sub_profile).isVisible = boolean
            findItem(R.id.sub_login).isVisible = !boolean
        }

    }

    private fun setDataForDrawerLayout(name: String?, brithday: String?) {
        val navigationView = (findViewById<View>(R.id.nav_view) as NavigationView)
        navigationView.setNavigationItemSelectedListener(this)
        val header = navigationView.getHeaderView(0)
        (header.findViewById<View>(R.id.tv_title_name) as TextView).text = name
        (header.findViewById<View>(R.id.tv_title_email) as TextView).text = brithday
    }

    private fun arrowBack() {
        mObsevedViewModel.nameMission.observe(this) { checkEnbleMission = it }
        if (checkEnbleMission.trim() != "") {
            AlertDialog.Builder(this).apply {
                setTitle("Bạn có chắc chắn?")
                setMessage("Thoát ra khỏi đây?")
                setPositiveButton("Vâng", DialogInterface.OnClickListener { dialogInterface, i ->
                    mObsevedViewModel.nameMission.value = ""
                    dialogInterface.dismiss()
                    eventSettingBackStack()
                })
                setNegativeButton("Hủy bỏ", DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                })
                create()
                show()
            }
        } else {
            eventSettingBackStack()
        }
    }

    //    hàm xữ lý khi người dùng back ở máy
    private fun setUponBackPresseDispatcher() {
        onBackPressedDispatcher.addCallback(this) {
            when (checkStatusBottomNavigation) {
                Constants.Action.ACTION_ADD_SAVE -> arrowBack()
                Constants.Action.ACTION_UPDATE_SAVE -> arrowBack()
                else -> finish()
            }
        }
    }

    //    hàm show view Navigation Drawer
    private fun setUpViewNavigationDrawer() {
        binding.navView.setNavigationItemSelectedListener(this)
        val drawerToggle = ActionBarDrawerToggle(
            this,
            binding.layoutDrawer,
            binding.bottomAppBar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        binding.layoutDrawer.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        mObsevedViewModel.SaveProfile.observe(this) { userLogin ->
            setDataForDrawerLayout(userLogin[0].name, userLogin[0].brithday)
        }
    }

    //        buttom chuyển đên fragment add missuon
    private fun setUpClickFloatBottomAddOrSave() {
        binding.floatingActionButton.setOnClickListener {
            handleViewBottomAppbar()
        }
    }

    //    hàm check và xữ lý view cho bottomAppbar and Navigation Icon Toolbar
    private fun handleViewBottomAppbar() {
        when (checkStatusBottomNavigation) {
            Constants.Action.ACTION_ADD -> {
                eventSettingAddMission()
            }
            Constants.Action.ACTION_ADD_SAVE -> {
                setUpSaveAddOrSaveUpdate(Constants.STATUS_SAVE_ADD)
            }
            Constants.Action.ACTION_UPDATE_SAVE -> {
                setUpSaveAddOrSaveUpdate(Constants.STATUS_SAVE_UPDATE)
            }
        }
    }

    //    Hàm xữ lý chuyển màn đến fragment update
    private fun setUpdateItemMission() {
        mObsevedViewModel.statusBottomApp.observe(this) {
            if (it == Constants.STATUS_UPDATE) {
                checkStatusBottomNavigation = Constants.Action.ACTION_UPDATE_SAVE
                setUpViewAction()
//                binding.apply {
//                    bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
//                    floatingActionButton.setImageDrawable(resources.getDrawable(R.drawable.ic_save))
//                    toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_back)
//                    bottomAppBar.navigationIcon = null
//                }
            }
        }
    }

    private fun setUpViewAction(){
        binding.apply {
            bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
            floatingActionButton.setImageDrawable(resources.getDrawable(R.drawable.ic_save))
            toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_back)
            bottomAppBar.navigationIcon = null
        }
    }

    private fun setUpSaveAddOrSaveUpdate(save: String) {
        mObsevedViewModel.statusBottomApp.value = save
        mObsevedViewModel.nameMission.observe(this@MainActivity) {
            checkEnbleMission = it
        }
        if (String.format(checkEnbleMission) != "") {
            Snackbar.make(binding.layoutDrawer, "Thêm Nhiệm Vụ Thành Công", Snackbar.LENGTH_LONG)
                .setDuration(10000)
                .setAnchorView(binding.floatingActionButton)
                .show()
            eventSettingBackStack()
            setUpSaveData()
        } else {
            Snackbar.make(binding.layoutDrawer, "Bạn Chưa nhập Nhiệm vụ", Snackbar.LENGTH_LONG)
                .setDuration(10000)
                .setAnchorView(binding.floatingActionButton)
                .show()
        }
    }

    private fun setUpSaveData() {
        with(mObsevedViewModel) {
            idMission.observe(this@MainActivity) {
                id = it
            }
            nameMission.observe(this@MainActivity) {
                name = it
            }
            dayMission.observe(this@MainActivity) {
                day = it
            }
            timeMission.observe(this@MainActivity) {
                time = it
            }
            categoryMission.observe(this@MainActivity) {
                category = it
            }
            completeMission.observe(this@MainActivity) {
                complete = it
            }
        }
        mObsevedViewModel.statusBottomApp.observe(this) {
            statusSave = it
        }
        when (statusSave) {
            Constants.STATUS_SAVE_ADD -> {
                mCalendarViewModel.insertCalendar(
                    CalendarEntity(
                        null,
                        name,
                        day,
                        time,
                        category,
                        complete
                    )
                )
                scheduleNotification()
            }
            Constants.STATUS_SAVE_UPDATE -> {
                mCalendarViewModel.updateCalendar(
                    CalendarEntity(
                        id,
                        name,
                        day,
                        time,
                        category,
                        complete
                    )
                )
                changeNotification(id)
            }
        }
    }

    private fun changeNotification(id: Int?) {
        mCalendarViewModel.readAllData.observe(this@MainActivity) { calendarList ->
            calendarList.forEach { item ->
                if (item.id == id) {
                    pushNotification(item)
                }
            }
        }
    }

    fun scheduleNotification() {
        mCalendarViewModel.readAllData.observe(this@MainActivity) { calendarList ->
            calendarList.forEach { item ->
                if (item.name.toString() == name && item.day.toString() == day && item.time.toString() == time && item.category.toString() == category && item.complete.toString() == complete) {
                    if (item.day.toString() != "" || item.time.toString() != "") pushNotification(
                        item
                    )
                }
            }
        }
    }

    private fun cancelNotification(id: Int) {
        val intent = Intent(applicationContext, NotificationBroadcast::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            id,
            intent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        if (alarmManager == null) {
            alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        }
        alarmManager!!.cancel(pendingIntent)
    }

    //    hàm xữ lý push Notification
    private fun pushNotification(calendar: CalendarEntity) {
        val intent = Intent(applicationContext, NotificationBroadcast::class.java)
        var time: Long = -1
        if (calendar.day.toString().trim() != "") {
            time = getTime(calendar.day.toString(), calendar.time.toString())
        }
        intent.putExtra(notifiExtra, calendar.id)
        intent.putExtra(titleExtra, calendar.category)
        intent.putExtra(messageExtra, calendar.name)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (time.toInt() != -1) {
                if (alarmManager == null) {
                    alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                }
                val pendingIntent = PendingIntent.getBroadcast(
                    applicationContext,
                    calendar.id!!,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT
                )
                alarmManager!!.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    time,
                    pendingIntent
                )
            }
        }
    }

    //    Hàm set thời gian để push notification
    private fun getTime(dateDay: String, time: String): Long {
        val str = dateDay + " " + time
        val calendar = Calendar.getInstance()
        Log.d("this", dateDay + " " + time)
        if (dateDay.trim() != "" && time.trim() == "") {
            val day = dateDay + " 00:00"
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val date: Date = sdf.parse(day)!!
            val millis: Long = date.time
            if (calendar.timeInMillis >= millis) return -1
            return millis
        }
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val date: Date = sdf.parse(str)!!
        val millis: Long = date.time
        if (calendar.timeInMillis >= millis) return -1
        return millis
    }

    //    hàm sử lý sự kiện backStack
    private fun eventSettingBackStack() {
        checkStatusBottomNavigation = Constants.Action.ACTION_ADD
        navGraph.popBackStack()
        binding.apply {
            toolbar.navigationIcon = null
            bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
            floatingActionButton.setImageDrawable(resources.getDrawable(R.drawable.ic_add))
            bottomAppBar.navigationIcon = resources.getDrawable(R.drawable.ic_menu)
        }
    }

    //    hàm sử lý Click nút add
    private fun eventSettingAddMission() {
        navGraph.navigate(R.id.addFragment)
        checkStatusBottomNavigation = Constants.Action.ACTION_ADD_SAVE
        mObsevedViewModel.statusBottomApp.value = Constants.STATUS_ADD
        setUpViewAction()
    }

    //    hàm xữ lý sự kiện navigationToolbar click
    private fun setUpNavigationBackStack() {
        binding.toolbar.setNavigationOnClickListener {
            when (checkStatusBottomNavigation) {
                1 -> mObsevedViewModel.statusBottomApp.value = Constants.STATUS_SAVE_ADD
                2 -> mObsevedViewModel.statusBottomApp.value = Constants.STATUS_SAVE_UPDATE
                else -> {}
            }
            arrowBack()
        }
    }


    //    setUpView all fragment in Navigation
    private fun setUpView() {
        setSupportActionBar(binding.toolbar)
        setUpListenerClickItemDrawer(R.string.list_all, R.drawable.ic_home)
        setUpEventClickItemDrawer(Category.ListAll.CategoryName)
        navGraph.navigate(R.id.listDataFragment)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sub_list_all -> {
                setUpListenerClickItemDrawer(R.string.list_all, R.drawable.ic_home)
                setUpEventClickItemDrawer(Category.ListAll.CategoryName)
                navGraph.navigate(R.id.listDataFragment)
                true
            }
            R.id.sub_default -> {
                setUpListenerClickItemDrawer(R.string.defaul_t, R.drawable.ic_category_list)
                setUpEventClickItemDrawer(Category.Default.CategoryName)
                navGraph.navigate(R.id.listDataFragment)
                true
            }
            R.id.sub_individual -> {
                setUpListenerClickItemDrawer(R.string.individual, R.drawable.ic_category_list)
                setUpEventClickItemDrawer(Category.Individual.CategoryName)
                navGraph.navigate(R.id.listDataFragment)
                true
            }
            R.id.sub_work -> {
                setUpListenerClickItemDrawer(R.string.work, R.drawable.ic_category_list)
                setUpEventClickItemDrawer(Category.Work.CategoryName)
                navGraph.navigate(R.id.listDataFragment)
                true
            }
            R.id.sub_shopping -> {
                setUpListenerClickItemDrawer(R.string.shopping, R.drawable.ic_category_list)
                setUpEventClickItemDrawer(Category.Shopping.CategoryName)
                navGraph.navigate(R.id.listDataFragment)
                true
            }
            R.id.sub_complete -> {
                setUpListenerClickItemDrawer(R.string.complete, R.drawable.ic_complete)
                setUpEventClickItemDrawer(Category.Complete.CategoryName)
                navGraph.navigate(R.id.listDataFragment)
                true
            }
            R.id.sub_login -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            R.id.sub_logout -> {
                val sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE)
                sharedPref.edit().clear().apply()
                finish()
                startActivity(intent)
                setDataForDrawerLayout("", "")
                true
            }
            R.id.sub_profile -> {
                val intent = Intent(this, ProfileActivity::class.java)
                intent.putExtra("ProFile", proFile)
                Log.d("this", proFile.toString())
                startActivity(intent)
                finish()
                true
            }
            else -> false
        }
    }

    private fun setUpEventClickItemDrawer(actionItem: String) {
        mObsevedViewModel.ActionDrawerItem.value = actionItem
    }

    private fun setUpListenerClickItemDrawer(titlee: Int, icon: Int) {
        title = " " + resources.getString(titlee)
        binding.toolbar.logo = resources.getDrawable(icon)
        if (binding.layoutDrawer.isDrawerOpen(GravityCompat.START)) {
            binding.layoutDrawer.closeDrawer(GravityCompat.START)
        }
    }
}