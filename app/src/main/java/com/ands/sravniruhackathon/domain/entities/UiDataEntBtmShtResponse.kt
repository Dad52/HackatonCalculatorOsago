package com.ands.sravniruhackathon.domain.entities

import com.google.gson.annotations.SerializedName

data class UiDataEntBtmShtResponse(
    @SerializedName("uiData")
    val uiData: List<UiDataEntBtmSht>
)