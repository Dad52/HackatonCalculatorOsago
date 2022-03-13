package com.ands.sravniruhackathon.domain.usecases

import com.ands.sravniruhackathon.domain.repository.Repository

class GetSearchListUseCase(private val repository: Repository) {

    fun execute(itemId: String): List<String>? {
        return repository.getSearchList(itemId = itemId)
    }

}