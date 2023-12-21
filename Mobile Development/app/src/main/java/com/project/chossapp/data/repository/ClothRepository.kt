package com.project.chossapp.data.repository

import com.project.chossapp.data.local.ClothDao
import com.project.chossapp.data.model.Cloth
import kotlinx.coroutines.flow.Flow

class ClothRepository(
    private val clothDao: ClothDao
) {
    fun getClothes(): Flow<List<Cloth>> {
        return clothDao.getClothes()
    }
    suspend fun saveCloth(cloth: Cloth) {
        clothDao.upsert(cloth)
    }
    suspend fun delete(cloth: Cloth) {
        clothDao.delete(cloth)
    }
}