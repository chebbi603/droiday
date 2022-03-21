package com.autofill.droiday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PasswordResetActivity extends AppCompatActivity {

    Button ResetBtn;
    TextView BackBtn;
    EditText EmailText;
    String email;
    String EmailPattern = "[a-zA-Z0-9_.-]+@[a-zA-Z0-9_.-]+[a-zA-Z0-9_.-]";
    private FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        BackBtn = findViewById(R.id.backBtn);
        ResetBtn = findViewById(R.id.resetBtn);
        EmailText = findViewById(R.id.inpEmailReset);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( PasswordResetActivity.this,LoginActivity.class));
            }
        });
        ResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                email = EmailText.getText().toString();
                if (!email.matches(EmailPattern)) {
                    EmailText.setError("Enter A Valid Email Address");
                } else {
                    auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Intent intent =new Intent( PasswordResetActivity.this,LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    Toast.makeText(PasswordResetActivity.this, "An Email Has Been Sent To Your Address", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(PasswordResetActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                }
            }
        });
    }
}