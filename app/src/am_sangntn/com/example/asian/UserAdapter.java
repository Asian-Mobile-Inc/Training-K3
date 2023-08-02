package com.example.asian;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private static final String SQL_QUERY_DELETE_USER = "DELETE FROM USER WHERE Id = ";

    private final Context mContext;

    private List<User> mListUser;

    private final UserHelper mUserHelper;

    public UserAdapter(Context mContext) {
        this.mContext = mContext;
        mUserHelper = new UserHelper(mContext, "User.database", null, 1);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<User> list) {
        this.mListUser = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = mListUser.get(position);

        holder.tvUserId.setText(String.valueOf(user.getId()));
        holder.tvUserName.setText(user.getName());
        holder.tvUserAge.setText(String.valueOf(user.getAge()));

        //set du lieu len view
        holder.btnDelete.setOnClickListener(view -> {
            String idUser = String.valueOf(mListUser.get(holder.getAdapterPosition()).getId());
            mUserHelper.queryData(SQL_QUERY_DELETE_USER + idUser);
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

    public class UserViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvUserId, tvUserName, tvUserAge;
        private final Button btnDelete;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUserId = itemView.findViewById(R.id.tvUserId);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUserAge = itemView.findViewById(R.id.tvUserAge);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            GradientDrawable border = new GradientDrawable();
            border.setStroke(2, mContext.getResources().getColor(R.color.black));
            tvUserName.setBackground(border);

            GradientDrawable border1 = new GradientDrawable();
            border1.setStroke(2, mContext.getResources().getColor(R.color.black));
            tvUserAge.setBackground(border1);
            tvUserId.setBackground(border1);

        }
    }
}
