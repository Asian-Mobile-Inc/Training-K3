package com.example.kotlin.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlin.databinding.ActivityMainBinding
import com.example.kotlin.model.User
import com.example.kotlin.ui.viewmodel.UserViewModel
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mUserViewModel: UserViewModel

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        try {
            mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
            //val viewModedl = ViewModelProvider(this)[UserViewModel::class.java]
        } catch (e: Exception) {
            Log.d("ddd", e.toString())
        }

//        var userAdapter = UserAdapter(mUserViewModel)
//        binding.rcvListUsers.layoutManager = LinearLayoutManager(this)
//        binding.rcvListUsers.adapter = userAdapter

        try {
            binding.btnAdd.setOnClickListener(View.OnClickListener {
              // insertUserToDatabase()
            })

        } catch (e: Exception) {
            Log.d("ddd", e.toString())
        }


    }
    private fun insertUserToDatabase() {
        val name = binding.edtUserName.text.toString()
        val age = binding.edtUserAge.text.toString()

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