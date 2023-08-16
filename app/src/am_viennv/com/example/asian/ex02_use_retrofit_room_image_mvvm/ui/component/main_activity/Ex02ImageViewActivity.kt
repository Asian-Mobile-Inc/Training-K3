package com.example.asian.ex02_use_retrofit_room_image_mvvm.ui.component.main_activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.asian.R
import com.example.asian.databinding.ActivityEx02ImageBinding

class Ex02ImageViewActivity : AppCompatActivity() {

    private lateinit var mActivityMainBinding: ActivityEx02ImageBinding
    private lateinit var mNavcontroller: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVariable()
        handleClick()
    }

    private fun handleClick() {
        mActivityMainBinding.tbApp.setOnMenuItemClickListener {
            mNavcontroller =
                Navigation.findNavController(this, R.id.fragmentContainerView)
            mNavcontroller.navigate(R.id.action_homeFragment_to_favoriteFragment)
            true
        }
    }

    private fun initVariable() {
        mActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_ex02_image)
    }
}
