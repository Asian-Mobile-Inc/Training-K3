package com.example.asian;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context mContext;
    private List<User> mListUser;

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<User> mListUser) {
        this.mListUser = mListUser;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        mContext = parent.getContext();
        return new UserViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = mListUser.get(position);
        if (user == null) {
            return;
        }
        holder.mTvId.setText(String.valueOf(user.getId()));
        holder.mTvAge.setText(String.valueOf(user.getAge()));
        holder.mTvName.setText(user.getName());

        holder.mBtnDelete.setOnClickListener(view -> {
            UserDatabase.getInstance(mContext).userDao().deleteUser(user);
            mListUser.remove(holder.getAdapterPosition());
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        if (mListUser != null) {
            return mListUser.size();
        }
        return 0;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTvName, mTvAge, mTvId;
        private final Button mBtnDelete;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            mTvName = itemView.findViewById(R.id.tvUserName);
            mTvAge = itemView.findViewById(R.id.tvUserAge);
            mBtnDelete = itemView.findViewById(R.id.btnDelete);
            mTvId = itemView.findViewById(R.id.tvUserId);
        }
    }
}
