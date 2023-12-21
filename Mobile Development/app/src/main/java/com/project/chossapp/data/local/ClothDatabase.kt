package com.project.chossapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.project.chossapp.data.model.Cloth

@Database(entities = [Cloth::class], version = 1, exportSchema = false)
abstract class ClothDatabase: RoomDatabase() {
    abstract val clothDao: ClothDao
}