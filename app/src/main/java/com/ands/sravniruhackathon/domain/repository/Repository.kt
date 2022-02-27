package com.ands.sravniruhackathon.domain.repository

import com.ands.sravniruhackathon.domain.entities.Coeffs
import com.ands.sravniruhackathon.domain.entities.CoeffsResponse
import com.ands.sravniruhackathon.domain.entities.EnteredData
import com.ands.sravniruhackathon.domain.entities.UiDataEntBtmSht
import retrofit2.Response

interface Repository {

    fun getDefaultCoeffsData(): List<Coeffs>

    suspend fun getCoeffs(data: EnteredData) : Response<CoeffsResponse>

    fun getSearchList(itemId: String): List<String>

    fun getUiEntBrmSht(): List<UiDataEntBtmSht>

}