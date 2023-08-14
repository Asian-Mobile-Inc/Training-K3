package com.example.asian;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.asian.databinding.CardItemBinding;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private final List<Image> mLstImage;
    private Context mContext;
    private final IClickItemUser mIclickItemUser;

    public ImageAdapter(IClickItemUser iClickItemUser) {
        this.mIclickItemUser = iClickItemUser;
        mLstImage = new ArrayList<>();
    }

    public interface IClickItemUser {
        void deleteUser(Image image);
    }

    public void setData(List<Image> lstImage) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ImageDiffCallback(mLstImage, lstImage));
        mLstImage.clear();
        mLstImage.addAll(lstImage);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);
        mContext = parent.getContext();
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Image image = mLstImage.get(position);
        holder.mCardItemBinding.ibDeleteImage.setOnClickListener(view -> mIclickItemUser.deleteUser(image));
        Glide.with(mContext)
                .load(image.getmUrl())
                .skipMemoryCache(true)
                .apply(RequestOptions.centerCropTransform())
                .apply(new RequestOptions()
                        .transform(
                                new RoundedCorners(16),
                                new CenterCrop()))
                .into(holder.mCardItemBinding.imageView);
    }

    @Override
    public int getItemCount() {
        return mLstImage.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardItemBinding mCardItemBinding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCardItemBinding = CardItemBinding.bind(itemView);
        }
    }
}
