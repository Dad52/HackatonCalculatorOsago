package com.ands.sravniruhackathon.domain.usecases

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.core.content.ContextCompat
import com.ands.sravniruhackathon.R
import com.ands.sravniruhackathon.domain.entities.Coeffs

class MakeHeaderCoeffsUseCase(private val context: Context) {

    fun execute(list: List<Coeffs>): Spanned {

        var coeffsHtmlText = ""

        val color = String.format("#%06x", ContextCompat.getColor(context, R.color.MainBlue) and 0xffffff)

        list.forEachIndexed { index, coeffsList ->

            if (index == list.lastIndex) {
                coeffsHtmlText += "<font color=\"$color\">${coeffsList.headerValue}</font>"
                return@forEachIndexed
            }
            coeffsHtmlText += "<font color=\"$color\">${coeffsList.headerValue}</font>×"

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(
                coeffsHtmlText,
                Html.FROM_HTML_MODE_LEGACY
            )
        } else {
            return Html.fromHtml(coeffsHtmlText)
        }
    }

}