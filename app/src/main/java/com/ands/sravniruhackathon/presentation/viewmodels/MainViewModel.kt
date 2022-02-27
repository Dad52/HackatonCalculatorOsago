package com.ands.sravniruhackathon.presentation.viewmodels

import android.text.Spanned
import android.util.Log
import androidx.lifecycle.*
import com.ands.sravniruhackathon.domain.entities.Coeffs
import com.ands.sravniruhackathon.domain.entities.EnteredData
import com.ands.sravniruhackathon.domain.repository.Repository
import com.ands.sravniruhackathon.domain.usecases.*
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
    private val repository: Repository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val defaultCoeffsData = getDefaultCoeffsDataUseCase.execute()

    private val currentCoeffsDataMutable =
        savedStateHandle.getLiveData<List<Coeffs>>("currentCoeffs", defaultCoeffsData)
    val currentCoeffsData: LiveData<List<Coeffs>> = currentCoeffsDataMutable

    fun getCoeffs() = viewModelScope.launch {
        try {
            repository.getCoeffs(localEnteredData).let { response ->
                if (response.isSuccessful) {
                    currentCoeffsDataMutable.postValue(response.body()?.factors)
                    Log.e("TAG", response.body().toString())
                } else {
                    Log.d("tag", "Error during request: ${response.code()}")
                }
            }
        } catch (e: Exception) {
            Log.e("TAG", "Exception during request: ${e.localizedMessage}")
        }
    }


    private val uiDataEntBtmSht = getUiDataEntBtmShtUseCase.execute()

    private var localEnteredData = savedStateHandle.get<EnteredData>("enteredData") ?: EnteredData()

    private val enteredDataMutable =
        savedStateHandle.getLiveData<EnteredData>("enteredData", localEnteredData)
    val enteredData: LiveData<EnteredData> = enteredDataMutable

    private var prevQuestionNum = 0
    private var currQuestionNum = 0

    val currentQuestionUi = MutableLiveData(uiDataEntBtmSht[currQuestionNum])
    val currentEnteredData = MutableLiveData<String>()

    fun addEnteredData(itemId: String, value: String) {
        val newData =
            addEnteredDataUseCase.execute(itemId = itemId, data = localEnteredData, value = value)
        enteredDataMutable.postValue(newData)
        updateMapEnteredData()

        changeQuestionNumber(value, 1)
    }

    private var mapEnteredData = mapOf<String, String>()

    fun updateMapEnteredData() {
        mapEnteredData = mapOf<String, String>(
            "regCity" to localEnteredData.regCity,
            "enginePower" to localEnteredData.enginePower,
            "quantityDrivers" to localEnteredData.quantityDrivers,
            "minDriverAge" to localEnteredData.minDriverAge,
            "minDriverExp" to localEnteredData.minDriverExp,
            "yearsWithoutAccidents" to localEnteredData.yearsWithoutAccidents
        )
    }

    fun changeQuestionNumber(value: String, variable: Int) {

        val currentQuestionId = currentQuestionUi.value?.id

        if (currentQuestionId == "quantityDrivers" && value == "Без ограничений") {
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

        if (chosenNumber == 4 && localEnteredData.quantityDrivers == "Без ограничений")
            prevQuestionNum = currQuestionNum - 2

        setCurrentQuestion()
    }

    fun getLastTitleId(): String {
        return uiDataEntBtmSht[uiDataEntBtmSht.size - 1].id
    }


    private val searchListData = MutableLiveData<List<String>>()
    val searchListDataLiveData = searchListData

    fun changeSearchList(itemId: String) {
        val newSearchList = getSearchListUseCase.execute(itemId = itemId)
        searchListData.postValue(newSearchList)
    }

    private val headerCoeffsMutable = MutableLiveData<Spanned>()
    val headerCoeffs: LiveData<Spanned> = headerCoeffsMutable

    fun updateHeaderCoeffs(list: List<Coeffs>) {
        headerCoeffsMutable.postValue(makeHeaderCoeffsUseCase.execute(list))
    }

}