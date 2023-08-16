package com.example.asian.ex01_use_coroutines.ui.component.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.asian.R
import com.example.asian.databinding.ItemUserBinding
import com.example.asian.ex01_use_coroutines.data.model.User
import com.example.usermanagermentmvvm.utils.DiffUtilCallback

class UserAdapter(private val iclickItemUser: (User) -> Unit) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private var mUserList = mutableListOf<User>()
    private lateinit var mItemUserBinding: ItemUserBinding
    private lateinit var mIclickItemUser: IClickItemUser

    interface IClickItemUser {
        fun deleteUser(user: User?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        mItemUserBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_user, parent, false)
        initInterfaceIclick()
        return ViewHolder(mItemUserBinding)
    }

    private fun initInterfaceIclick() {
        mIclickItemUser = object : IClickItemUser {
            override fun deleteUser(user: User?) {
                iclickItemUser(user!!)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = mUserList[position]
        holder.mItemUserBinding?.tvUserId?.text = user.mIdUser.toString()
        holder.mItemUserBinding?.tvUserName?.text = user.mUserName
        holder.mItemUserBinding?.tvUserAge?.text = user.mAge.toString()
        holder.mItemUserBinding?.btnDelete?.setOnClickListener {
            mIclickItemUser.deleteUser(user)
        }
    }

    fun setData(users: MutableList<User>) {
        val diffResult = DiffUtil.calculateDiff(DiffUtilCallback(users, mUserList))
        diffResult.dispatchUpdatesTo(this)
        mUserList.clear()
        mUserList.addAll(users)
    }

    override fun getItemCount(): Int {
        return mUserList.size
    }

    inner class ViewHolder(itemBinding: ItemUserBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        var mItemUserBinding: ItemUserBinding? = null

        init {
            mItemUserBinding = itemBinding
        }
    }
}
