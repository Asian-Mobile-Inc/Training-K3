package com.example.asian.adapters;

import androidx.recyclerview.widget.DiffUtil;

import com.example.asian.beans.User;

import java.util.List;

public class DiffUtilsCallback extends DiffUtil.Callback {

    private final List<User> mOldUsers;
    private final List<User> mNewUsers;

    public DiffUtilsCallback(List<User> oldUsers, List<User> newUsers) {
        this.mOldUsers = oldUsers;
        this.mNewUsers = newUsers;
    }

    @Override
    public int getOldListSize() {
        return mOldUsers.size();
    }

    @Override
    public int getNewListSize() {
        return mNewUsers.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldUsers.get(oldItemPosition).getmIdUser().equals(mNewUsers.get(newItemPosition).getmIdUser());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        User oldUser = mOldUsers.get(oldItemPosition);
        User newUser = mNewUsers.get(newItemPosition);
        return oldUser.equals(newUser);
    }
}
