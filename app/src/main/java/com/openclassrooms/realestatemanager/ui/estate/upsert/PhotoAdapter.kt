package com.openclassrooms.realestatemanager.ui.estate.upsert

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.models.Photo
import com.openclassrooms.realestatemanager.databinding.UpsertPhotoItemBinding

class PhotoAdapter(
    private val onDeleteClick: (PhotoViewState) -> Unit,
    private val onDescriptionChange: (PhotoViewState, String) -> Unit
) :
    ListAdapter<PhotoViewState, PhotoAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.upsert_photo_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding: UpsertPhotoItemBinding = UpsertPhotoItemBinding.bind(itemView)

        init {
            binding.photoDeleteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onDeleteClick(getItem(position))
                }
            }
            binding.estatePhotoDescInput.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onDescriptionChange(
                            getItem(position),
                            binding.estatePhotoDescInput.text.toString()
                        )
                    }
                }
            }
        }

        fun bind(photo: PhotoViewState) {
            Glide.with(itemView)
                .load(photo.uri)
                .into(binding.estatePhotoView)
            if (photo.description != binding.estatePhotoDescInput.text.toString()) {
                binding.estatePhotoDescInput.setText(photo.description)
            }
            binding.estatePhotoDescLayout.error = photo.error
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<PhotoViewState> =
            object : DiffUtil.ItemCallback<PhotoViewState>() {
                override fun areItemsTheSame(
                    oldPhoto: PhotoViewState,
                    newPhoto: PhotoViewState
                ): Boolean {
                    return oldPhoto.uri == newPhoto.uri
                }

                override fun areContentsTheSame(
                    oldPhoto: PhotoViewState,
                    newPhoto: PhotoViewState
                ): Boolean {
                    return oldPhoto == newPhoto
                }
            }
    }
}