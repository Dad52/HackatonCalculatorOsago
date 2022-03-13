package com.ands.sravniruhackathon.domain.usecases

import com.ands.sravniruhackathon.domain.entities.UiDataEntBtmSht
import com.ands.sravniruhackathon.domain.repository.Repository

class GetUiDataEntBtmShtUseCase(private val repository: Repository) {

    fun execute(): List<UiDataEntBtmSht>? {
        return repository.getUiEntBrmSht()
    }

}