package com.example.ex01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.ex01.databinding.ActivityMainBinding
import com.example.ex01.model.User
import com.example.ex01.viewmodel.UserViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mUserViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            mUserViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)).get(UserViewModel::class.java)
        } catch (e: Exception) {
           Log.d("ddd", e.toString())
        }

        binding.btnAdd.setOnClickListener(View.OnClickListener {
            addUser()
        })
    }

    private fun addUser() {
        var name = binding.edtUserName.text.toString()
        var age = binding.edtUserAge.text.toString()

        if (checkEmpty(name, age)) {
            val user = User(0, name, age.toInt())
            mUserViewModel.addUser(user)
            Toast.makeText(this, "Successfully Added !", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Name or Age do not empty !", Toast.LENGTH_LONG).show()
        }
    }

    fun checkEmpty(name: String, age: String): Boolean {
        return !(TextUtils.isEmpty(name) || TextUtils.isEmpty(age))
    }
}