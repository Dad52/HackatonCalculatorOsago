package com.ands.sravniruhackathon.presentation.viewmodels

import android.text.Spanned
import android.util.Log
import androidx.lifecycle.*
import com.ands.sravniruhackathon.R
import com.ands.sravniruhackathon.domain.entities.Coeffs
import com.ands.sravniruhackathon.domain.entities.EnteredData
import com.ands.sravniruhackathon.domain.entities.OffersResponse
import com.ands.sravniruhackathon.domain.repository.Repository
import com.ands.sravniruhackathon.domain.usecases.*
import com.ands.sravniruhackathon.domain.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getDefaultCoeffsDataUseCase: GetDefaultCoeffsDataUseCase,
    private val getSearchListUseCase: GetSearchListUseCase,
    private val getUiDataEntBtmShtUseCase: GetUiDataEntBtmShtUseCase,
    private val addEnteredDataUseCase: AddEnteredDataUseCase,
    private val makeHeaderCoeffsUseCase: MakeHeaderCoeffsUseCase,
    private val getStringValuesUseCase: GetStringValuesUseCase,
    private val repository: Repository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val defaultCoeffsData = getDefaultCoeffsDataUseCase.execute()

    private val _currentCoeffsData =
        savedStateHandle.getLiveData<List<Coeffs>>(
            CURRENT_COEFFS,
            defaultCoeffsData
        )
    val currentCoeffsData: LiveData<List<Coeffs>> = _currentCoeffsData

    fun getCoeffs() = viewModelScope.launch {
        try {
            repository.getCoeffs(localEnteredData).let { response ->
                if (response.isSuccessful) {
                    _currentCoeffsData.postValue(response.body()?.factors)
                } else {
                    Log.d("tag", "Error during request: ${response.code()}")
                }
            }
        } catch (e: Exception) {
            Log.e("TAG", "Exception during request: ${e.localizedMessage}")
        }
    }

    private val uiDataEntBtmSht = getUiDataEntBtmShtUseCase.execute()

    private var localEnteredData = savedStateHandle.get<EnteredData>(ENTERED_DATA) ?: EnteredData()

    private val _enteredData =
        savedStateHandle.getLiveData<EnteredData>(ENTERED_DATA, localEnteredData)
    val enteredData: LiveData<EnteredData> = _enteredData

    private var prevQuestionNum = 0
    private var currQuestionNum = 0

    val currentQuestionUi = MutableLiveData(uiDataEntBtmSht[currQuestionNum])
    val currentEnteredData = MutableLiveData<String>()

    fun addEnteredData(itemId: String, value: String) {
        val newData =
            addEnteredDataUseCase.execute(itemId = itemId, data = localEnteredData, value = value)
        _enteredData.postValue(newData)
        updateMapEnteredData()

        changeQuestionNumber(value, 1)
    }

    private var mapEnteredData = mapOf<String, String>()

    fun updateMapEnteredData() {
        mapEnteredData = mapOf<String, String>(
            Constants.REG_CITY to localEnteredData.regCity,
            Constants.ENGINE_POWER to localEnteredData.enginePower,
            Constants.QUANTITY_DRIVERS to localEnteredData.quantityDrivers,
            Constants.MIN_DRIVER_AGE to localEnteredData.minDriverAge,
            Constants.MIN_DRIVER_EXP to localEnteredData.minDriverExp,
            Constants.YEARS_WITHOUT_ACCIDENTS to localEnteredData.yearsWithoutAccidents
        )
    }//на 4 неделе постараюсь перейти от связи по id к каком-либо другому способу

    private val withoutLimitsText = getStringValuesUseCase.execute(withoutLimitsValue)

    fun changeQuestionNumber(value: String = "", variable: Int) {

        val currentQuestionId = currentQuestionUi.value?.id

        if (currentQuestionId == quantityDrivers && value == withoutLimitsText) {
            prevQuestionNum = currQuestionNum
            currQuestionNum += 2
            localEnteredData.minDriverAge = ""
            setCurrentQuestion()
            return
        }

        if (variable == -1) {
            currQuestionNum = prevQuestionNum
            prevQuestionNum = currQuestionNum - 1
        }

        if (variable == 1) {
            prevQuestionNum = currQuestionNum
            currQuestionNum += 1
        }
        setCurrentQuestion()
    }

    private fun setCurrentQuestion() {
        if (currQuestionNum > uiDataEntBtmSht.size - 1 || currQuestionNum < 0) {
            currQuestionNum = 0
        }
        currentQuestionUi.postValue(uiDataEntBtmSht[currQuestionNum])
    }

    fun setEntDataForInput(itemId: String) {
        currentEnteredData.postValue(mapEnteredData[itemId].toString())
    }

    fun chooseAnyQuestion(chosenNumber: Int) {
        currQuestionNum = chosenNumber

        if (currQuestionNum > 0)
            prevQuestionNum = currQuestionNum - 1
        else
            prevQuestionNum = 0

        if (chosenNumber == 4 && localEnteredData.quantityDrivers == withoutLimitsText)
            prevQuestionNum = currQuestionNum - 2

        setCurrentQuestion()
    }

    fun getLastTitleId(): String {
        return uiDataEntBtmSht[uiDataEntBtmSht.size - 1].id
    }

    fun getFirstTitleId(): String {
        return uiDataEntBtmSht[0].id
    }


    private val _searchListData = MutableLiveData<List<String>>()
    val searchListData = _searchListData

    fun changeSearchList(itemId: String) {
        val newSearchList = getSearchListUseCase.execute(itemId = itemId)
        _searchListData.postValue(newSearchList)
    }

    private val _headerCoeffs = MutableLiveData<Spanned>()
    val headerCoeffs: LiveData<Spanned> = _headerCoeffs

    fun updateHeaderCoeffs(list: List<Coeffs>) {
        _headerCoeffs.postValue(makeHeaderCoeffsUseCase.execute(list))
    }

    private val _offersResponse = MutableLiveData<OffersResponse>()
    val offersResponse: LiveData<OffersResponse> = _offersResponse

    fun getOffers() = viewModelScope.launch {
        try {
            repository.getOffers(localEnteredData).let { response ->
                if (response.isSuccessful) {
                    _offersResponse.postValue(response.body())
                } else {
                    Log.d("tag", "Error during request: ${response.code()}")
                }
            }
        } catch (e: Exception) {
            Log.e("TAG", "Exception during request: ${e.localizedMessage}")
        }
    }

    companion object {
        const val withoutLimitsValue = R.string.withoutLimits
        const val quantityDrivers = Constants.QUANTITY_DRIVERS
        const val ENTERED_DATA = "entered_data"
        const val CURRENT_COEFFS = "current_coeffs"
    }

}