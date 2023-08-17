package com.example.ex02.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ex02.R
import com.example.ex02.bean.ItemImage
import com.example.ex02.databinding.FragmentApiBinding
import com.example.ex02.model.api.ApiRepository
import com.example.ex02.model.api.ApiService
import com.example.ex02.view.adapter.ImageAdapter
import com.example.ex02.viewmodel.ImageApiViewModel
import com.example.ex02.viewmodel.ImageApiViewModelFactory
import com.example.ex02.viewmodel.ImageViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ApiFragment : Fragment() {
    private lateinit var binding: FragmentApiBinding
   // private lateinit var imageViewModel: ImageViewModel
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var imageApiViewModel: ImageApiViewModel
    private lateinit var rcv: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentApiBinding.inflate(inflater, container, false)

        rcv = binding.recyclerView // Sử dụng binding để tìm RecyclerView

        val list: MutableList<ItemImage> = mutableListOf()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.gyazo.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val repository = ApiRepository(apiService)

        imageApiViewModel = ViewModelProvider(
            this,
            ImageApiViewModelFactory(repository)
        ).get(ImageApiViewModel::class.java)
        val accessToken = "XZiQLn5Xu3cjUTKYpQKOUsYHweBoxKJVOgCfneoY1Yo"
        imageApiViewModel.fetchImages(accessToken)
        Log.d("ddd", "ddad")
        imageApiViewModel.images.observe(this, Observer { images ->
            Log.d("ddd", "ddd")
            for (image in images) {

                if (image.metadata.title != null) {
                    Log.d("ddd", image.metadata.title)
                    list.add(ItemImage(image.url, image.metadata.title))
                } else {
                    list.add(ItemImage(image.url, "null"))
                }
            }
            //  imageAdapter.setData(list)
        })


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gridLayoutManager = GridLayoutManager(requireContext(), 3)
        rcv.layoutManager = gridLayoutManager
        imageAdapter = ImageAdapter()
       // getListItemImage()
        rcv.adapter = imageAdapter

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //imageViewModel = ViewModelProvider(this, ImageApiViewModelFactory(repository)).get(ImageViewModel::class.java)

    }


    private fun getListItemImage() {



        try {




        } catch (e: Exception) {
            Log.d("ddd", e.toString())
        }
    }

}