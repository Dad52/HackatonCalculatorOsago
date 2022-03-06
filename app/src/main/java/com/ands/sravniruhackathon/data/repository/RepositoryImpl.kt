package com.ands.sravniruhackathon.data.repository

import com.ands.sravniruhackathon.data.network.ApiService
import com.ands.sravniruhackathon.data.storage.UiDataLocalStorage
import com.ands.sravniruhackathon.domain.entities.*
import com.ands.sravniruhackathon.domain.repository.Repository
import retrofit2.Response

class RepositoryImpl(
        private val uiDataLocalStorage: UiDataLocalStorage,
        private val apiService: ApiService
) : Repository {

    override fun getDefaultCoeffsData(): List<Coeffs> {
        return uiDataLocalStorage.getDefaultCoeffsData()
    }

    override fun getSearchList(itemId: String): List<String> {
        return uiDataLocalStorage.getSearchList(itemId = itemId)
    }

    override fun getUiEntBrmSht(): List<UiDataEntBtmSht> {
        return uiDataLocalStorage.getUiEntBtmSheet()
    }

    override suspend fun getCoeffs(data: EnteredData): Response<CoeffsResponse> {
        return apiService.getCoeffs(data = data)
    }

    override suspend fun getOffers(data: EnteredData): Response<OffersResponse> {
        return apiService.getOffers(data = data)
    }

}