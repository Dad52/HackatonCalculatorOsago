package com.ands.sravniruhackathon.domain.usecases

import com.ands.sravniruhackathon.domain.entities.Coeffs
import com.ands.sravniruhackathon.domain.repository.Repository

class GetDefaultCoeffsDataUseCase(private val repository: Repository) {

    fun execute(): List<Coeffs>? {
        return repository.getDefaultCoeffsData()
    }

}