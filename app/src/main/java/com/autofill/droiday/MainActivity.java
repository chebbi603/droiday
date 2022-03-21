package com.autofill.droiday;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button getstarted = (Button) findViewById(R.id.getstarted);
        getstarted.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
            @Override
            public void onClick(View view) {
                Intent intent =new Intent( MainActivity.this,ResourcesActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                stopService(getIntent());
            }
        });
    }
}