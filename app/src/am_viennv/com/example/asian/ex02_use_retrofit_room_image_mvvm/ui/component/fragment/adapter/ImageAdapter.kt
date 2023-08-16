package com.example.asian.ex02_use_retrofit_room_image_mvvm.ui.component.fragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.asian.R
import com.example.asian.databinding.CardItemBinding
import com.example.asian.ex02_use_retrofit_room_image_mvvm.data.model.Image
import com.example.asian.ex02_use_retrofit_room_image_mvvm.utils.DiffUtilCallback

class ImageAdapter(private val mContext: Context?, iclickItemUser: IClickItemUser) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    private var mImageList = mutableListOf<Image>()
    private lateinit var mItemBinding: CardItemBinding
    private var mIclickItemUser: IClickItemUser = iclickItemUser

    abstract class IClickItemUser {
        abstract fun updateFavoriteState(isFavorite: Boolean, id: String)
        abstract fun downloadImage(image: Image)
        open fun showFullScreenImage(url: String) = true
    }

    fun setData(images: MutableList<Image>) {
        val diffResult = DiffUtil.calculateDiff(DiffUtilCallback(images, mImageList))
        diffResult.dispatchUpdatesTo(this)
        mImageList.clear()
        mImageList.addAll(images)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.card_item,
            parent,
            false
        )
        return ViewHolder(this.mItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = mImageList[position]
        setImageBackground(
            holder.mItemBinding.ibDownload,
            image.mDownload,
            R.drawable.baseline_file_download_done_24,
            R.drawable.baseline_download_24
        )

        setImageBackground(
            holder.mItemBinding.ibFavorite,
            image.mFavorite,
            R.drawable.baseline_favorite_24,
            R.drawable.baseline_favorite_border_24
        )
        if (mContext != null) {
            Glide.with(mContext)
                .load(image.mUrl)
                .skipMemoryCache(true)
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(30)))
                .into(holder.mItemBinding.imageView)
        }
        holder.mItemBinding.ibFavorite.setOnClickListener {
            image.mFavorite = !image.mFavorite
            notifyItemChanged(position)
            mIclickItemUser.updateFavoriteState(image.mFavorite, image.mIdImage)
        }
        holder.mItemBinding.ibDownload.setOnClickListener {
            notifyItemChanged(position)
            mIclickItemUser.downloadImage(image)
            if (!image.mDownload) {
                image.mDownload = true
            }
        }
        holder.mItemBinding.cvItem.setOnClickListener {
            mIclickItemUser.showFullScreenImage(image.mUrl)
        }
    }

    private fun setImageBackground(
        imageButton: ImageButton,
        isDownloaded: Boolean,
        downloadedResId: Int,
        notDownloadedResId: Int
    ) {
        val drawableRes = if (isDownloaded) {
            downloadedResId
        } else {
            notDownloadedResId
        }
        imageButton.background = ContextCompat.getDrawable(mContext!!, drawableRes)
    }

    override fun getItemCount(): Int {
        return mImageList.size
    }

    class ViewHolder(itemBinding: CardItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        var mItemBinding: CardItemBinding

        init {
            this.mItemBinding = itemBinding
        }
    }
}
