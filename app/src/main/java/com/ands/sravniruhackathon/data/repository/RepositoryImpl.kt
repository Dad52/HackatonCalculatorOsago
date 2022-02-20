package com.ands.sravniruhackathon.data.repository

import com.ands.sravniruhackathon.data.storage.UiDataLocalStorage
import com.ands.sravniruhackathon.domain.entities.Coeffs
import com.ands.sravniruhackathon.domain.repository.Repository

class RepositoryImpl(
        private val uiDataLocalStorage: UiDataLocalStorage
): Repository {
    override fun getDefaultCoeffsData(): List<Coeffs> {
        return uiDataLocalStorage.getDefaultCoeffsData()
    }
}