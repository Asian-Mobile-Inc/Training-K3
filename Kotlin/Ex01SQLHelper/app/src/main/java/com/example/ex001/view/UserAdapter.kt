package com.example.ex001.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.ex001.databinding.ItemUserBinding
import com.example.ex001.model.User
import com.example.ex001.viewmodel.UserViewModel

class UserAdapter(private val userViewModel: UserViewModel) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var users: LiveData<List<User>> = MutableLiveData()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: LiveData<List<User>>) {
        users = list
        notifyDataSetChanged()
    }

    class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {

        var deleteButton = binding.btnDelete
        fun bind(user: User) {
            binding.tvUserId.text = user.id.toString()
            binding.tvUserName.text = user.name
            binding.tvUserAge.text = user.age.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return users.value?.size ?: 0
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        users.value?.let { holder.bind(it[position]) }
        holder.deleteButton.setOnClickListener {
            users.value?.get(position)?.let { it1 -> deleteUser(it1.id) }
        }


    }

    private fun deleteUser(userId: Int) {
        userViewModel.deleteUser(userId)
        notifyDataSetChanged()
    }
}