package com.example.asian.ex02_use_retrofit_room_image_mvvm.ui.component.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.asian.R
import com.example.asian.databinding.FragmentImageFullScreenBinding

class ShowImageFragment : Fragment() {
    private lateinit var mBinding: FragmentImageFullScreenBinding
    private lateinit var mWindowInsetsController: WindowInsetsControllerCompat
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.fragment_image_full_screen,
            container,
            false
        )
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVariable()
        hiddenToolBar()
        setImageToImageView()
        hiddenSystemUI()
    }

    private fun initVariable() {
        val window = requireActivity().window
        mWindowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
    }

    private fun setImageToImageView() {
        val mUrlImage = arguments?.getString("Url_Image")
        Glide.with(this)
            .load(mUrlImage)
            .centerCrop()
            .into(mBinding.ivShowImage)
    }

    private fun hiddenToolBar() {
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.tbApp)
        toolbar.visibility = View.GONE
    }

    private fun hiddenSystemUI() {
        mWindowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        mWindowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }
}
