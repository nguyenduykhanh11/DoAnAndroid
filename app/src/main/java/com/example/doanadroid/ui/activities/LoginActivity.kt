package com.example.doanadroid.ui.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.viewModels
import com.example.doanadroid.databinding.ActivityLoginBinding
import com.example.doanadroid.viewModel.ObseverViewModel
import com.example.todolist.utils.Constants
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val mObserveViewModel: ObseverViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpListenerBackStackActivity()
        setEventBack()
    }

    private fun setEventBack() {
        onBackPressedDispatcher.addCallback(this) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setUpListenerBackStackActivity() {
        val sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val editor =  sharedPref.edit()
        mObserveViewModel.ActionLogin.observe(this){
            if (it == Constants.ACTION_LOGIN){
                mObserveViewModel.SaveProfile.observe(this){ userLogin->
                    editor.apply{
                        putInt("Save_pref", userLogin[0].id!!)
                        apply()
                    }
                }
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}