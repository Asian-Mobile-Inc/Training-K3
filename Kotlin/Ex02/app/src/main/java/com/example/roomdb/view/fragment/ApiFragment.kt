package com.example.roomdb.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.roomdb.R
import com.example.roomdb.databinding.FragmentApiBinding

class ApiFragment : Fragment() {
    private lateinit var binding: FragmentApiBinding
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentApiBinding.inflate(inflater, container, false)
        binding.btnOpenFavoriteFragment.setOnClickListener {
            navController.navigate(R.id.action_apiFragment_to_favoriteFragment2)
        }
        return binding.root
    }
}