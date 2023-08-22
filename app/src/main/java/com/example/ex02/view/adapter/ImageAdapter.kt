package com.example.ex02.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ex02.R
import com.example.ex02.bean.ItemImage
import com.example.ex02.databinding.ItemImageBinding
import com.example.ex02.viewmodel.ImageRoomViewModel

class ImageAdapter(private val imageRoomViewModel: ImageRoomViewModel) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    private val _images = MutableLiveData<List<ItemImage>>()
    private val images: LiveData<List<ItemImage>> = _images

    fun setData(list: MutableList<ItemImage>) {
        _images.value = list
        Log.d("ddd", "" + (images.value?.size ?: 0))
        notifyDataSetChanged()

    }

    class ImageViewHolder(binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var imageView = binding.imageView
        var favoriteButton = binding.btnFavorite
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return _images.value?.size ?: 0
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        try {
            Glide.with(holder.imageView.context)
                .load(_images.value?.get(position)?.imageUrl)
                .placeholder(R.drawable.ic_baseline_image_search_24)
                .error(R.drawable.ic_baseline_error_24)
                .into(holder.imageView)
        } catch (e: Exception) {
            Log.d("ddd", e.toString())
        }

        holder.favoriteButton.setOnClickListener {
            images.value?.let { it1 -> addImageToFavoriteList(it1[position]) }
        }
    }

    private fun addImageToFavoriteList(itemImage: ItemImage) {
        imageRoomViewModel.addItemIfNotExists(itemImage)
    }
}