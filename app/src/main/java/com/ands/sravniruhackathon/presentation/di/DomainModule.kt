package com.ands.sravniruhackathon.presentation.di

import com.ands.sravniruhackathon.domain.repository.Repository
import com.ands.sravniruhackathon.domain.usecases.GetDefaultCoeffsDataUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideGetDefaultCoeffsDataUseCase(repository: Repository): GetDefaultCoeffsDataUseCase {
        return GetDefaultCoeffsDataUseCase(repository)
    }

}