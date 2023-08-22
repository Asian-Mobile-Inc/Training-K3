package com.example.ex02.view.ex01

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ex02.databinding.ActivityEx01UserBinding
import com.example.ex02.model.ex01.User
import com.example.ex02.viewmodel.ex01.UserViewModel

class UserActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var binding: ActivityEx01UserBinding
    private lateinit var userAdapter: UserAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEx01UserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)).get(
            UserViewModel::class.java)

        binding.rcvListUsers.layoutManager = LinearLayoutManager(this)
        userAdapter = UserAdapter(userViewModel)
        binding.rcvListUsers.adapter = userAdapter

        binding.btnAdd.setOnClickListener {
            try {
                addUser()
            } catch (e: Exception) {
                showToast("Failed Added !")
                Log.d("ddd", e.toString())
            }
        }

        binding.btnDeleteAll.setOnClickListener {
            try {
                userViewModel.deleteAllUsers()
                showToast("Delete all Users successfully !")
            } catch (e: Exception) {
                showToast("Delete all Users failed !")
                Log.d("ddd", e.toString())
            }
        }

        userViewModel.getAllUsers().observe(
            this,
        ) { list ->
            userAdapter.setData(list)
        }
    }

    private fun addUser() {
        val name = binding.edtUserName.text.toString()
        val age = binding.edtUserAge.text.toString()
        if (checkEmpty(name, age)) {
            val user = User(null, name, age.toInt())
            userViewModel.addUser(user)
            showToast("Successfully Added !")
        } else {
            showToast("Please enter name and age !")
        }
    }

    private fun checkEmpty(name: String, age: String): Boolean {
        return !((name == "") || (age == ""))
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}