package com.example.kotlin.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater

import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin.databinding.ItemUserBinding
import com.example.kotlin.model.User
import com.example.kotlin.ui.viewmodel.UserViewModel

class UserAdapter(private val userViewModel: UserViewModel) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var userList: List<User> = emptyList()

    fun updateItems(newUserList: List<User>) {
        userList = newUserList
        notifyDataSetChanged()
    }
    class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User, clickListener: OnClickListener) {
            binding.tvUserId.text = user.id.toString()
            binding.tvUserName.text = user.name
            binding.tvUserAge.text = user.age.toString()

            binding.btnDelete.setOnClickListener(clickListener)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 0//userViewModel.listUser.value?.size ?: 0
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        /*val item = userViewModel.getUser(position)

        if (item != null) {
            holder.bind(item) {
                userViewModel.removeUser(position)
            }
        }*/
    }

}