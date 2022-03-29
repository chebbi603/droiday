package com.autofill.droiday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

public class TracksActivity extends AppCompatActivity {

    Button english, school, extra, coding, math, phy ,tech , svt, info, fr, ar, his, geo, edC;
    ScrollView SubjectScroll, ExtraScroll, Tracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracks);

        coding = findViewById(R.id.code);
        school = findViewById(R.id.school2);
        extra = findViewById(R.id.extracurricular2);
        SubjectScroll = findViewById(R.id.SubjectScroll);
        ExtraScroll = findViewById(R.id.ExtraScroll);
        Tracks = findViewById(R.id.Tracks);
        math = findViewById(R.id.math);
        phy = findViewById(R.id.physics);
        tech = findViewById(R.id.Tech);
        svt = findViewById(R.id.SVT);
        info = findViewById(R.id.Info);
        english = findViewById(R.id.english);
        fr = findViewById(R.id.francais);
        ar = findViewById(R.id.arabic);
        his = findViewById(R.id.Histoire);
        geo = findViewById(R.id.Geographie);
        edC = findViewById(R.id.EdC);

        SubjectScroll.setVisibility(View.VISIBLE);
        ExtraScroll.setVisibility(View.INVISIBLE);
        Tracks.setVisibility(View.INVISIBLE);

        school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                school.setBackgroundTintList(ColorStateList.valueOf(0xffffffff));
                extra.setBackgroundTintList(ColorStateList.valueOf(0xffebebeb));
                SubjectScroll.setVisibility(View.VISIBLE);
                ExtraScroll.setVisibility(View.INVISIBLE);
                Tracks.setVisibility(View.INVISIBLE);
            }
        });

        extra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                school.setBackgroundTintList(ColorStateList.valueOf(0xffebebeb));
                extra.setBackgroundTintList(ColorStateList.valueOf(0xffffffff));
                SubjectScroll.setVisibility(View.INVISIBLE);
                ExtraScroll.setVisibility(View.VISIBLE);
                Tracks.setVisibility(View.INVISIBLE);
            }
        });

        coding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TracksActivity.this, MiniGameActivity.class);
                startActivity(intent);
            }
        });

        math.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubjectScroll.setVisibility(View.INVISIBLE);
                Tracks.setVisibility(View.VISIBLE);
            }
        });
        phy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubjectScroll.setVisibility(View.INVISIBLE);
                Tracks.setVisibility(View.VISIBLE);
            }
        });
        tech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubjectScroll.setVisibility(View.INVISIBLE);
                Tracks.setVisibility(View.VISIBLE);
            }
        });
        svt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubjectScroll.setVisibility(View.INVISIBLE);
                Tracks.setVisibility(View.VISIBLE);
            }
        });
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubjectScroll.setVisibility(View.INVISIBLE);
                Tracks.setVisibility(View.VISIBLE);
            }
        });
        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubjectScroll.setVisibility(View.INVISIBLE);
                Tracks.setVisibility(View.VISIBLE);
            }
        });
        fr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubjectScroll.setVisibility(View.INVISIBLE);
                Tracks.setVisibility(View.VISIBLE);
            }
        });
        ar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubjectScroll.setVisibility(View.INVISIBLE);
                Tracks.setVisibility(View.VISIBLE);
            }
        });
        his.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubjectScroll.setVisibility(View.INVISIBLE);
                Tracks.setVisibility(View.VISIBLE);
            }
        });
        geo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubjectScroll.setVisibility(View.INVISIBLE);
                Tracks.setVisibility(View.VISIBLE);
            }
        });
        edC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubjectScroll.setVisibility(View.INVISIBLE);
                Tracks.setVisibility(View.VISIBLE);
            }
        });
    }
}