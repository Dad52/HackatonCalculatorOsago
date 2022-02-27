package com.ands.sravniruhackathon.domain.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class EnteredData(
    var regCity: String = "",
    var enginePower: String = "",
    var quantityDrivers: String = "",
    var minDriverAge: String = "",
    var minDriverExp: String = "",
    var yearsWithoutAccidents: String = ""

): Parcelable

