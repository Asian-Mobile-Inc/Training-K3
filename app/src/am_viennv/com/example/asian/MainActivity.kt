package com.example.asian

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.asian.databinding.ActivityMainBinding
import com.example.asian.ex01_use_coroutines.ui.component.home.Ex01UserManagementActivity
import com.example.asian.ex02_use_retrofit_room_image_mvvm.ui.component.main_activity.Ex02ImageViewActivity

class MainActivity : AppCompatActivity() {
    private lateinit var mActivityBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVariable()
        handleClick()
    }

    private fun initVariable() {
        mActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    private fun handleClick() {
        mActivityBinding.btnEx01.setOnClickListener {
            intentToActivity(Ex01UserManagementActivity::class.java)
        }
        mActivityBinding.btnEx02.setOnClickListener {
            intentToActivity(Ex02ImageViewActivity::class.java)
        }
    }

    private fun intentToActivity(classTo: Class<out Activity>) {
        val intent = Intent(this, classTo)
        startActivity(intent)
    }
}
