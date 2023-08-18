package com.example.ex02.view.adapter

import android.content.ClipData.Item
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ex02.R
import com.example.ex02.bean.ItemImage
import com.example.ex02.databinding.ItemImageBinding
import com.example.ex02.viewmodel.ImageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class ImageAdapter() :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    private val _images = MutableLiveData<List<ItemImage>>()
    private val images: LiveData<List<ItemImage>> = _images

    fun setData(list: MutableList<ItemImage>) {
        // _images.postValue(null)
        _images.value = list
        Log.d("ddd", "" + (images.value?.size ?: 0))
        notifyDataSetChanged()

    }

    class ImageViewHolder(binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var imageView = binding.imageView
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
        return _images.value?.size ?: 0
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        Glide.with(holder.imageView.context)
            .load(_images.value?.get(position)?.imageUrl)
            .placeholder(R.drawable.ic_baseline_image_search_24)
            .error(R.drawable.ic_baseline_error_24)
            .into(holder.imageView)
        // _images.value?.get(position)?.let { loadImageFromUrl(it.imageUrl, holder.imageView) }
//        holder.favoriteButton.setOnClickListener {
//            images.value?.get(position)?.let { it1 -> addImageToFavoriteList(it1) }
//        }
    }

//    private fun addImageToFavoriteList(itemImage: ItemImage) {
//        imageViewModel.addImageToFavoriteList(itemImage)
//    }

    private fun loadImageFromUrl(urlString: String, imageView: ImageView) {
        var bitmap: Bitmap
        CoroutineScope(Dispatchers.IO).launch {
            val url = URL(urlString)
            try {
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val inputStream = connection.inputStream
                bitmap = BitmapFactory.decodeStream(inputStream)
                withContext(Dispatchers.Main) {
                    imageView.setImageBitmap(bitmap)
                }
                inputStream.close()
            } catch (e: Exception) {
                Log.d("ddd", e.toString())
            }
        }
        // return bitmap
    }
}