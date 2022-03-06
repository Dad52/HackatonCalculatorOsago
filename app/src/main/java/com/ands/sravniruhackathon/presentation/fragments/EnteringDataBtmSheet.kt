package com.ands.sravniruhackathon.presentation.fragments

import android.os.Bundle
import android.util.Log
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
    private var itemId: String = ""
    private var isLastQuestion: Boolean = false

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

    override fun onDestroy() {
        if (itemId.isNotBlank() && !isLastQuestion) {
            mainViewModel.addEnteredData(
                    itemId = itemId,
                    value = binding.searchView.query.toString()
            )
            mainViewModel.getCoeffs()
        }
        super.onDestroy()
    }

    private fun setUpSearchList() = with(binding) {

        mainViewModel.searchListData.observe(viewLifecycleOwner, { list ->

            val searchAdapter: ArrayAdapter<String> = ArrayAdapter(
                    requireContext(), android.R.layout.simple_list_item_1, list
            )

            searchList.adapter = searchAdapter

            searchList.setOnItemClickListener { view, _, position, _ ->
                searchView.setQuery(view.getItemAtPosition(position).toString(), false)
            }

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchView.clearFocus()
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

            itemId = item.id
            dialogTitle.text = item.title
            searchView.queryHint = item.hint
            nextBtn.text = item.buttonTitle

            mainViewModel.updateMapEnteredData()
            mainViewModel.changeSearchList(itemId = item.id)
            mainViewModel.setEntDataForInput(itemId = item.id)

            val firstTitleId = mainViewModel.getFirstTitleId()

            if (item.id == firstTitleId) backBtn.visibility = View.GONE
            else backBtn.visibility = View.VISIBLE

            backBtn.setOnClickListener() {
                mainViewModel.changeQuestionNumber(variable = -1)
            }

            nextBtn.setOnClickListener {
                mainViewModel.addEnteredData(itemId = item.id, value = searchView.query.toString())
                if (item.id != mainViewModel.getLastTitleId()) {
                    searchView.setQuery("", false)
                    return@setOnClickListener
                }

                if (item.id == mainViewModel.getLastTitleId()) {
                    mainViewModel.getCoeffs()
                    isLastQuestion = true
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