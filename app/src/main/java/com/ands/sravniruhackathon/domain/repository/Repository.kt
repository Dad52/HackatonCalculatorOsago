package com.ands.sravniruhackathon.domain.repository

import com.ands.sravniruhackathon.domain.entities.Coeffs

interface Repository {

    fun getDefaultCoeffsData(): List<Coeffs>

}