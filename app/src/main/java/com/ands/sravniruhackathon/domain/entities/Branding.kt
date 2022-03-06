package com.ands.sravniruhackathon.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Branding(
    val backgroundColor: String,
    val bankLogoUrlPDF: String,
    val bankLogoUrlSVG: String,
    val fontColor: String,
    val iconTitle: String,
    val name: String
) : Parcelable