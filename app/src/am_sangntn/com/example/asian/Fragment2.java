package com.example.asian;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Fragment2 extends Fragment {

    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment2, container, false);
        initUI();
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void initUI() {
        TextView textView = view.findViewById(R.id.tv_fg_2);
        LinearLayout layout_fragment = view.findViewById(R.id.layout_fragment2);
        try {
            if (getArguments() != null) {
                String colorText = getArguments().getString("color");
                layout_fragment.setBackgroundColor(Color.parseColor(colorText));
            }
        } catch (Exception e) {
            textView.setText(e.toString());
        }

        textView.setText("Fragment Two");


    }
}
