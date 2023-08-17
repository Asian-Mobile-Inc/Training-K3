package com.example.ex02

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ex02.bean.ItemImage
import com.example.ex02.databinding.ActivityMainBinding
import com.example.ex02.model.api.ApiRepository
import com.example.ex02.model.api.ApiService
import com.example.ex02.view.adapter.ImageAdapter
import com.example.ex02.viewmodel.ImageApiViewModel
import com.example.ex02.viewmodel.ImageApiViewModelFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var imageApiViewModel: ImageApiViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomMenu.setupWithNavController(navController)

    }


}