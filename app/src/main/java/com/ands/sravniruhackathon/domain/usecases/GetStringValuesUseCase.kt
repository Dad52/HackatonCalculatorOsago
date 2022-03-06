package com.ands.sravniruhackathon.domain.usecases

import android.content.Context

class GetStringValuesUseCase(private val context: Context) {

    fun execute(path: Int): String {
        return context.getString(path)
    }

}