package com.example.asian.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asian.R;
import com.example.asian.adapter.CustomAdapter;

import java.util.ArrayList;

public class FragmentTab2 extends Fragment {

    private RecyclerView mRecyclerView;

    private CustomAdapter mCustomAdapter;

    private final ArrayList<String> mListData;
    private View mRootView;

    public FragmentTab2(ArrayList<String> mListData) {
        this.mListData = mListData;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.recycler_view_list_item, container, false);
        initView();
        createRecyclerView();
        return mRootView;
    }

    public void initView() {
        mRecyclerView = mRootView.findViewById(R.id.recyclerView);
    }

    public void createRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mCustomAdapter = new CustomAdapter(this.getContext(), mListData);
        mRecyclerView.setAdapter(mCustomAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void addDataToRecyclerView(String newData) {
        mCustomAdapter.addItem(newData);
    }

    public void delDataToRecyclerView(String position) {
        mCustomAdapter.removeItem(Integer.parseInt(position));
    }
}
