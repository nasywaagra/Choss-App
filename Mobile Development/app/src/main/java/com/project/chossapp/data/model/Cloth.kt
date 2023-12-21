package com.project.chossapp.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
data class Cloth(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val photo: Int
)

@Parcelize
data class Clothes(
    val name: String? = null,
    val photos: List<Int>? = null,
    val colorTemperature: String? = "Warm"
): Parcelable