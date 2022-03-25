package com.autofill.droiday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class SplashActivity extends AppCompatActivity {
    String filename = "lang.mt";
    String isOpenedfile = "isopened.mt";
    FileOutputStream outputStream;
    String language = "french";
    boolean isOpened = false;
    String line;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        try {
            FileInputStream in = openFileInput(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            try {
                if ((line = bufferedReader.readLine()) != null) {
                    language = line;
                } else {
                    language = "french";
                }
            } catch (IOException e) {
                language = "french";
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            language = "french";
            e.printStackTrace();
        }
        try {
            FileInputStream in = openFileInput(isOpenedfile);
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            try {
                if ((line = bufferedReader.readLine()) != null) {
                    String s  = line;
                    if(s.equals("isOpened")) isOpened = true;
                } else {
                    isOpened = false;
                }
            } catch (IOException e) {
                isOpened = false;
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            isOpened = false;
            e.printStackTrace();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent =new Intent( SplashActivity.this,MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                stopService(getIntent());
            }
        },3000);

    }
}