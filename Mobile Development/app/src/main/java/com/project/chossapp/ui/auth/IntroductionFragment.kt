package com.project.chossapp.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.project.chossapp.R
import com.project.chossapp.databinding.FragmentIntroductionBinding

class IntroductionFragment : Fragment() {

    private var _binding: FragmentIntroductionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIntroductionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(
                R.id.action_introductionFragment_to_loginFragment
            )
        }
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(
                R.id.action_introductionFragment_to_registerFragment
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}