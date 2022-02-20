package com.ands.sravniruhackathon.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ands.sravniruhackathon.R
import com.ands.sravniruhackathon.databinding.CoeffsCardviewItemBinding
import com.ands.sravniruhackathon.domain.entities.Coeffs

class CoeffsAdapter : ListAdapter<Coeffs, CoeffsAdapter.ItemHolder>(ItemComparator()) {

    class ItemComparator : DiffUtil.ItemCallback<Coeffs>() {
        override fun areItemsTheSame(oldItem: Coeffs, newItem: Coeffs): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Coeffs, newItem: Coeffs): Boolean {
            return oldItem == newItem
        }

    }

    class ItemHolder(private val binding: CoeffsCardviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(coeffs: Coeffs) = with(binding) {

            value1.text = coeffs.value.toString()
            name1.text = binding.root.context.getString(
                R.string.CoeffsNamePlaceholder,
                coeffs.name.toString()
            )
            detailText1.text = coeffs.detailText.toString()
            title1.text = coeffs.title.toString()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            CoeffsCardviewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(getItem(position))
    }

}