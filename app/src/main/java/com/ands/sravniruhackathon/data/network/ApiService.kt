package com.ands.sravniruhackathon.data.network

import com.ands.sravniruhackathon.domain.entities.CoeffsResponse
import com.ands.sravniruhackathon.domain.entities.EnteredData
import com.ands.sravniruhackathon.domain.utils.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST(Constants.END_POINT_COEFFS)
    suspend fun getCoeffs(@Body data: EnteredData): Response<CoeffsResponse>

}