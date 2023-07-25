package com.example.asian;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ConstrainLayout extends AppCompatActivity {
    private Button btnSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constrain_layout);
        btnSend = findViewById(R.id.btn_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = ((EditText)findViewById(R.id.edt_username)).getText().toString();
                String cardInfo = ((EditText)findViewById(R.id.edt_card)).getText().toString();
                String moreInfo = ((EditText)findViewById(R.id.edt_more_info)).getText().toString();
                if(
                validateInfo(view.getContext(),userName,"UserName")&&
                validateInfo(view.getContext(),cardInfo,"CardInfo")&&
                validateInfo(view.getContext(),moreInfo,"MoreInfo")
                ){
                    makeText(view.getContext(),"Login Success !");
                        Intent switchActivityIntent = new Intent(view.getContext(), MainActivity.class);
                        startActivity(switchActivityIntent);
                }
            }
        });

    }

    private boolean validateInfo(Context context, String text, String nameMessage){
        if(text.equals("")){
            makeText(context,"Please enter "+nameMessage);
            return false;
        }
        if(nameMessage.equals("UserName")){
            if(text.length()<3)
            {
                makeText(context,"Length UserName must >=3 character");
                return false;
            }
        }
        else if(nameMessage.equals("CardInfo")){
            if(text.length()!=9)
            {
                makeText(context,"Length Card information must = 9 character");
                return  false;
            }
        }
        else {
            if(text.length()<100)
            {
                makeText(context,"Length more information must >= 100 character");
                return false;
            }
        }
        return true;
    }

    public static  void makeText(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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