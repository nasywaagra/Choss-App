package com.project.chossapp.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import com.project.chossapp.databinding.FragmentProfileBinding
import com.project.chossapp.ui.auth.AuthActivity
import com.project.chossapp.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val viewModel by viewModels<AuthViewModel>()
    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            startActivity(
                Intent(requireContext(), AuthActivity::class.java)
            )
            requireActivity().finish()
        }

        val user = viewModel.signedInUser.asLiveData()
        user.observe(viewLifecycleOwner) { data ->
            binding.tvEmail.text = data.email
        }
    }

}