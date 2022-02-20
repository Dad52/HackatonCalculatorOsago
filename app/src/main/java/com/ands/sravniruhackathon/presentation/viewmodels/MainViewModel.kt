package com.ands.sravniruhackathon.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ands.sravniruhackathon.domain.entities.Coeffs
import com.ands.sravniruhackathon.domain.usecases.GetDefaultCoeffsDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
        private val getDefaultCoeffsDataUseCase: GetDefaultCoeffsDataUseCase
) : ViewModel() {

    private val defaultCoeffsData = getDefaultCoeffsDataUseCase.execute()

    private val currentCoeffsDataMutable = MutableLiveData<List<Coeffs>>(defaultCoeffsData)
    val currentCoeffsData: LiveData<List<Coeffs>> = currentCoeffsDataMutable


}