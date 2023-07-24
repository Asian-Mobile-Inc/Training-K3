package com.example.asian;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.List;

public class ConstrainLayout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constrain_layout);
    }
    public void onRadioButtonClick(View view){
        List<RadioButton> radioButtons = new ArrayList<>();
        radioButtons.add(findViewById(R.id.radio_intermediate));
        radioButtons.add(findViewById(R.id.radio_college));
        radioButtons.add(findViewById(R.id.radio_university));

        for(RadioButton radioButton : radioButtons){
            if(radioButton.isChecked()){
                ColorStateList colorStateList = new ColorStateList(
                        new int[][]{
                                new int[]{android.R.attr.state_enabled} //enabled
                        },
                        new int[] {getResources().getColor(R.color.dart_pink) }
                );
                radioButton.setButtonTintList(colorStateList);
            }
            else{
                ColorStateList colorStateList = new ColorStateList(
                        new int[][]{
                                new int[]{android.R.attr.state_enabled} //enabled
                        },
                        new int[] {getResources().getColor(R.color.grey) }
                );
                radioButton.setButtonTintList(colorStateList);
            }
        }
    }
}