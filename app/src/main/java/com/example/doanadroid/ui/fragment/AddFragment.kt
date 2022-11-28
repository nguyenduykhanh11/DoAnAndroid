package com.example.doanadroid.ui.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.example.doanadroid.R
import com.example.doanadroid.databinding.FragmentAddBinding
import com.example.doanadroid.extensions.Category
import com.example.doanadroid.extensions.CategoryName
import com.example.doanadroid.viewModel.ObseverViewModel
import com.example.todolist.utils.Constants
import java.util.*

class AddFragment : Fragment() {
    private lateinit var binding: FragmentAddBinding
    private val observerViewModel: ObseverViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setValueDropDownMenu()
        setDayOrTimeEndIconClick()
        observerViewModel.statusBottomApp.observe(viewLifecycleOwner){
            if (it == Constants.STATUS_SAVE_ADD){
                observerViewModel.nameMission.value=binding.txtMission.text.toString()
                observerViewModel.dayMission.value = binding.txtDay.text.toString()
                observerViewModel.timeMission.value = binding.textTime.text.toString()
                observerViewModel.categoryMission.value = binding.autoCompleteTextView.text.toString()
                observerViewModel.completeMission.value = Category.NoYet.CategoryName
            }
        }
    }


//    hàm hiện dialog day or time
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

    //    hàm set View cho text fields droo down menu
    private fun setValueDropDownMenu() {
        val items = listOf(Category.Default.CategoryName,Category.Individual.CategoryName, Category.Work.CategoryName, Category.Shopping.CategoryName)
        (binding.tilCategory.editText as? AutoCompleteTextView)?.setAdapter(ArrayAdapter(requireContext(), R.layout.list_item, items))
    }


}