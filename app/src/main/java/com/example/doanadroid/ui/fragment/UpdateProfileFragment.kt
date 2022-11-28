package com.example.doanadroid.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.doanadroid.R
import com.example.doanadroid.databinding.FragmentUpdateProfileBinding
import com.example.doanadroid.extensions.stringWithTrim
import com.example.doanadroid.model.entity.UserEntity
import com.example.doanadroid.ui.activities.MainActivity
import com.example.doanadroid.viewModel.UserViewModel
import com.google.android.material.snackbar.Snackbar

class UpdateProfileFragment : Fragment() {
    private lateinit var binding: FragmentUpdateProfileBinding
    private val mUserViewModel: UserViewModel by lazy { ViewModelProvider(this)[UserViewModel::class.java] }
    private val navGraph by lazy{ Navigation.findNavController(requireActivity(), R.id.host_profile_fragment) }
    private var name = ""
    private var phone = ""
    private var address = ""
    private var brithday = ""
    private var email = ""
    private var password = ""
    private var checkPassword = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = arguments?.getParcelable<UserEntity>("upadte_user")
        Log.d("this", user.toString())
        setUpView(user)
        setUpEventUpdate(user)
    }

    private fun setUpEventUpdate(user: UserEntity?) {
        binding.floatBtnUpdate.setOnClickListener {
            checkPassword = false
            if(checkNull(user)){
                mUserViewModel.updateUser(UserEntity(user?.id, name, phone, address, brithday, email, password))
                Snackbar.make(binding.layoutUpdateContainer, "Thay đổi thông tin thành công", Snackbar.LENGTH_SHORT).show()
                var intent = Intent(activity, MainActivity::class.java)
                activity?.startActivity(intent)
                activity?.finish()
            }
        }
    }
    private fun checkNull(user: UserEntity?):Boolean{
        with(binding){
            name = txtFullName.stringWithTrim
            phone = txtPhone.stringWithTrim
            address = txtAddress.stringWithTrim
            brithday = txtBrithday.stringWithTrim
            email = txtEmail.stringWithTrim
            password = txtPassword.stringWithTrim
            if (txtPasswordOld.stringWithTrim != user?.password){
                checkPassword = true
            }

            if (txtFullName.stringWithTrim == "" || phone == "" || txtAddress.stringWithTrim == "" || txtBrithday.stringWithTrim == "" || email == "" || password == "" || txtEndPassword.stringWithTrim== ""){
                Snackbar.make(binding.layoutUpdateContainer, "Bạn chưa nhập đủ thông tin", Snackbar.LENGTH_SHORT).show()
                return false
            }
            else{
                if (password != txtEndPassword.stringWithTrim){
                    Snackbar.make(binding.layoutUpdateContainer, "Password mới không trùng", Snackbar.LENGTH_SHORT).show()
                    return false
                }else if(checkPassword){
                    Snackbar.make(binding.layoutUpdateContainer, "Password không dúng", Snackbar.LENGTH_SHORT).show()
                    return false
                }
            }
        }
        return true
    }

    private fun setUpView(user: UserEntity?) {
        with(binding){
            txtFullName.setText(user?.name)
            txtPhone.setText(user?.phone)
            txtAddress.setText(user?.address)
            txtBrithday.setText(user?.brithday)
            txtEmail.setText(user?.email)
        }
    }

}