package com.example.doanadroid.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.doanadroid.R
import com.example.doanadroid.databinding.FragmentRegisteBinding
import com.example.doanadroid.extensions.stringWithTrim
import com.example.doanadroid.model.entity.UserEntity
import com.example.doanadroid.viewModel.UserViewModel
import com.google.android.material.snackbar.Snackbar

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisteBinding
    private val mUserViewModel: UserViewModel by lazy { ViewModelProvider(this)[UserViewModel::class.java] }
    private val navGraph by lazy{ Navigation.findNavController(requireActivity(), R.id.host_login_fragment) }
    private var name = ""
    private var phone = ""
    private var address = ""
    private var brithday = ""
    private var email = ""
    private var password = ""
    private var checkEmail = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setEventRegister()
        setventSignIn()
    }

    private fun setventSignIn() {
        binding.tvOnClickSingIn.setOnClickListener {
            navGraph.popBackStack()
        }
    }

    private fun setEventRegister() {
        binding.floatBtnRegister.setOnClickListener {
            checkEmail = false
            if (checkNull()){
                mUserViewModel.insertUser(UserEntity(null, name, phone, address, brithday, email, password))
                navGraph.popBackStack()
            }
        }
    }


    private fun checkNull():Boolean{
        with(binding){
            name = txtLastName.stringWithTrim +" "+ txtFirstName.stringWithTrim
            phone = txtPhone.stringWithTrim
            address = txtAddress.stringWithTrim
            brithday = txtBrithday.stringWithTrim
            email = txtEmail.stringWithTrim
            password = txtPassword.stringWithTrim
            mUserViewModel.readUser.observe(viewLifecycleOwner){ user->
                user.forEach{ itemUser->
                    if (txtEmail.stringWithTrim == itemUser.email){
                        Log.d("this", itemUser.email+"//////"+txtEmail.stringWithTrim)
                        checkEmail = true
                    }
                }
            }
            if (txtFirstName.stringWithTrim == "" || txtLastName.stringWithTrim == "" || phone == "" || txtAddress.stringWithTrim == "" || txtBrithday.stringWithTrim == "" || email == "" || password == "" || txtEndPassword.stringWithTrim== ""){
                Snackbar.make(binding.lauoutContainer, "Bạn chưa nhập đủ thông tin", Snackbar.LENGTH_SHORT).show()
                return false
            }
            else{
                if (password != txtEndPassword.stringWithTrim){
                    Snackbar.make(binding.lauoutContainer, "Password không trùng", Snackbar.LENGTH_SHORT).show()
                    return false
                }else if(checkEmail){
                    Snackbar.make(binding.lauoutContainer, "Email đã tồn tại", Snackbar.LENGTH_SHORT).show()
                    return false
                }
            }
        }
        return true
    }
}