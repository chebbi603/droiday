package com.autofill.droiday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

public class TracksActivity extends AppCompatActivity {

    Button english , school, extra, coding;
    ScrollView SubjectScroll, ExtraScroll, Tracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracks);

        //coding = findViewById(R.id.math2);
        school = findViewById(R.id.school);
        extra = findViewById(R.id.extracurricular);
        english = findViewById(R.id.english);
        SubjectScroll = findViewById(R.id.SubjectScroll);
        //ExtraScroll = findViewById(R.id.ExtraScroll);
        Tracks = findViewById(R.id.Tracks);

        SubjectScroll.setVisibility(View.VISIBLE);
        //ExtraScroll.setVisibility(View.INVISIBLE);
        Tracks.setVisibility(View.INVISIBLE);

        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubjectScroll.setVisibility(View.INVISIBLE);
                Tracks.setVisibility(View.VISIBLE);
            }
        });

        school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubjectScroll.setVisibility(View.VISIBLE);
                ExtraScroll.setVisibility(View.INVISIBLE);
                Tracks.setVisibility(View.INVISIBLE);
            }
        });

        extra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*SubjectScroll.setVisibility(View.INVISIBLE);
                ExtraScroll.setVisibility(View.VISIBLE);
                Tracks.setVisibility(View.INVISIBLE);*/
                Intent intent = new Intent(TracksActivity.this, MiniGameActivity.class);
                startActivity(intent);
            }
        });

        coding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TracksActivity.this, MiniGameActivity.class);
                startActivity(intent);
            }
        });
    }
}