package com.example.roomdb.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdb.databinding.ItemUserBinding
import com.example.roomdb.model.User
import com.example.roomdb.viewmodel.UserViewModel

class UserAdapter(private val userViewModel: UserViewModel) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var users = MutableLiveData<List<User>>()


    fun setData(list: List<User>) {
        users.postValue(list)
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
            users.value?.let { it1 -> deleteUser(it1[position]) }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteUser(user: User) {
        userViewModel.deleteUser(user)
        notifyDataSetChanged()
    }
}