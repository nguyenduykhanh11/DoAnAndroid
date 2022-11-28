package com.example.doanadroid.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.doanadroid.R
import com.example.doanadroid.databinding.FragmentProfileBinding
import com.example.doanadroid.model.entity.UserEntity

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val navGraph by lazy{ Navigation.findNavController(requireActivity(), R.id.host_profile_fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = arguments?.getParcelable<UserEntity>("userData")
        setUpView(user)
        setUpEventUpdate(user)
    }

    private fun setUpEventUpdate(user: UserEntity?) {
        val bundle = Bundle()
        bundle.putParcelable("upadte_user", user)
        binding.floatBottomUpdate.setOnClickListener {
            navGraph.navigate(R.id.updateProfileFragment, bundle)
        }
    }

    private fun setUpView(user: UserEntity?) {
        with(binding){
            tvName.text = user?.name.toString()
            tvPhone.text = user?.phone.toString()
            tvLocation.text = user?.address.toString()
            tvBirthday.text = user?.brithday.toString()
            tvEmail.text = user?.email.toString()
        }
    }
}