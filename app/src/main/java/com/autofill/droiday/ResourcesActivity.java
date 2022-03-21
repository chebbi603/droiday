package com.autofill.droiday;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ResourcesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);
        ImageView indic  = (ImageView) findViewById(R.id.indic);
        ImageView home_but = (ImageView) findViewById(R.id.home_but);
        ImageView cal_but = (ImageView) findViewById(R.id.cal_but);
        ImageView games_but = (ImageView) findViewById(R.id.games_but);
        TextView title = (TextView) findViewById(R.id.title_res);
        Button manuels = (Button) findViewById(R.id.books_btn);
        Button exam = (Button) findViewById(R.id.exams_btn);
        Button series = (Button) findViewById(R.id.series_btn);
        Button return_btn = (Button) findViewById(R.id.return_btn);
        title.setText("");
        manuels.setVisibility(View.VISIBLE);
        exam.setVisibility(View.VISIBLE);
        series.setVisibility(View.VISIBLE);
        return_btn.setVisibility(View.INVISIBLE);

        return_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title.setText("");
                manuels.setVisibility(View.VISIBLE);
                exam.setVisibility(View.VISIBLE);
                series.setVisibility(View.VISIBLE);
                return_btn.setVisibility(View.INVISIBLE);
            }
        });
        manuels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title.setText("Manuels :");
                manuels.setVisibility(View.INVISIBLE);
                exam.setVisibility(View.INVISIBLE);
                series.setVisibility(View.INVISIBLE);
                return_btn.setVisibility(View.VISIBLE);
            }
        });
        exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title.setText("Examens et épreuves :");
                manuels.setVisibility(View.INVISIBLE);
                exam.setVisibility(View.INVISIBLE);
                series.setVisibility(View.INVISIBLE);
                return_btn.setVisibility(View.VISIBLE);
            }
        });
        series.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title.setText("Series d'exercices :");
                manuels.setVisibility(View.INVISIBLE);
                exam.setVisibility(View.INVISIBLE);
                series.setVisibility(View.INVISIBLE);
                return_btn.setVisibility(View.VISIBLE);
            }
        });
        cal_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indic.setX(cal_but.getX() + 40);
            }
        });
        home_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indic.setX(home_but.getX()+40);
            }
        });
        games_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indic.setX(games_but.getX()+40);
            }
        });
    }
}