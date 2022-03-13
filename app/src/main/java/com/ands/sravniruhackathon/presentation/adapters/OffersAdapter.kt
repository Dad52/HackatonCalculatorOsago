package com.ands.sravniruhackathon.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load
import com.ands.sravniruhackathon.R
import com.ands.sravniruhackathon.databinding.OffersItemBinding
import com.ands.sravniruhackathon.domain.entities.Offers
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class OffersAdapter(
    private val itemClick: ItemClick,
    private val imageLoader: ImageLoader
) : ListAdapter<Offers, OffersAdapter.ItemHolder>(ItemComparator()) {

    class ItemComparator : DiffUtil.ItemCallback<Offers>() {
        override fun areItemsTheSame(oldItem: Offers, newItem: Offers): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Offers, newItem: Offers): Boolean {
            return oldItem == newItem
        }

    }

    class ItemHolder(private val binding: OffersItemBinding, private val imageLoader: ImageLoader) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(offerInfo: Offers, itemClick: ItemClick) = with(binding) {

            companyIcon.contentDescription = offerInfo.branding.iconTitle
            companyIcon.load(offerInfo.branding.bankLogoUrlSVG, imageLoader = imageLoader) {
                size(100)
                scale(coil.size.Scale.FILL)
                precision(coil.size.Precision.EXACT)
                listener(
                    onError = { _, _ ->

                        companyIcon.setImageDrawable(null)

                        bgCompanyIcon.apply {
                            isVisible = true
                            setCardBackgroundColor(getColorInt(offerInfo.branding.backgroundColor))
                        }

                        imageDescription.apply {
                            text = offerInfo.branding.iconTitle
                            setTextColor(getColorInt(offerInfo.branding.fontColor))
                        }

                    }
                )
            }
            companyIcon.contentDescription = offerInfo.branding.iconTitle
            companyName.text = offerInfo.name
            companyRating.text = offerInfo.rating.toString()
            offerPrice.text = binding.root.context.getString(
                R.string.PricePlaceholder,
                thousandSeparator(offerInfo.price.toString().toDouble())
            )

            itemView.setOnClickListener {
                itemClick.sendResultBack(offerInfo)
            }

        }

        private fun thousandSeparator(str: Double): String {

            val newDecimalFormatSymbols = DecimalFormatSymbols(Locale.forLanguageTag("ru"))

            val myFormatter = DecimalFormat("###,###", newDecimalFormatSymbols)
            return myFormatter.format(str)
        }

        private fun getColorInt(colorCode: String): Int {
            return binding.root.context.getString(R.string.colorLattice, colorCode).toColorInt()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            OffersItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), imageLoader = imageLoader
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(getItem(position), itemClick = itemClick)
    }

    interface ItemClick {
        fun sendResultBack(offerInfo: Offers)
    }


}