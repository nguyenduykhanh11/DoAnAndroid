package com.example.doanadroid.adapter

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.doanadroid.R
import com.example.doanadroid.databinding.ListDataItemBinding
import com.example.doanadroid.model.entity.CalendarEntity
import com.example.todolist.utils.Constants
import java.text.SimpleDateFormat
import java.util.*

class CalendarAdapter(private val showMenuOnlongClick: (Boolean) -> Unit): RecyclerView.Adapter<CalendarAdapter.ViewHolder>(){
    lateinit var onClickItem : ((CalendarEntity) ->Unit)
    lateinit var onLongClickItem : ((CalendarEntity) ->Unit)
    lateinit var checkedItem : ((CalendarEntity) ->Unit)
    lateinit var sendItemDelete : ((List<CalendarEntity>) ->Unit)
    lateinit var sendItemSelect : ((List<CalendarEntity>) ->Unit)
    lateinit var sendItemUnSelect : ((List<CalendarEntity>) ->Unit)
    var calendarList = mutableListOf<CalendarEntity>()
    private var listOfLongItem = mutableListOf<CalendarEntity>()

    inner class ViewHolder(var binding: ListDataItemBinding):RecyclerView.ViewHolder(binding.root)


    fun setCalendar(newList: List<CalendarEntity>){
        this.calendarList.clear()
        this.calendarList = mutableListOf(*newList.toTypedArray())
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ListDataItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        setUpViewItem(holder, position)
        setUpViewTime(holder, position)
        checkOnclickItem(holder, position)
        setOnLongClickItem(holder, position)
        setUpListenerCheckBoxItem(holder, position)
    }

    private fun setUpViewTime(holder: CalendarAdapter.ViewHolder, position: Int) {
        if (calendarList[position].day != "" && calendarList[position].time != ""){
            val daytime = calendarList[position].day+" "+calendarList[position].time
            setColorTextDayTime(daytime ,holder)
        }
        else if(calendarList[position].day != "" && calendarList[position].time == "") {
            val dayTime = calendarList[position].day+" 00:00"
            setColorTextDayTime(dayTime ,holder)
        }
    }
    @SuppressLint("ResourceAsColor")
    private fun setColorTextDayTime(day: String, holder: ViewHolder) {
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val date: Date = sdf.parse(day)!!
        if(date < calendar.time){
            holder.binding.tvDayTime.setTextColor(Color.parseColor("#D33F36"))
        }
        else {
            holder.binding.tvDayTime.setTextColor(Color.parseColor("#79FA42"))
        }
    }

    private fun setUpViewItem(holder: CalendarAdapter.ViewHolder, position: Int) {
        with(holder.binding){
            tvMission.text = calendarList[position].name
            tvCategory.text = calendarList[position].category
            if(calendarList[position].day!!.trim() == ""){
                tvDayTime.isVisible = false
            }else{
                tvDayTime.isVisible = true
                tvDayTime.text = calendarList[position].day +" "+calendarList[position].time
            }
            if (calendarList[position].complete == Constants.COMPLETE){
                chkMission.isChecked = true
            }
            else{
                chkMission.isChecked = false
            }
            holder.binding.recyclerViewBackground.setBackgroundResource(R.drawable.bg_color_nomal)
        }
    }

    private fun setUpListenerCheckBoxItem(holder: CalendarAdapter.ViewHolder, position: Int) {
        holder.binding.chkMission.setOnClickListener{
            holder.binding.chkMission.isChecked = false
            checkedItem.invoke(calendarList[position])
        }
    }

    private fun checkOnclickItem(holder: CalendarAdapter.ViewHolder, position: Int) {
        holder.binding.cardClickChange.setOnClickListener {
            if (listOfLongItem.size == 0) {
                onClickItem.invoke(calendarList[position])
            }
            else{
                checkedItemLongClick(holder, position)
            }
        }
    }

    private fun checkedItemLongClick(holder: CalendarAdapter.ViewHolder, position: Int) {
        if (listOfLongItem.contains(calendarList[position])) {
            listOfLongItem.remove(calendarList[position])
            holder.binding.recyclerViewBackground.setBackgroundResource(R.drawable.bg_color_nomal)
            if(listOfLongItem.size == 0){
                showMenuOnlongClick(false)
            }

        } else {
            holder.binding.recyclerViewBackground.setBackgroundResource(R.drawable.bg_color_long_click_item)
            listOfLongItem.add(calendarList[position])
        }
    }

    private fun setOnLongClickItem(holder: CalendarAdapter.ViewHolder, position: Int) {
        holder.binding.cardClickChange.setOnLongClickListener{
            onLongClickItem.invoke(calendarList[position])
            holder.binding.recyclerViewBackground.setBackgroundResource(R.drawable.bg_color_long_click_item)
            listOfLongItem.add(calendarList[position])
            showMenuOnlongClick(true)
            true
        }
    }

    override fun getItemCount(): Int =  calendarList.size

    fun deleteSelecterItem() {
        if (listOfLongItem.isNotEmpty()) {
            sendItemDelete.invoke(listOfLongItem)
            listOfLongItem.clear()
        }
    }

    fun selectCheckBox() {
        if (listOfLongItem.isNotEmpty()) {
            sendItemSelect.invoke(listOfLongItem)
            listOfLongItem.clear()
        }
    }

    fun unCheckbox() {
        if (listOfLongItem.isNotEmpty()) {
            sendItemUnSelect.invoke(listOfLongItem)
            listOfLongItem.clear()
        }
    }
}