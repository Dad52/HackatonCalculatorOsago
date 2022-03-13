package com.ands.sravniruhackathon.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.ImageLoader
import com.ands.sravniruhackathon.R
import com.ands.sravniruhackathon.databinding.FragmentOffersBinding
import com.ands.sravniruhackathon.domain.entities.Offers
import com.ands.sravniruhackathon.presentation.adapters.CoeffsAdapter
import com.ands.sravniruhackathon.presentation.adapters.OffersAdapter
import com.ands.sravniruhackathon.presentation.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OffersFragment : Fragment() {

    private var _binding: FragmentOffersBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by activityViewModels()
    private var isCardViewExpanded: Boolean = false

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOffersBinding.inflate(inflater, container, false)
        isCardViewExpanded = savedInstanceState?.getBoolean(CARD_VIEW_EXPANDED2) ?: false

        setUpButtons()
        offersObserver()
        coeffsObserver()
        headerCoeffsObserver()
        mainViewModel.getOffers()

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(CARD_VIEW_EXPANDED2, isCardViewExpanded)
        super.onSaveInstanceState(outState)
    }

    private fun setUpButtons() = with(binding) {
        expandableCardLayout.expandableCardView.setOnClickListener { expandCardView() }
        backBtnImage.setOnClickListener() {
            Navigation.findNavController(binding.root)
                    .navigate(R.id.action_offersFragment_to_mainFragment)
        }
        if (isCardViewExpanded) {
            expandableCardLayout.apply {
                rvInvisiblePart.isVisible = true
                expandImage.setImageResource(R.drawable.ic_baseline_expand_less_24)
            }
        }
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

    private fun coeffsObserver() = with(binding) {

        val recView = expandableCardLayout.rvInvisiblePart

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

    private fun offersObserver() = with(binding) {


        val offersAdapter = OffersAdapter(object : OffersAdapter.ItemClick {
            override fun sendResultBack(offerInfo: Offers) {
                parentFragmentManager.setFragmentResult(
                        REQUEST_CODE_FOR_OFFER,
                        bundleOf(CHOSEN_OFFER_KEY to offerInfo)
                )
                findNavController().popBackStack()
            }
        }, imageLoader = mainViewModel.imageLoaderCoil)

        rvOffers.layoutManager = LinearLayoutManager(binding.root.context)
        rvOffers.adapter = offersAdapter

        mainViewModel.offersResponse.observe(viewLifecycleOwner, { offersResponse ->
            if (offersResponse != null) {
                offShimmer()
                calculateExactPriceBtn.isEnabled = true
                calculateExactPriceBtn.text = offersResponse.actionText
            }
            offersAdapter.submitList(offersResponse.offers)
        })
    }

    private fun offShimmer() = with(binding) {
        innerShimmerItem1.innerShimmer.stopShimmer()
        innerShimmerItem1.innerShimmer.hideShimmer()
        innerShimmerItem1.innerShimmer.isVisible = false
        shimmerLayout.isVisible = false
        rvOffers.isVisible = true
    }

    private fun headerCoeffsObserver() = with(binding) {
        mainViewModel.headerCoeffs.observe(viewLifecycleOwner, { headerLine ->
            expandableCardLayout.coeffsText.text = headerLine
        })
    }


    companion object {
        const val CHOSEN_OFFER_KEY = "chosen_offer_key"
        const val REQUEST_CODE_FOR_OFFER = "request_code_for_offer"
        const val CARD_VIEW_EXPANDED2 = "card_view_expanded2"
    }

}