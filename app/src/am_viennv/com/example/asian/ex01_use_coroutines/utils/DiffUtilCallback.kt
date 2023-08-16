package com.example.usermanagermentmvvm.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.asian.ex01_use_coroutines.data.model.User


class DiffUtilCallback(
    private var mNewList: MutableList<User>,
    private var mOldList: MutableList<User>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return mOldList.size
    }

    override fun getNewListSize(): Int {
        return mNewList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mNewList[newItemPosition].mIdUser == mOldList[oldItemPosition].mIdUser
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mNewList[newItemPosition].mUserName.compareTo(mOldList[oldItemPosition].mUserName) == 0
    }
}
