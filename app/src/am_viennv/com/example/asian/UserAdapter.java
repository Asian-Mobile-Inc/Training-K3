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

import com.example.asian.beans.User;

import java.util.List;
import java.util.Locale;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    Context mContext;
    List<User> mUserList;
    private final IClickItemUser mIClickItemUser;

    public UserAdapter(IClickItemUser iClickItemUser) {
        this.mIClickItemUser = iClickItemUser;
    }

    public interface IClickItemUser {
        void deleteUser(User user);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(Context context, List<User> userList) {
        this.mContext = context;
        this.mUserList = userList;
        notifyDataSetChanged();
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
            holder.mTextViewUserId.setText(String.format(Locale.getDefault(), "%d", user.getMIdUser()));
            holder.mTextViewUserName.setText(user.getMUserName());
            holder.mTextViewUserAge.setText(String.format(Locale.getDefault(), "%d", user.getMAge()));

            holder.mButtonDelete.setOnClickListener(view -> mIClickItemUser.deleteUser(user));
        }
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
