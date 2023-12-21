package com.project.chossapp.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.project.chossapp.R
import com.project.chossapp.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvToLogin.setOnClickListener {
            findNavController().navigate(
                R.id.action_registerFragment_to_loginFragment
            )
        }

        observer()

        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val username = binding.etUsername.text.toString()

            if (email.isEmpty() && password.isEmpty() && username.isEmpty()) {
                Toast.makeText(requireContext(), "Harap isi semua fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.register(
                username,
                email,
                password
            )
        }
    }

    private fun observer() {
        val registerState = viewModel.registerState.asLiveData()
        registerState.observe(viewLifecycleOwner) {
            if (it == "success") {
                Toast.makeText(requireContext(), "Berhasil mendaftar, silahkan login", Toast.LENGTH_SHORT).show()
                findNavController().navigate(
                    R.id.action_registerFragment_to_loginFragment
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}