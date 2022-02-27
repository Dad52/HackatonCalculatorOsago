package com.ands.sravniruhackathon.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import com.ands.sravniruhackathon.databinding.FragmentEnteringDataBtmSheetBinding
import com.ands.sravniruhackathon.presentation.viewmodels.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EnteringDataBtmSheet : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentEnteringDataBtmSheetBinding
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEnteringDataBtmSheetBinding.inflate(inflater, container, false)

        setUpSearchList()
        questionObserver()
        enteredDataObserver()

        return binding.root
    }

    private fun setUpSearchList() {

        mainViewModel.searchListDataLiveData.observe(viewLifecycleOwner, { list ->

            val searchAdapter: ArrayAdapter<String> = ArrayAdapter(
                requireContext(), android.R.layout.simple_list_item_1, list
            )

            binding.searchList.adapter = searchAdapter

            binding.searchList.setOnItemClickListener { view, _, position, _ ->
                binding.searchView.setQuery(view.getItemAtPosition(position).toString(), false)
            }

            binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    binding.searchView.clearFocus()
                    if (list.contains(query)) {
                        searchAdapter.filter.filter(query)
                    }
                    return false
                }

                override fun onQueryTextChange(text: String?): Boolean {
                    searchAdapter.filter.filter(text)
                    return false
                }
            })
        })
    }

    private fun questionObserver() = with(binding) {
        mainViewModel.currentQuestionUi.observe(viewLifecycleOwner, { item ->

            dialogTitle.text = item.title
            searchView.queryHint = item.hint
            nextBtn.text = item.buttonTitle

            mainViewModel.updateMapEnteredData()
            mainViewModel.changeSearchList(itemId = item.id)
            mainViewModel.setEntDataForInput(itemId = item.id)

            if (item.id == "regCity") backBtn.visibility = View.GONE
            else backBtn.visibility = View.VISIBLE

            backBtn.setOnClickListener() {
                mainViewModel.changeQuestionNumber("", -1)
            }

            nextBtn.setOnClickListener {
                mainViewModel.addEnteredData(itemId = item.id, value = searchView.query.toString())
                searchView.setQuery("", false)

                if (item.id == mainViewModel.getLastTitleId()) {
                    mainViewModel.getCoeffs()
                    dismiss()
                }
            }
        })
    }

    private fun enteredDataObserver() = with(binding) {
        mainViewModel.currentEnteredData.observe(viewLifecycleOwner, { data ->
            if (data != "null") {
                searchView.setQuery(data.toString(), false)
            }
        })
    }

}