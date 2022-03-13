package com.ands.sravniruhackathon.domain.usecases

import com.ands.sravniruhackathon.domain.entities.EnteredData
import com.ands.sravniruhackathon.domain.utils.Constants

class AddEnteredDataUseCase {

    fun execute(itemId: String, data: EnteredData, value: String): EnteredData {

        when (itemId) {
            Constants.REG_CITY -> {
                data.regCity = value
            }
            Constants.ENGINE_POWER -> {
                data.enginePower = value
            }
            Constants.QUANTITY_DRIVERS -> {
                data.quantityDrivers = value
            }
            Constants.MIN_DRIVER_AGE -> {
                data.minDriverAge = value
            }
            Constants.MIN_DRIVER_EXP -> {
                data.minDriverExp = value
            }
            Constants.YEARS_WITHOUT_ACCIDENTS -> {
                data.yearsWithoutAccidents = value
            }
        }
        return data
    }

}