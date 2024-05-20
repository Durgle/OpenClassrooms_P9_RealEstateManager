package com.openclassrooms.realestatemanager.ui.estate.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.models.Photo
import com.openclassrooms.realestatemanager.databinding.ImageListItemBinding

class CarouselViewAdapter() :
    ListAdapter<Photo, CarouselViewAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding: ImageListItemBinding = ImageListItemBinding.bind(itemView)

        fun bind(photo: Photo) {
            Glide.with(itemView)
                .load(photo.uri)
                .into(binding.carouselImage)

            binding.carouselDescription.text = photo.description

        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Photo> =
            object : DiffUtil.ItemCallback<Photo>() {
                override fun areItemsTheSame(
                    oldPhoto: Photo,
                    newPhoto: Photo
                ): Boolean {
                    return oldPhoto.uri == newPhoto.uri
                }

                override fun areContentsTheSame(
                    oldPhoto: Photo,
                    newPhoto: Photo
                ): Boolean {
                    return oldPhoto == newPhoto
                }
            }
    }
}