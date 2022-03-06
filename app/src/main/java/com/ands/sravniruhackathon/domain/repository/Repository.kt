package com.ands.sravniruhackathon.domain.repository

import com.ands.sravniruhackathon.domain.entities.*
import retrofit2.Response

interface Repository {

    fun getDefaultCoeffsData(): List<Coeffs>

    fun getSearchList(itemId: String): List<String>

    fun getUiEntBrmSht(): List<UiDataEntBtmSht>

    suspend fun getCoeffs(data: EnteredData) : Response<CoeffsResponse>

    suspend fun getOffers(data: EnteredData) : Response<OffersResponse>
}