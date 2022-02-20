package com.ands.sravniruhackathon.presentation.fragments

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.ands.sravniruhackathon.R
import com.ands.sravniruhackathon.databinding.FragmentMainBinding
import com.ands.sravniruhackathon.domain.entities.Coeffs
import com.ands.sravniruhackathon.presentation.adapters.CoeffsAdapter
import com.ands.sravniruhackathon.presentation.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        setUpRv()
        setUpButtons()

        return binding.root
    }

    private fun setUpButtons() = with(binding) {

        calculateOsagoBtn.setOnClickListener() {
            Navigation.findNavController(binding.root).navigate(R.id.action_mainFragment_to_offersFragment)
        }

        expandableCardLayout.expandableCardView.setOnClickListener() {
            expandCardView()
        }

    }

    private fun expandCardView() = with(binding) {

        val recyclerView = expandableCardLayout.rvInvisiblePart

        if (recyclerView.visibility == View.GONE) {
            recyclerView.visibility = View.VISIBLE
            expandableCardLayout.expandImage.setImageResource(R.drawable.ic_baseline_expand_less_24)
        } else {
            recyclerView.visibility = View.GONE
            expandableCardLayout.expandImage.setImageResource(R.drawable.ic_baseline_expand_more_24)
        }
        TransitionManager.beginDelayedTransition(
            expandableCardLayout.expandableLayout,
            AutoTransition()
        )
    }

    private fun setUpRv() = with(binding) {

        val recView = expandableCardLayout.rvInvisiblePart
        recView.itemAnimator = null

        val coeffsAdapter = CoeffsAdapter()
        recView.adapter = coeffsAdapter
        recView.layoutManager = LinearLayoutManager(requireContext())

        mainViewModel.currentCoeffsData.observe(requireActivity(), { data ->
            if (data != null) {

                setHeaderCoeffs(data)
                coeffsAdapter.submitList(data)
            }
        })
    }

    private fun setHeaderCoeffs(list: List<Coeffs>) = with(binding) {
        var coeffsHtmlText = ""

        val color = String.format("#%06x", ContextCompat.getColor(requireContext(), R.color.MainBlue) and 0xffffff)

        list.forEachIndexed { index, coeffsList ->

            if (index == list.lastIndex) {
                coeffsHtmlText += "<font color=\"$color\">${coeffsList.headerValue}</font>"
                return@forEachIndexed
            }
            coeffsHtmlText += "<font color=\"$color\">${coeffsList.headerValue}</font>×"

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            expandableCardLayout.coeffsText.text = Html.fromHtml(
                coeffsHtmlText,
                Html.FROM_HTML_MODE_LEGACY
            )
        } else {
            expandableCardLayout.coeffsText.text = Html.fromHtml(coeffsHtmlText)
        }
    }

}