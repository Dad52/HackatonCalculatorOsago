package com.ands.sravniruhackathon.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import coil.size.Precision
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.ands.sravniruhackathon.R
import com.ands.sravniruhackathon.databinding.FragmentResultBtmSheetBinding
import com.ands.sravniruhackathon.presentation.viewmodels.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ResultBtmSheet : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentResultBtmSheetBinding
    private val mainViewModel: MainViewModel by activityViewModels()

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

            companyIcon.contentDescription = offerInfo?.branding?.iconTitle
            companyIcon.load(offerInfo?.branding?.bankLogoUrlSVG, imageLoader = mainViewModel.imageLoaderCoil) {
                size(100)
                scale(coil.size.Scale.FILL)
                precision(coil.size.Precision.EXACT)
                listener(
                    onError = { _, _ ->

                        companyIcon.setImageDrawable(null)

                        bgCompanyIcon.apply {
                            isVisible = true
                            setCardBackgroundColor(getColorInt(offerInfo?.branding?.backgroundColor))
                        }

                        imageDescription.apply {
                            text = offerInfo?.branding?.iconTitle
                            setTextColor(getColorInt(offerInfo?.branding?.fontColor))
                        }

                    }
                )
            }
            companyIcon.contentDescription = offerInfo?.branding?.iconTitle
            companyName.text = offerInfo?.name
            companyRating.text = offerInfo?.rating.toString()
            offerPrice.text = binding.root.context.getString(
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