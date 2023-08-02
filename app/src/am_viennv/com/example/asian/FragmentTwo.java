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

public class FragmentTwo extends Fragment {
    private static String mParam;

    public FragmentTwo() {
    }

    public static FragmentTwo newInstance(String param) {
        FragmentTwo fragment = new FragmentTwo();
        mParam = param;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_two, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FrameLayout frameLayout = view.findViewById(R.id.fragmentTwo);
        frameLayout.setBackgroundColor(Color.parseColor(mParam));
        super.onViewCreated(view, savedInstanceState);
    }
}
