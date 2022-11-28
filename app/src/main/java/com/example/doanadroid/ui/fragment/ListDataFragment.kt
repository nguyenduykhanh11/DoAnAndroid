package com.example.doanadroid.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doanadroid.R
import com.example.doanadroid.adapter.CalendarAdapter
import com.example.doanadroid.databinding.FragmentListDataBinding
import com.example.doanadroid.extensions.Category
import com.example.doanadroid.extensions.CategoryName
import com.example.doanadroid.extensions.Title
import com.example.doanadroid.extensions.TitleName
import com.example.doanadroid.model.entity.CalendarEntity
import com.example.doanadroid.viewModel.CalendarViewModel
import com.example.doanadroid.viewModel.ObseverViewModel
import com.example.todolist.utils.Constants

class ListDataFragment : Fragment(), androidx.appcompat.widget.SearchView.OnQueryTextListener {

    private lateinit var binding: FragmentListDataBinding
    private val mCalendarAdapter: CalendarAdapter by lazy {
        CalendarAdapter { show ->
            showMenuOnlongClick(
                show
            )
        }
    }
    private var mainMenu: Menu? = null
    private val mCalendarViewModel: CalendarViewModel by lazy {
        ViewModelProvider(this).get(
            CalendarViewModel::class.java
        )
    }
    private val navGraph by lazy {
        Navigation.findNavController(
            requireActivity(),
            R.id.host_fragment
        )
    }
    private val mObsevedViewModel: ObseverViewModel by activityViewModels()
    private var checkCalendarComplete = ""
    private var statusCategory = Category.ListAll.CategoryName

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        //getDataFollowItemDrawer()
        setUpView()
        setListenerOnClickItem()
        setListenerOnLongClickItem()
        setUpListenerCheckedBox()
        setUpActionMenuItemAdapter()

    }

    private fun setUpListenerCheckedBox() {
        mCalendarAdapter.checkedItem = { calendar ->
            if (calendar.complete == Category.NoYet.CategoryName){
                mCalendarViewModel.updateCalendar(
                    CalendarEntity(calendar.id, calendar.name, calendar.day, calendar.time, calendar.category,Category.Complete.CategoryName))
//                checkLocation(check_Current_Position)

            }else{
                mCalendarViewModel.updateCalendar(
                    CalendarEntity(calendar.id, calendar.name, calendar.day, calendar.time, calendar.category, Category.NoYet.CategoryName))
//                checkLocation(check_Current_Position)
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        mainMenu = menu
        inflater.inflate(R.menu.menu_long_item, menu)
        showMenuOnlongClick(false)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search?.actionView as androidx.appcompat.widget.SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)

        super.onCreateOptionsMenu(menu, inflater)
    }

    //    set up VIsible cho menu
    private fun showMenuOnlongClick(show: Boolean) {
        if (checkCalendarComplete == Category.Complete.CategoryName) {
            mainMenu?.findItem(R.id.menu_checkbox)?.isVisible = show
            mainMenu?.findItem(R.id.menu_share)?.isVisible = show
            mainMenu?.findItem(R.id.menu_delete)?.isVisible = show
        } else if (checkCalendarComplete == Category.NoYet.CategoryName) {
            mainMenu?.findItem(R.id.menu_checkbox_complete)?.isVisible = show
            mainMenu?.findItem(R.id.menu_share)?.isVisible = show
            mainMenu?.findItem(R.id.menu_delete)?.isVisible = show
        }
    }


    private fun setListenerOnClickItem() {
        mCalendarAdapter.onClickItem = { calendar ->
            val bundel = Bundle()
            bundel.putParcelable(Constants.BUNDEL_SEND_CALENDAR, calendar)
            mObsevedViewModel.statusBottomApp.value = Constants.STATUS_UPDATE
            navGraph.navigate(R.id.updateFragment, bundel)
        }
    }

    private fun setListenerOnLongClickItem() {
        mCalendarAdapter.onLongClickItem = { calendar ->
            checkCalendarComplete = calendar.complete.toString()
//            val bundel = Bundle()
//            bundel.putParcelable(Constants.BUNDEL_SEND_CALENDAR , calendar)
//            mObsevedViewModel.statusBottomApp.value = Constants.STATUS_UPDATE
//            navGraph.navigate(R.id.updateFragment, bundel)
        }
    }

    private fun setUpView() {
        mObsevedViewModel.ActionDrawerItem.observe(viewLifecycleOwner) { item ->
            Log.d("this", "item: "+item)
            statusCategory = item
            when(item) {
                Category.ListAll.CategoryName -> {
                    with(mCalendarViewModel) {
                        readAllData.observe(viewLifecycleOwner) { calendarList ->
//                        calendarList.logByName(screen = "ListDataFragment 0")
                            mCalendarAdapter.setCalendar(ArrayList(calendarList))
                            Log.d("this", "all+++ ")
                        }
                    }
                }
                Category.Complete.CategoryName -> {
                    mCalendarViewModel.readAllDataComplete.observe(viewLifecycleOwner) { calendar ->
//                    calendar.logByName(screen = "ListDataFragment 1")
                        mCalendarAdapter.setCalendar(ArrayList(calendar))
                        Log.d("this", "Hoàn thành+++")
                    }
                }
                Category.Default.CategoryName -> {
                    Log.d("this", "Mặc định+++")
                    dataCategory(item)
                }
                Category.Individual.CategoryName -> {
                    Log.d("this", "Cá nhân+++")
                    dataCategory(item)
                }
                Category.Work.CategoryName -> {
                    Log.d("this", "Làm việc+++")
                    dataCategory(item)
                }
                Category.Shopping.CategoryName -> {
                    Log.d("this", "Mua sắm+++")
                    dataCategory(item)
                }
                else -> {

                }
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = mCalendarAdapter
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_delete -> {
                deleteListItem()
                true
            }
            R.id.menu_share -> {
                Toast.makeText(this.requireContext(), "share", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.menu_checkbox -> {
                unCheckbox()
                true
            }
            R.id.menu_checkbox_complete -> {
                selectCheckBox()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun selectCheckBox() {
        alertDialog(Title.complete.TitleName)
    }

    private fun unCheckbox() {
        alertDialog(Title.noYet.TitleName)
    }

    //    hàm hiện thông báo khi click itemMenu delete
    private fun deleteListItem() {
        alertDialog(Title.delete.TitleName)
    }

    private fun alertDialog(title: String?) {
        AlertDialog.Builder(this.requireContext()).run {
            setTitle("Bạn có chắc chắn?")
            setMessage(title)
            setPositiveButton("Vâng") { _, _ ->
                when(title){
                    Title.complete.TitleName->{
                        mCalendarAdapter.selectCheckBox()
                    }
                    Title.noYet.TitleName->{
                        mCalendarAdapter.unCheckbox()
                    }
                    Title.delete.TitleName->{
                        mCalendarAdapter.deleteSelecterItem()
                    }
                }
                showMenuOnlongClick(false)
            }
            setNegativeButton("Hủy bỏ") { _, _ -> }
            show()
        }
    }

    //    Hàm xoá các item khi click vào itemMenu delete
    private fun setUpActionMenuItemAdapter() {
        mCalendarAdapter.sendItemDelete = { listCalendar ->
            listCalendar.forEach {
                mCalendarViewModel.deleteCalendar(it)
            }
            mObsevedViewModel.selectItem(listCalendar)
            mObsevedViewModel.ActionUpdateNotification.value = Constants.StatusNotification.DELETE
        }
        mCalendarAdapter.sendItemSelect = { listCalendar ->
            listCalendar.forEach {
                mCalendarViewModel.updateCalendar(CalendarEntity(it.id, it.name, it.day, it.time, it.category, Category.Complete.CategoryName))
            }
            mObsevedViewModel.selectItem(listCalendar)
            mObsevedViewModel.ActionUpdateNotification.value = Constants.StatusNotification.COMPLETE
        }
        mCalendarAdapter.sendItemUnSelect = { listCalendar ->
            listCalendar.forEach {
                mCalendarViewModel.updateCalendar(CalendarEntity(it.id, it.name, it.day, it.time, it.category, Category.NoYet.CategoryName))
            }
            mObsevedViewModel.selectItem(listCalendar)
            mObsevedViewModel.ActionUpdateNotification.value = Constants.StatusNotification.NO_YET
        }
    }

    private fun dataCategory(category: String){
        mCalendarViewModel.readCategory(category!!).observe(viewLifecycleOwner) { calendar ->
            mCalendarAdapter.setCalendar(ArrayList(calendar))
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query!= null){
            searchData(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText!= null){
            searchData(newText)
        }
        return true
    }

    private fun searchData(query: String){
        val searchQuery ="%$query%"
        mCalendarViewModel.searchData(searchQuery).observe(this) { calendar ->
            mCalendarAdapter.setCalendar(calendar)
        }
    }
}