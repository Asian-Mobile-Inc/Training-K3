package com.example.asian.ex02_use_retrofit_room_image_mvvm.ui.component.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.asian.R
import com.example.asian.databinding.FragmentFavoriteBinding
import com.example.asian.ex02_use_retrofit_room_image_mvvm.data.local.ImageDatabase
import com.example.asian.ex02_use_retrofit_room_image_mvvm.data.model.Image
import com.example.asian.ex02_use_retrofit_room_image_mvvm.data.remote.ApiService
import com.example.asian.ex02_use_retrofit_room_image_mvvm.data.remote.RetrofitClient
import com.example.asian.ex02_use_retrofit_room_image_mvvm.data.repository.ImageRepository
import com.example.asian.ex02_use_retrofit_room_image_mvvm.ui.component.fragment.adapter.ImageAdapter
import com.example.asian.ex02_use_retrofit_room_image_mvvm.ui.component.launch.ImageViewModel
import com.example.asian.ex02_use_retrofit_room_image_mvvm.utils.DownloadImageUtils
import com.example.asian.ex02_use_retrofit_room_image_mvvm.utils.HandleViewData
import com.example.asian.ex02_use_retrofit_room_image_mvvm.utils.NetworkUtils

class FavoriteFragment : Fragment() {
    private lateinit var mViewModel: ImageViewModel
    private lateinit var mImageAdapter: ImageAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mFavoriteFragmentBinding: FragmentFavoriteBinding
    private lateinit var mLayoutManager: GridLayoutManager
    private lateinit var mImageDatabase: ImageDatabase
    private lateinit var mListPhotoFavorite: MutableList<Image>
    private lateinit var mNavcontroller: NavController
    private lateinit var mWindowInsetsController: WindowInsetsControllerCompat
    private lateinit var mHandleViewData: HandleViewData
    private lateinit var mDownloadImageUtils: DownloadImageUtils
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mFavoriteFragmentBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.fragment_favorite,
            container,
            false
        )
        return mFavoriteFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVariable()
        mHandleViewData.showToolBar()
        setTitle()
        initRecyclerView()
        loadDataForRecyclerView()
        mHandleViewData.showErrorState()
        mHandleViewData.progessloadingData()
    }

    private fun setTitle() {
        val toolbar = activity?.findViewById<Toolbar>(R.id.tbApp)
        toolbar?.title = "Favorite Fragment"
        toolbar?.menu?.findItem(R.id.favorite)?.isVisible = false
    }

    private fun loadDataForRecyclerView() {
        if (NetworkUtils.isOnline(requireContext())) {
            mViewModel.loadAllImagesFromAPI()
        } else {
            mViewModel.getAllImageFromRoom()
        }
        mViewModel.mImages.observe(viewLifecycleOwner) { images ->
            mListPhotoFavorite =
                images.filter { it.mFavorite || it.mDownload } as MutableList<Image>
            mImageAdapter.setData(mListPhotoFavorite)
        }
    }

    private fun initRecyclerView() {
        mRecyclerView = mFavoriteFragmentBinding.rvImage
        mLayoutManager = GridLayoutManager(context, 3)
        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.adapter = mImageAdapter
    }

    private fun initVariable() {
        mDownloadImageUtils = DownloadImageUtils(requireContext(), requireActivity())
        val window = requireActivity().window
        mWindowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
        mNavcontroller = Navigation.findNavController(requireView())
        mImageDatabase = context?.let { ImageDatabase.getInstance(it) }!!
        mViewModel = ImageViewModel(
            ImageRepository(
                RetrofitClient.mInstance.create(ApiService::class.java)
            ),
            mImageDatabase,
            mDownloadImageUtils
        )
        mHandleViewData = HandleViewData(requireActivity(), mViewModel, viewLifecycleOwner)
        mImageAdapter = ImageAdapter(requireContext(), object : ImageAdapter.IClickItemUser() {
            override fun updateFavoriteState(isFavorite: Boolean, id: String) {
                mViewModel.updateFavoriteState(isFavorite, id)
            }

            override fun downloadImage(image: Image) {
                mViewModel.downloadImage(image)
            }

            override fun showFullScreenImage(url: String): Boolean {
                val bundle = Bundle()
                bundle.putString("Url_Image", url)
                mNavcontroller.navigate(
                    R.id.action_favoriteFragment_to_imageFullScreenFragment,
                    bundle
                )
                return true
            }
        })
    }
}
