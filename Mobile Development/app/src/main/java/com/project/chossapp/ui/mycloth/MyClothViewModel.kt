package com.project.chossapp.ui.mycloth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.chossapp.data.model.Cloth
import com.project.chossapp.data.repository.ClothRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyClothViewModel @Inject constructor(
    private val clothRepository: ClothRepository
): ViewModel() {
    fun getFavorite() = clothRepository.getClothes()
    fun deleteFavorite(cloth: Cloth) =
        viewModelScope.launch {
            clothRepository.delete(cloth)
        }
}