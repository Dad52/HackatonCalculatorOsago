package com.ands.sravniruhackathon.data.repository

import com.ands.sravniruhackathon.data.network.ApiService
import com.ands.sravniruhackathon.data.storage.UiDataLocalStorage
import com.ands.sravniruhackathon.domain.entities.Coeffs
import com.ands.sravniruhackathon.domain.entities.CoeffsResponse
import com.ands.sravniruhackathon.domain.entities.EnteredData
import com.ands.sravniruhackathon.domain.entities.UiDataEntBtmSht
import com.ands.sravniruhackathon.domain.repository.Repository
import retrofit2.Response

class RepositoryImpl(
        private val uiDataLocalStorage: UiDataLocalStorage,
        private val apiService: ApiService
) : Repository {

    override fun getDefaultCoeffsData(): List<Coeffs> {
        return uiDataLocalStorage.getDefaultCoeffsData()
    }

    override suspend fun getCoeffs(data: EnteredData): Response<CoeffsResponse> {
        return apiService.getCoeffs(data = data)
    }

    override fun getSearchList(itemId: String): List<String> {
        return uiDataLocalStorage.getSearchList(itemId = itemId)
    }

    override fun getUiEntBrmSht(): List<UiDataEntBtmSht> {
        return uiDataLocalStorage.getUiEntBtmSheet()
    }


}