package com.ands.sravniruhackathon.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.ands.sravniruhackathon.R
import com.ands.sravniruhackathon.databinding.FragmentMainBinding
import com.ands.sravniruhackathon.domain.entities.Offers
import com.ands.sravniruhackathon.presentation.adapters.CoeffsAdapter
import com.ands.sravniruhackathon.presentation.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by activityViewModels()
    private var isCardViewExpanded: Boolean = false

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        isCardViewExpanded = savedInstanceState?.getBoolean(CARD_VIEW_EXPANDED) ?: false

        setUpRvObserver()
        setUpButtons()
        enteredDataObserver()
        headerCoeffsObserver()

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(CARD_VIEW_EXPANDED, isCardViewExpanded)
        super.onSaveInstanceState(outState)
    }

    private fun setUpButtons() = with(binding) {

        parentFragmentManager.setFragmentResultListener(
                OffersFragment.REQUEST_CODE_FOR_OFFER, viewLifecycleOwner
        ) { _, data ->
            val offer = data.getParcelable<Offers>(OffersFragment.CHOSEN_OFFER_KEY)

            val direction = MainFragmentDirections.actionMainFragmentToResultBtmSheet(offer = offer)
            Navigation.findNavController(binding.root).navigate(direction)
        }

        calculateOsagoBtn.setOnClickListener {
            Navigation.findNavController(binding.root)
                    .navigate(R.id.action_mainFragment_to_offersFragment)
        }

        expandableCardLayout.expandableCardView.setOnClickListener {
            expandCardView()
        }

        regCityEditText.setOnClickListener { openEntBtmSheet(0) }
        enginePowerEditText.setOnClickListener { openEntBtmSheet(1) }
        quantityDriversEditText.setOnClickListener { openEntBtmSheet(2) }
        minDriverAgeEditText.setOnClickListener { openEntBtmSheet(3) }
        minDriverExpEditText.setOnClickListener { openEntBtmSheet(4) }
        yearsWithoutAccidentsEditText.setOnClickListener { openEntBtmSheet(5) }

        if (isCardViewExpanded) {
            expandableCardLayout.apply {
                rvInvisiblePart.isVisible = true
                expandImage.setImageResource(R.drawable.ic_baseline_expand_less_24)
            }
        }
    }

    private fun openEntBtmSheet(chosenQuestion: Int) {
        Navigation.findNavController(binding.root)
                .navigate(R.id.action_mainFragment_to_enteringDataBtmSheet)
        mainViewModel.chooseAnyQuestion(chosenQuestion)
    }

    private fun expandCardView() = with(binding) {

        val recyclerView = expandableCardLayout.rvInvisiblePart
        isCardViewExpanded = !isCardViewExpanded

        if (recyclerView.isVisible) {
            recyclerView.isVisible = false
            expandableCardLayout.expandImage.setImageResource(R.drawable.ic_baseline_expand_more_24)
        } else {
            recyclerView.isVisible = true
            expandableCardLayout.expandImage.setImageResource(R.drawable.ic_baseline_expand_less_24)
        }

    }

    private fun setUpRvObserver() = with(binding) {

        val recView = expandableCardLayout.rvInvisiblePart
        recView.itemAnimator = null

        val coeffsAdapter = CoeffsAdapter()
        recView.adapter = coeffsAdapter
        recView.layoutManager = LinearLayoutManager(requireContext())

        mainViewModel.currentCoeffsData.observe(viewLifecycleOwner, { data ->
            if (data != null) {
                mainViewModel.updateHeaderCoeffs(data)
                coeffsAdapter.submitList(data)
            }
        })
    }

    private fun enteredDataObserver() = with(binding) {
        mainViewModel.enteredData.observe(viewLifecycleOwner, { data ->
            if (data != null) {
                minDriverAgeInput.isEnabled =
                        data.quantityDrivers != binding.root.context.getString(R.string.withoutLimits)

                regCityEditText.setText(data.regCity)
                enginePowerEditText.setText(data.enginePower)
                quantityDriversEditText.setText(data.quantityDrivers)
                minDriverAgeEditText.setText(data.minDriverAge)
                minDriverExpEditText.setText(data.minDriverExp)
                yearsWithoutAccidentsEditText.setText(data.yearsWithoutAccidents)

                enableDisableButton()
            }
        })
    }

    private fun headerCoeffsObserver() = with(binding) {
        mainViewModel.headerCoeffs.observe(viewLifecycleOwner, { headerLine ->
            expandableCardLayout.coeffsText.text = headerLine
        })
    }

    private fun enableDisableButton() = with(binding) {

        val withoutLimitsText = binding.root.context.getString(R.string.withoutLimits)

        calculateOsagoBtn.isEnabled = (
                regCityEditText.text!!.isNotEmpty() && enginePowerEditText.text!!.isNotEmpty() &&
                        quantityDriversEditText.text!!.isNotEmpty() && minDriverExpEditText.text!!.isNotEmpty() &&
                        yearsWithoutAccidentsEditText.text!!.isNotEmpty() &&
                        (minDriverAgeEditText.text!!.isNotEmpty() || quantityDriversEditText.text!!.toString() == withoutLimitsText)

                )

    }

    companion object {
        const val CARD_VIEW_EXPANDED = "card_view_expanded"
    }
}