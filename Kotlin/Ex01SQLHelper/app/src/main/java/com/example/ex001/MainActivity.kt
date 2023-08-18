package com.example.ex001

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ex001.databinding.ActivityMainBinding
import com.example.ex001.model.User
import com.example.ex001.view.UserAdapter
import com.example.ex001.viewmodel.UserViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userAdapter: UserAdapter
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rcvListUsers.layoutManager = LinearLayoutManager(this)

        try {
            userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        } catch (e: Exception) {
            Log.d("ddd", e.toString())
        }

        userAdapter = UserAdapter(userViewModel)
        binding.rcvListUsers.adapter = userAdapter

        userViewModel.getAllUsers().observe(
            this,
        ) {
            userAdapter.setData(userViewModel.getAllUsers())
        }

        binding.btnAdd.setOnClickListener {
            try {
                addUser()
            } catch (e: Exception) {
                Log.d("ddd", e.toString())
                showToast("Failed Added !")
            }
        }

        binding.btnShowAll.setOnClickListener {
            userAdapter.setData(userViewModel.getAllUsers())
        }

        binding.btnDeleteAll.setOnClickListener {
            if (userViewModel.deleteAllUsers() > 0) {
                showToast("Successfully Deleted !")
                userAdapter.setData(userViewModel.getAllUsers())
            } else {
                showToast("Failed Deleted !")
            }
        }

    }

    private fun addUser() {
        val name = binding.edtUserName.text.toString()
        val age = binding.edtUserAge.text.toString()
        val user = User(0, name, age.toInt())
        if (checkEmpty(name, age)) {
            userViewModel.addUser(user)
            showToast("Successfully Added !")
        } else {
            showToast("Name or Age do not Empty !")
        }

    }

    private fun checkEmpty(name: String, age: String): Boolean {
        return !(name == "" || age == "")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}