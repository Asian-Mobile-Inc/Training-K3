package com.example.ex02.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ex02.R
import com.example.ex02.bean.ItemImage
import com.example.ex02.databinding.ItemImageBinding
import com.example.ex02.viewmodel.ImageRoomViewModel

class ImageListAdapter(private val imageRoomViewModel: ImageRoomViewModel) : ListAdapter<ItemImage, ImageListAdapter.ImageViewHolder>(ImageDiffCallback()) {

    class ImageViewHolder(binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root) {
        var imageView = binding.imageView
        var favoriteButton = binding.btnFavorite
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = getItem(position)
        try {
            Glide.with(holder.imageView.context)
                .load(image.imageUrl)
                .placeholder(R.drawable.ic_baseline_image_search_24)
                .error(R.drawable.ic_baseline_error_24)
                .into(holder.imageView)
        } catch (e: Exception) {
            Log.d("ddd", e.toString())
        }

        holder.favoriteButton.setOnClickListener {
            addImageToFavoriteList(image)
        }
    }

    private fun addImageToFavoriteList(itemImage: ItemImage) {
        imageRoomViewModel.addItemIfNotExists(itemImage)
    }
}