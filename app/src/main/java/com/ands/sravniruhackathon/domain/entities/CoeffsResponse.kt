package com.ands.sravniruhackathon.domain.entities

import com.google.gson.annotations.SerializedName

data class CoeffsResponse(
    @SerializedName("factors")
    val factors: List<Coeffs>
)
