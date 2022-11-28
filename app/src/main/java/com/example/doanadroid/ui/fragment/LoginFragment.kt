package com.example.doanadroid.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.doanadroid.R
import com.example.doanadroid.databinding.FragmentLoginBinding
import com.example.doanadroid.extensions.stringVal
import com.example.doanadroid.extensions.stringWithTrim
import com.example.doanadroid.model.entity.logByName
import com.example.doanadroid.viewModel.ObseverViewModel
import com.example.doanadroid.viewModel.UserViewModel
import com.example.todolist.utils.Constants
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val navGraph by lazy{ Navigation.findNavController(requireActivity(), R.id.host_login_fragment) }
    private val mUserViewModel: UserViewModel by lazy { ViewModelProvider(this).get(UserViewModel::class.java) }
    private val mObsevedViewModel: ObseverViewModel by activityViewModels()
    private var email = ""
    private var password = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpEventSignUp()
        setUpEvenTLogin()
    }

    private fun setUpEvenTLogin() {
        binding.floatBottomLogin.setOnClickListener {
            if (checkLogin()){
                Log.d("this", email + password)
                mUserViewModel.readSignIn(email, password).observe(viewLifecycleOwner){ user->
                    try {
                        if (user[0]==null)
                        else{
                            if (user[0].email == email && user[0].password == password) {
                                Snackbar.make(binding.layoutLoginContainer, "Đăng nhập thành công", Snackbar.LENGTH_SHORT).show()
                                mObsevedViewModel.ActionLogin.value = Constants.ACTION_LOGIN
                                mObsevedViewModel.SaveProfile.value = user
                            }
                        }
                    }catch (e: Exception){
                        Snackbar.make(binding.layoutLoginContainer, "Email hoặc Password không đúng", Snackbar.LENGTH_SHORT).show()
                        mObsevedViewModel.ActionLogin.value = Constants.ACTION_UN_LOGIN
                    }
                }
            }
        }
    }

    private fun checkLogin(): Boolean{
        with(binding) {
            email = txtLoginEmail.stringWithTrim
            password = txtLoginPassword.stringVal
        }
        return if (email.isEmpty() || password.isEmpty()){
            Snackbar.make(binding.layoutLoginContainer, "Bạn chưa nhập đủ thông tin", Snackbar.LENGTH_SHORT).show()
            false
        } else{
            mUserViewModel.readSignIn(email, password).observe(viewLifecycleOwner) { user ->
                if (user == null ){
                    Snackbar.make(binding.layoutLoginContainer, "Emai hoặc Password sai", Snackbar.LENGTH_SHORT).show()
                    false
                }
            }
            true
        }
    }

    private fun setUpEventSignUp() {
        binding.tvOnClickSingUp.setOnClickListener {
            navGraph.navigate(R.id.registeFragment)
        }
    }
}