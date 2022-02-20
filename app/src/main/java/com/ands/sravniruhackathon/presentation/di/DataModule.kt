package com.ands.sravniruhackathon.presentation.di

import com.ands.sravniruhackathon.data.repository.RepositoryImpl
import com.ands.sravniruhackathon.data.storage.UiDataLocalStorage
import com.ands.sravniruhackathon.data.storage.UiDataLocalStorageImpl
import com.ands.sravniruhackathon.domain.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideRepository(uiDataLocalStorage: UiDataLocalStorage) : Repository {
        return RepositoryImpl(uiDataLocalStorage = uiDataLocalStorage)
    }

    @Provides
    @Singleton
    fun provideUiDataLocalStorage() : UiDataLocalStorage {
        return UiDataLocalStorageImpl()
    }

}