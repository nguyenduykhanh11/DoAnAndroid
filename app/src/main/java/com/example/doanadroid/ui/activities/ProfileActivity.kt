package com.example.doanadroid.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.addCallback
import androidx.navigation.Navigation
import com.example.doanadroid.R
import com.example.doanadroid.databinding.ActivityProfileBinding
import com.example.doanadroid.model.entity.UserEntity

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val navGraph by lazy{ Navigation.findNavController(this, R.id.host_profile_fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
        setUpEventBack()
    }

    private fun setUpEventBack() {
        onBackPressedDispatcher.addCallback(this) {
            val intent = Intent(this@ProfileActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setUpView() {
        val user = intent.getParcelableExtra<UserEntity>("ProFile")
        val bundle = Bundle()
        bundle.putParcelable("userData", user)
        navGraph.navigate(R.id.profileFragment, bundle)
    }
}