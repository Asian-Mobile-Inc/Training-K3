package com.example.ex02.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.ex02.bean.ItemImage

class ImageDiffCallback : DiffUtil.ItemCallback<ItemImage>() {
    override fun areItemsTheSame(oldItem: ItemImage, newItem: ItemImage): Boolean {
       return oldItem.imageUrl == newItem.imageUrl
    }

    override fun areContentsTheSame(oldItem: ItemImage, newItem: ItemImage): Boolean {
        return oldItem == newItem
    }
}