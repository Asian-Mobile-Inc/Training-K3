package com.example.ex02.view.adapter

import android.content.ClipData.Item
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.ex02.bean.ItemImage
import com.example.ex02.databinding.ItemImageBinding
import com.example.ex02.viewmodel.ImageViewModel
class ImageAdapter() :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    private val _images = MutableLiveData<List<ItemImage>>()
    private val images: LiveData<List<ItemImage>> = _images

    fun setData(list: MutableList<ItemImage>) {
        Log.d("ddd", "" + list.size)
        _images.postValue(list)
        //notifyDataSetChanged()

    }

    class ImageViewHolder(binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var favoriteButton = binding.btnFavorite
        fun bind(itemImage: ItemImage) {
            //binding.imageView.setImageBitmap()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return images.value?.size ?: 0
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        //images.value?.let { holder.bind(it[position]) }
        holder.favoriteButton.text = images.value?.get(position)?.name ?: "d"
//        holder.favoriteButton.setOnClickListener {
//            images.value?.get(position)?.let { it1 -> addImageToFavoriteList(it1) }
//        }
    }

//    private fun addImageToFavoriteList(itemImage: ItemImage) {
//        imageViewModel.addImageToFavoriteList(itemImage)
//    }
}