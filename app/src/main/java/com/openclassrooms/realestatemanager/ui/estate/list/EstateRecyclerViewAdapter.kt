package com.openclassrooms.realestatemanager.ui.estate.list

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.EstateListItemBinding

class EstateRecyclerViewAdapter(private val listener: OnItemClickListener) :
    ListAdapter<EstateViewState, EstateRecyclerViewAdapter.ViewHolder>(DIFF_CALLBACK) {

    interface OnItemClickListener {
        fun onClick(estateId: Long)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.estate_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding: EstateListItemBinding = EstateListItemBinding.bind(itemView)

        fun bind(estate: EstateViewState, listener: OnItemClickListener) {
            Glide.with(itemView)
                .load(estate.photo?.uri ?: R.drawable.no_photos)
                .into(binding.estatePicture)
            binding.estatePrice.text = estate.price
            binding.estateType.setText(estate.propertyType.labelResId)
            binding.estateLocation.text = estate.city
            binding.estateListItem.setOnClickListener {
                listener.onClick(estate.id)
            }

            if (estate.selected) {
                binding.estateListItem.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.colorAccent
                    )
                )
                binding.estatePrice.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.colorWhite
                    )
                )
            } else {
                binding.estateListItem.setBackgroundColor(Color.TRANSPARENT)
                binding.estatePrice.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.colorAccent
                    )
                )
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<EstateViewState> =
            object : DiffUtil.ItemCallback<EstateViewState>() {
                override fun areItemsTheSame(
                    oldEstate: EstateViewState,
                    newEstate: EstateViewState
                ): Boolean {
                    return oldEstate.id == newEstate.id
                }

                override fun areContentsTheSame(
                    oldEstate: EstateViewState,
                    newEstate: EstateViewState
                ): Boolean {
                    return oldEstate == newEstate
                }
            }
    }
}