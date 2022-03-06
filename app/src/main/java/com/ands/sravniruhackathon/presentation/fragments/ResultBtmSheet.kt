package com.ands.sravniruhackathon.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import coil.size.Precision
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.ands.sravniruhackathon.R
import com.ands.sravniruhackathon.databinding.FragmentResultBtmSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

@AndroidEntryPoint
class ResultBtmSheet : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentResultBtmSheetBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultBtmSheetBinding.inflate(inflater, container, false)

        loadCardData()

        binding.doneBtn.setOnClickListener { dismiss() }

        return binding.root
    }

    private fun loadCardData() = with(binding) {
        val offerInfo = ResultBtmSheetArgs.fromBundle(requireArguments()).offer

        binding.offerItem.apply {

            bgCompanyIcon.setCardBackgroundColor(getColorInt(offerInfo?.branding?.backgroundColor))

            val imageLoader = ImageLoader.Builder(binding.root.context)
                    .componentRegistry {
                        add(SvgDecoder(binding.root.context))
                    }
                    .build()

            companyIcon.load(offerInfo?.branding?.bankLogoUrlSVG, imageLoader = imageLoader) {
                size(70)
                scale(Scale.FILL)
                precision(Precision.EXACT)
                listener(
                        onError = { _, _ ->
                            companyIcon.setImageDrawable(null)
                            imageDescription.apply {
                                isVisible = true
                                text = offerInfo?.branding?.iconTitle
                                setTextColor(getColorInt(offerInfo?.branding?.fontColor))
                            }
                        }
                )
            }
            companyIcon.contentDescription = offerInfo?.branding?.iconTitle
            companyName.text = offerInfo?.name
            companyRating.text = offerInfo?.rating.toString()
            offerPrice.text = getString(
                    R.string.PricePlaceholder,
                    thousandSeparator(offerInfo?.price.toString().toDouble())
            )
        }
    }

    private fun getColorInt(colorCode: String?): Int {
        return getString(R.string.colorLattice, colorCode).toColorInt()
    }

    private fun thousandSeparator(str: Double): String {
        val newDecimalFormatSymbols = DecimalFormatSymbols(Locale.forLanguageTag("ru"))
        val myFormatter = DecimalFormat("###,###", newDecimalFormatSymbols)
        return myFormatter.format(str)
    }

}