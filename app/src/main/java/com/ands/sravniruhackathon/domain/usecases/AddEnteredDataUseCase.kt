package com.ands.sravniruhackathon.domain.usecases

import com.ands.sravniruhackathon.domain.entities.EnteredData

class AddEnteredDataUseCase {

    fun execute(itemId: String, data: EnteredData, value: String): EnteredData {

        when (itemId) {
            "regCity" -> {
                data.regCity = value
            }
            "enginePower" -> {
                data.enginePower = value
            }
            "quantityDrivers" -> {
                data.quantityDrivers = value
            }
            "minDriverAge" -> {
                data.minDriverAge = value
            }
            "minDriverExp" -> {
                data.minDriverExp = value
            }
            "yearsWithoutAccidents" -> {
                data.yearsWithoutAccidents = value
            }
        }
        return data
    }

}