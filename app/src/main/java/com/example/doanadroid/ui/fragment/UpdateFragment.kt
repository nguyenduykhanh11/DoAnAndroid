package com.example.doanadroid.ui.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.example.doanadroid.R
import com.example.doanadroid.databinding.FragmentListDataBinding
import com.example.doanadroid.databinding.FragmentUpdateBinding
import com.example.doanadroid.extensions.Category
import com.example.doanadroid.extensions.CategoryName
import com.example.doanadroid.model.entity.CalendarEntity
import com.example.doanadroid.viewModel.ObseverViewModel
import com.example.todolist.utils.Constants
import java.text.SimpleDateFormat
import java.util.*

class UpdateFragment : Fragment() {
    private lateinit var binding: FragmentUpdateBinding
    private val observerViewModel: ObseverViewModel by activityViewModels()
    private var id: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewUpdate()
        setUpListenerSaveUpdate()
        setDayOrTimeEndIconClick()
    }

    private fun setDayOrTimeEndIconClick() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        with(binding){
            textLayoutDay.setEndIconOnClickListener{
                DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
                    var month2 = (i2+1).toString()
                    var day2 = i3.toString()
                    if ((i2+1)<10){
                        month2= "0"+(i2+1).toString()
                    }
                    if(i3<10){
                        day2 = "0"+i3.toString()
                    }
                    binding.txtDay.setText("" + i + "-" + month2 + "-" + day2)
                    binding.textLayoutTime.isVisible = true
                }, year, month, day).show()
            }
            textLayoutTime.setEndIconOnClickListener{
                TimePickerDialog(requireContext(), TimePickerDialog.OnTimeSetListener { timePicker, i, i2 ->
                    var hour2 = i.toString()
                    var minute2 = i2.toString()
                    if (i<10){
                        hour2 = "0"+i
                    }
                    if (i2<10){
                        minute2 = "0"+i2
                    }
                    binding.textTime.setText("$hour2:$minute2")
                }, hour, minute, true).show()
            }
        }
    }

    private fun setUpViewUpdate() {
        arguments?.getParcelable<CalendarEntity>(Constants.BUNDEL_SEND_CALENDAR).let { calendar->
            with(binding){
                id = calendar?.id
                txtMission.setText(calendar?.name)
                txtDay.setText(calendar?.day)
                textTime.setText(calendar?.time)
                autoCompleteTextView.setText(calendar?.category)
            }
        }
    }

    private fun setUpListenerSaveUpdate() {
        observerViewModel.statusBottomApp.observe(viewLifecycleOwner){
            if (it == Constants.STATUS_SAVE_UPDATE){
                observerViewModel.idMission.value = id
                observerViewModel.nameMission.value=binding.txtMission.text.toString().trim()
                observerViewModel.dayMission.value = binding.txtDay.text.toString()
                observerViewModel.timeMission.value = binding.textTime.text.toString()
                observerViewModel.categoryMission.value = binding.autoCompleteTextView.text.toString()
                observerViewModel.completeMission.value = Category.NoYet.CategoryName
            }
        }
        setValueDropDownMenu()
    }
    private fun setValueDropDownMenu() {
        val items = listOf(Category.Default.CategoryName,Category.Individual.CategoryName, Category.Work.CategoryName, Category.Shopping.CategoryName)
        (binding.tilCategory.editText as? AutoCompleteTextView)?.setAdapter(ArrayAdapter(requireContext(), R.layout.list_item, items))
    }
//
//    binding.imgAddDown.setOnClickListener {
//        val calendar = Calendar.getInstance().also {
//            it.timeInMillis
//            Log.d("this", it.timeInMillis.toString())
//            val date = Date(it.timeInMillis)
//            val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
//            Log.d("this", format.format(date).toString())
//        }
//    }


}