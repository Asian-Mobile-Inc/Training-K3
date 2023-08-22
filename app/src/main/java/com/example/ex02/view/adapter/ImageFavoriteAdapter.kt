package com.example.ex02.view.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ex02.R
import com.example.ex02.bean.ItemImage
import com.example.ex02.databinding.ItemImageBinding
import com.example.ex02.viewmodel.ImageRoomViewModel

class ImageFavoriteAdapter(private val imageRoomViewModel: ImageRoomViewModel) :
    RecyclerView.Adapter<ImageFavoriteAdapter.ImageViewHolder>() {

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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        try {
            Glide.with(holder.imageView.context)
                .load(_images.value?.get(position)?.imageUrl)
                .placeholder(R.drawable.ic_baseline_image_search_24)
                .error(R.drawable.ic_baseline_error_24)
                .into(holder.imageView)
        } catch (e: Exception) {
            Log.d("ddd", e.toString())
            Toast.makeText(holder.imageView.context, "failed", Toast.LENGTH_SHORT).show()
        }

        holder.favoriteButton.text = "Delete"

        holder.favoriteButton.setOnClickListener {
            _images.value?.let { it1 -> deleteImageInFavoriteList(it1.get(position)) }
        }
    }

    private fun deleteImageInFavoriteList(itemImage: ItemImage) {
        imageRoomViewModel.deleteImage(itemImage)
    }

}