package com.project.chossapp.ui.recommendation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.chossapp.data.model.Cloth
import com.project.chossapp.data.repository.ClothRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecommendationViewModel @Inject constructor(
    private val clothRepository: ClothRepository
): ViewModel() {
    fun addToMyCloth(cloth: Cloth) {
        viewModelScope.launch {
            clothRepository.saveCloth(cloth)
        }
    }
}