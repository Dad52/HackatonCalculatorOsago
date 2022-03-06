package com.ands.sravniruhackathon.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Offers(
    val branding: Branding,
    val name: String,
    val price: Int,
    val rating: Double
) : Parcelable
