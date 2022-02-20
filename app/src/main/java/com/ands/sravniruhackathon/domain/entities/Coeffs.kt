package com.ands.sravniruhackathon.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Coeffs(
        val title: String? = null,
        val headerValue: String? = null,
        val value: String? = null,
        val name: String? = null,
        val detailText: String? = null
): Parcelable
