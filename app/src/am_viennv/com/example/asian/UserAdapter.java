package com.example.asian;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asian.beans.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    Context mContext;
    List<User> mUserList;
    List<User> mUserListCopy;

    public UserAdapter(Context context, List<User> userList) {
        mContext = context;
        mUserList = userList;
        mUserListCopy = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mUserList != null && mUserList.size() != 0) {
            User user = mUserList.get(position);
            holder.mTextViewUserId.setText(String.format(Locale.getDefault(), "%d", user.getmIdUser()));
            holder.mTextViewUserName.setText(user.getmUserName());
            holder.mTextViewUserAge.setText(String.format(Locale.getDefault(), "%d", user.getmAge()));

            holder.mButtonDelete.setOnClickListener(view -> {
                try (DBHelper DB = new DBHelper(mContext)) {
                    DB.deleteOneUser(user.getmIdUser());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mUserList.remove(position);
                mUserListCopy.remove(position);
                notifyItemRemoved(position);
            });
        }
    }

    public void addUser(User user) {
        mUserList.add(user);
        notifyItemInserted(mUserList.size());
    }

    @SuppressLint("NotifyDataSetChanged")
    public void searchUserByName(String name) {
        mUserList = new ArrayList<>(mUserListCopy);
        List<User> newUsersList = new ArrayList<>();
        if (name.equals("")) {
            mUserList = ((MainActivity) mContext).getList();
            notifyDataSetChanged();
            return;
        }
        for (User user : mUserList) {
            if (user.getmUserName().toLowerCase().contains(name.toLowerCase())) {
                newUsersList.add(user);
            }
        }
        mUserList.clear();
        mUserList = newUsersList;
        notifyDataSetChanged();
    }

    public void deleteAll() {
        int itemCount = mUserList.size();
        mUserList.clear();
        notifyItemRangeRemoved(0, itemCount);
    }

    public Integer findIdNewUser() {
        int newId = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Collections.sort(mUserList, Comparator.comparingInt(User::getmIdUser));
        }
        for (int i = 0; i < mUserList.size(); i++) {
            if (newId != mUserList.get(i).getmIdUser()) {
                return newId;
            }
            newId++;
        }
        return newId;
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTextViewUserId, mTextViewUserName, mTextViewUserAge;
        private final Button mButtonDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewUserId = itemView.findViewById(R.id.tvUserId);
            mTextViewUserName = itemView.findViewById(R.id.tvUserName);
            mTextViewUserAge = itemView.findViewById(R.id.tvUserAge);
            mButtonDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
