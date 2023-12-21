package com.project.chossapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.chossapp.data.model.Cloth
import kotlinx.coroutines.flow.Flow

@Dao
interface ClothDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun upsert(cloth: Cloth)
    @Delete
    suspend fun delete(cloth: Cloth)
    @Query("SELECT * FROM Cloth")
    fun getClothes(): Flow<List<Cloth>>
}