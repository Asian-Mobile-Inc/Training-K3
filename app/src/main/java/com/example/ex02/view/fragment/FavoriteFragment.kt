package com.example.ex02.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ex02.databinding.FragmentFavoriteBinding
import com.example.ex02.view.adapter.ImageFavoriteListAdapter
import com.example.ex02.viewmodel.ImageRoomViewModel

class FavoriteFragment : Fragment() {

    private lateinit var imageRoomViewModel: ImageRoomViewModel
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var imageAdapter: ImageFavoriteListAdapter
    private lateinit var rcv: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        imageRoomViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(ImageRoomViewModel::class.java)

        binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        rcv = binding.recyclerView

        imageRoomViewModel.getAllImage().observe(this) { list ->
            imageAdapter.submitList(list)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gridLayoutManager = GridLayoutManager(requireContext(), 3)
        rcv.layoutManager = gridLayoutManager
        imageAdapter = ImageFavoriteListAdapter(imageRoomViewModel)
        rcv.adapter = imageAdapter
    }
}