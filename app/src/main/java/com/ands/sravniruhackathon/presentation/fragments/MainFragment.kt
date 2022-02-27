package com.ands.sravniruhackathon.presentation.fragments

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.ands.sravniruhackathon.R
import com.ands.sravniruhackathon.databinding.FragmentMainBinding
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

        setUpRvObserver()
        setUpButtons()
        enteredDataObserver()
        headerCoeffsObserver()

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val recyclerView = binding.expandableCardLayout.rvInvisiblePart
        val state = recyclerView.visibility == View.VISIBLE
        outState.putString(CARD_VIEW_EXPANDED, state.toString())

        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        val recyclerView = binding.expandableCardLayout.rvInvisiblePart
        val savedState = savedInstanceState?.getString(CARD_VIEW_EXPANDED, "false")
        if (savedState == "true") {
            recyclerView.visibility = View.VISIBLE
            binding.expandableCardLayout.expandImage.setImageResource(R.drawable.ic_baseline_expand_less_24)
        }

        super.onViewStateRestored(savedInstanceState)
    }

    private fun setUpButtons() = with(binding) {

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

    }

    private fun openEntBtmSheet(chosenQuestion: Int) {
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_mainFragment_to_enteringDataBtmSheet)
        mainViewModel.chooseAnyQuestion(chosenQuestion)

        binding.apply {

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
    }//в следующем таске сделаю плавную анимацию, пока что оставил поломанную

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
                Log.e("MainFragmentObserver", data.toString())

                minDriverAgeInput.isEnabled = data.quantityDrivers != "Без ограничений"

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
        calculateOsagoBtn.isEnabled = (
                regCityEditText.text!!.isNotEmpty() && enginePowerEditText.text!!.isNotEmpty() &&
                        quantityDriversEditText.text!!.isNotEmpty() &&
                        minDriverExpEditText.text!!.isNotEmpty() && yearsWithoutAccidentsEditText.text!!.isNotEmpty()
                )
    }

    companion object {
        const val CARD_VIEW_EXPANDED = "card_view_expanded"
    }
}