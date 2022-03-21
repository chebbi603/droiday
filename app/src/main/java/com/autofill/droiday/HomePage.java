package com.autofill.droiday;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class HomePage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        ImageView indic  = (ImageView) findViewById(R.id.indic);
        ImageView home_but = (ImageView) findViewById(R.id.home_but);
        ImageView cal_but = (ImageView) findViewById(R.id.cal_but);
        ImageView games_but = (ImageView) findViewById(R.id.games_but);
        cal_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indic.setX(cal_but.getX() + 11);
            }
        });
        home_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indic.setX(home_but.getX()+11);
            }
        });
        games_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indic.setX(games_but.getX()+11);
            }
        });

    }
}