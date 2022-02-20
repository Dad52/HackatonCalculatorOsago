package com.ands.sravniruhackathon.data.storage

import com.ands.sravniruhackathon.domain.entities.Coeffs

interface UiDataLocalStorage {

    fun getDefaultCoeffsData(): List<Coeffs>

}