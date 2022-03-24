package com.autofill.droiday;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    String filename = "lang.mt";
    FileOutputStream outputStream;
    String language = "french";
    String line;
    String appname = "appname";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Button ar = findViewById(R.id.ar);
        Button fr = findViewById(R.id.fr);
        TextView title = findViewById(R.id.titlehomepage);
        TextView desc = findViewById(R.id.deschomepage);
        Button getstarted = (Button) findViewById(R.id.getstarted);

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

        if(language.equals("french")){
            title.setText("ٍVotre parcours académique\nn'est plus un problème");
            desc.setText("Rejoignez "+appname+" Maintenant \nPouvez vous remporter le défi?");
            getstarted.setText("Commencer");
        }
        if(language.equals("arabic")){
            title.setText("لن تقلق بشأن مشوارك التعليمي بعد الآن");
            desc.setText("إشترك الآن و استمتع بباقة من التحديات و مكتبة من الوثائق");
            getstarted.setText("فلنبدأ");
        }


        getstarted.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
            @Override
            public void onClick(View view) {
                Intent intent =new Intent( MainActivity.this,LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                stopService(getIntent());
            }
        });

        ar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = "arabic";
                try {
                    outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                    outputStream.write(value.getBytes());
                    outputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent =new Intent( MainActivity.this,MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }


        });

        fr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = "french";
                try {
                    outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                    outputStream.write(value.getBytes());
                    outputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent intent =new Intent( MainActivity.this,MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }

        });
    }
}