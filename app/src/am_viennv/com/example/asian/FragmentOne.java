package com.example.asian;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class FragmentOne extends Fragment {

    private static String mParam1;

    public FragmentOne() {
    }

    public static FragmentOne newInstance(String param1) {
        FragmentOne fragment = new FragmentOne();
        mParam1 = param1;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_one, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FrameLayout frameLayout = view.findViewById(R.id.fragmentOne);
        frameLayout.setBackgroundColor(Color.parseColor(mParam1));
        super.onViewCreated(view, savedInstanceState);
    }
}
