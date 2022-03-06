package com.ands.sravniruhackathon.data.storage

import android.content.Context
import android.util.Log
import com.ands.sravniruhackathon.R
import com.ands.sravniruhackathon.domain.entities.Coeffs
import com.ands.sravniruhackathon.domain.entities.CoeffsResponse
import com.ands.sravniruhackathon.domain.entities.UiDataEntBtmSht
import com.ands.sravniruhackathon.domain.entities.UiDataEntBtmShtResponse
import com.ands.sravniruhackathon.domain.utils.Constants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

class UiDataLocalStorageImpl(private val context: Context): UiDataLocalStorage {

    override fun getDefaultCoeffsData(): List<Coeffs> {
        val jsonFileString = getJsonDataFromAsset(context, "defaultCoeffsData.json") ?: return emptyList()

        val listType = object : TypeToken<CoeffsResponse>() {}.type
        val coeffsResponse: CoeffsResponse = Gson().fromJson(jsonFileString, listType)

        return coeffsResponse.factors
    }

    override fun getUiEntBtmSheet(): List<UiDataEntBtmSht> {

        val jsonFileString = getJsonDataFromAsset(context, "uiDataEntBtmSht.json") ?: return emptyList()

        val listType = object : TypeToken<UiDataEntBtmShtResponse>() {}.type
        val uiDataEntBtmShtResponse: UiDataEntBtmShtResponse = Gson().fromJson(jsonFileString, listType)

        return uiDataEntBtmShtResponse.uiData
    }

    override fun getSearchList(itemId: String): List<String> {

        val citiesArray = context.resources.getStringArray(R.array.citiesHints).toList()
        val enginePowerArray = context.resources.getStringArray(R.array.enginePowerHints).toList()
        val quantityDriversArray = context.resources.getStringArray(R.array.quantityDriversHints).toList()
        val minDriverAgeArray = context.resources.getStringArray(R.array.minDriverAgeHints).toList()
        val minDriverExpArray = context.resources.getStringArray(R.array.minDriverExpHints).toList()
        val yearsWithoutAccidentsArray = context.resources.getStringArray(R.array.yearsWithoutAccidentsHints).toList()

        val searchListData = mapOf<String, List<String>>(
                Constants.REG_CITY to citiesArray,
                Constants.ENGINE_POWER to enginePowerArray,
                Constants.QUANTITY_DRIVERS to quantityDriversArray,
                Constants.MIN_DRIVER_AGE to minDriverAgeArray,
                Constants.MIN_DRIVER_EXP to minDriverExpArray,
                Constants.YEARS_WITHOUT_ACCIDENTS to yearsWithoutAccidentsArray
        )
        return searchListData[itemId] ?: emptyList()
    }

    private fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

}