package com.ands.sravniruhackathon.presentation.di

import android.content.Context
import com.ands.sravniruhackathon.domain.repository.Repository
import com.ands.sravniruhackathon.domain.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext


@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideGetDefaultCoeffsDataUseCase(repository: Repository): GetDefaultCoeffsDataUseCase {
        return GetDefaultCoeffsDataUseCase(repository)
    }

    @Provides
    fun provideGetSearchListUseCase(repository: Repository): GetSearchListUseCase {
        return GetSearchListUseCase(repository)
    }

    @Provides
    fun provideGetUiDataEntBtmSheetUseCase(repository: Repository): GetUiDataEntBtmShtUseCase {
        return GetUiDataEntBtmShtUseCase(repository = repository)
    }

    @Provides
    fun provideAddEnteredDataUseCase(): AddEnteredDataUseCase {
        return AddEnteredDataUseCase()
    }

    @Provides
    fun provideMakeHeaderCoeffsUseCase(@ApplicationContext context: Context): MakeHeaderCoeffsUseCase {
        return MakeHeaderCoeffsUseCase(context = context)
    }

    @Provides
    fun provideGetStringValuesUseCase(@ApplicationContext context: Context): GetStringValuesUseCase {
        return GetStringValuesUseCase(context = context)
    }

}