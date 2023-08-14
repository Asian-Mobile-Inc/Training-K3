package com.example.asian;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class ImageDiffCallback extends DiffUtil.Callback {

    private final List<Image> mOldImages;
    private final List<Image> mNewImages;

    public ImageDiffCallback(List<Image> oldImages, List<Image> newImages) {
        this.mOldImages = oldImages;
        this.mNewImages = newImages;
    }

    @Override
    public int getOldListSize() {
        return mOldImages.size();
    }

    @Override
    public int getNewListSize() {
        return mNewImages.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldImages.get(oldItemPosition).getmIdImage().equals(mNewImages.get(newItemPosition).getmIdImage());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Image oldImage = mOldImages.get(oldItemPosition);
        Image newImage = mNewImages.get(newItemPosition);
        return oldImage.equals(newImage);
    }
}
