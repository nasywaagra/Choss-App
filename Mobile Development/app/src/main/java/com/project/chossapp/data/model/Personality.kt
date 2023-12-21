package com.project.chossapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Personality (
    val name: String,
    val description: String,
    val img: Int
): Parcelable