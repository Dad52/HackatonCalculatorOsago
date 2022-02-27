package com.ands.sravniruhackathon.data.storage

import android.content.Context
import com.ands.sravniruhackathon.domain.entities.Coeffs
import com.ands.sravniruhackathon.domain.entities.CoeffsResponse
import com.ands.sravniruhackathon.domain.entities.UiDataEntBtmSht

interface UiDataLocalStorage {

    fun getDefaultCoeffsData(): List<Coeffs>

    fun getSearchList(itemId: String): List<String>

    fun getUiEntBtmSheet(): List<UiDataEntBtmSht>

}