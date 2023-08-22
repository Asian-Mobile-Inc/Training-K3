package com.example.ex02.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ex02.bean.ItemImage
import com.example.ex02.databinding.FragmentApiBinding
import com.example.ex02.model.api.ApiRepository
import com.example.ex02.model.api.ApiService
import com.example.ex02.viewmodel.ImageApiViewModel
import com.example.ex02.viewmodel.ImageApiViewModelFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.recyclerview.widget.RecyclerView
import com.example.ex02.view.adapter.ImageListAdapter
import com.example.ex02.viewmodel.ImageRoomViewModel

class ApiFragment : Fragment() {
    private lateinit var imageRoomViewModel: ImageRoomViewModel
    private lateinit var binding: FragmentApiBinding
    private lateinit var imageAdapter: ImageListAdapter
    private lateinit var imageApiViewModel: ImageApiViewModel
    private lateinit var rcv: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        imageRoomViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(ImageRoomViewModel::class.java)

        binding = FragmentApiBinding.inflate(inflater, container, false)

        rcv = binding.recyclerView

            val retrofit = Retrofit.Builder().baseUrl("https://api.gyazo.com/api/")
                .addConverterFactory(GsonConverterFactory.create()).build()

            val apiService = retrofit.create(ApiService::class.java)
            val repository = ApiRepository(apiService)

            imageApiViewModel = ViewModelProvider(
                this, ImageApiViewModelFactory(repository)
            ).get(ImageApiViewModel::class.java)
            val accessToken = "XZiQLn5Xu3cjUTKYpQKOUsYHweBoxKJVOgCfneoY1Yo"

            imageApiViewModel.fetchImages(accessToken)
            imageApiViewModel.images.observe(this) { images ->
                val list: MutableList<ItemImage> = mutableListOf()
                for (image in images) {
                    if (image.metadata.title != null) {
                        list.add(ItemImage(null, image.url, image.metadata.title))
                    } else {
                        list.add(ItemImage(null, image.url, "null"))
                    }
                }
                //imageAdapter.setData(images)
                imageAdapter.submitList(list)
            }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gridLayoutManager = GridLayoutManager(requireContext(), 3)
        rcv.layoutManager = gridLayoutManager
        imageAdapter = ImageListAdapter(imageRoomViewModel)
        rcv.adapter = imageAdapter
    }
}