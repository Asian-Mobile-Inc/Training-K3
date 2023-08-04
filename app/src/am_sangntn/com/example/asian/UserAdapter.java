package com.example.asian;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> mListUser;

    public void setData(List<User> mListUser) {
        this.mListUser = mListUser;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = mListUser.get(position);
        if (user == null) {
            return;
        }
        holder.mTvId.setText(user.getId());
        holder.mTvAge.setText(user.getAge());
        holder.mTvName.setText(user.getName());

        holder.mBtnDelete.setOnClickListener(view -> {

        });

    }

    @Override
    public int getItemCount() {
        if (mListUser != null) {
            return mListUser.size();
        }
        return 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvName, mTvAge, mTvId;
        private Button mBtnDelete;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            mTvName = itemView.findViewById(R.id.tvUserName);
            mTvAge = itemView.findViewById(R.id.tvUserAge);
            mBtnDelete = itemView.findViewById(R.id.btnDelete);
            mTvId = itemView.findViewById(R.id.tvUserId);
        }
    }
}
