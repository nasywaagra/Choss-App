package com.project.chossapp.ui.mycloth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.project.chossapp.databinding.FragmentMyClothBinding
import com.project.chossapp.ui.mycloth.adapter.MyClothAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyClothFragment : Fragment() {

    private var _binding: FragmentMyClothBinding? = null
    private val binding get() = _binding!!
    private val myClothAdapter by lazy { MyClothAdapter() }
    private val viewModel by viewModels<MyClothViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyClothBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.rvMyCloth.apply {
            adapter = myClothAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

        lifecycleScope.launch {
            val cloth = viewModel.getFavorite().asLiveData()
            cloth.observe(viewLifecycleOwner) { list ->
                myClothAdapter.differ.submitList(list)

                myClothAdapter.onClick = {
                    viewModel.deleteFavorite(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}