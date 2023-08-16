package com.example.asian.ex02_use_retrofit_room_image_mvvm.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.asian.ex02_use_retrofit_room_image_mvvm.data.model.Image

class DiffUtilCallback(
    private var mNewList: MutableList<Image>,
    private var mOldList: MutableList<Image>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return mOldList.size
    }

    override fun getNewListSize(): Int {
        return mNewList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mNewList[newItemPosition].mIdImage == mOldList[oldItemPosition].mIdImage
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mNewList[newItemPosition].mName?.compareTo(mOldList[oldItemPosition].mName!!) == 0
    }
}
