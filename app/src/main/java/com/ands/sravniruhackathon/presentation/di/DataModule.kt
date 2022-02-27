package com.ands.sravniruhackathon.presentation.di

import android.content.Context
import com.ands.sravniruhackathon.data.network.ApiService
import com.ands.sravniruhackathon.data.repository.RepositoryImpl
import com.ands.sravniruhackathon.data.storage.UiDataLocalStorage
import com.ands.sravniruhackathon.data.storage.UiDataLocalStorageImpl
import com.ands.sravniruhackathon.domain.repository.Repository
import com.ands.sravniruhackathon.domain.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideRepository(uiDataLocalStorage: UiDataLocalStorage, apiService: ApiService): Repository {
        return RepositoryImpl(uiDataLocalStorage = uiDataLocalStorage, apiService = apiService)
    }

    @Provides
    @Singleton
    fun provideUiDataLocalStorage(@ApplicationContext context: Context): UiDataLocalStorage {
        return UiDataLocalStorageImpl(context = context)
    }

    @Provides
    fun provideBaseUrl() = Constants.BASE_URL

    @Provides
    @Singleton
    fun provideApiService(BASE_URL: String): ApiService {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
    }

}