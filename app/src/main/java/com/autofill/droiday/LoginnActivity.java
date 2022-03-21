package com.autofill.droiday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginnActivity extends AppCompatActivity {

    Button calenderBtn;
    String name;
    int xp;
    TextView UserName, xpText;
    ImageView ImageView;
    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        db = FirebaseFirestore.getInstance();
        calenderBtn = findViewById(R.id.CalenderBtn);
        UserName = findViewById(R.id.UserName);
        xpText = findViewById(R.id.xpText);
        ImageView = findViewById(R.id.avatarImage);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        UserName.setText("");

        calenderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginnActivity.this, CalenderActivity.class);
                startActivity(intent);
            }
        });

        //UserName.setText(mUser.getDisplayName());
        db.collection("users")
                .document(mUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                name = ""+document.getData().get("first");
                                xp = Integer.valueOf(document.getData().get("xp").toString());
                                UserName.setText(name);
                                xpText.setText("XP :"+xp);
                                if(Integer.valueOf(document.getData().get("avatar").toString()) == 0) {
                                    ImageView.setImageResource(R.drawable.avatar);
                                }
                                //Toast.makeText(MainActivity.this, "name = "+document.getData().get("first").toString(), Toast.LENGTH_SHORT).show();
                                //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            } else {
                                //Toast.makeText(MainActivity.this, "Document Not Found", Toast.LENGTH_SHORT).show();
                                //Log.d(TAG, "No such document");
                            }
                        } else {
                            //Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();
                            //Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }
}