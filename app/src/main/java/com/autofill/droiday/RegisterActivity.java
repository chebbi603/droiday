package com.autofill.droiday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    TextView HasAcc;
    Button Register;
    EditText EmailText, PasswordText, PasswordConfirmText;
    String username, email, password, passwordConfirm;
    String EmailPattern = "[a-zA-Z0-9_.-]+@[a-zA-Z0-9_.-]+[a-zA-Z0-9_.-]";
    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    private static final String TAG = "EmailPassword";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressDialog = new ProgressDialog(this);

        HasAcc = findViewById(R.id.HasAccBtn);
        Register = findViewById(R.id.RegisterBtn);
        EmailText = findViewById(R.id.inpEmailReg);
        PasswordText = findViewById(R.id.inpPasswordReg);
        PasswordConfirmText = findViewById(R.id.inpConfirmPassword);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        HasAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( RegisterActivity.this,LoginActivity.class));
            }
        });
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = EmailText.getText().toString();
                password = PasswordText.getText().toString();
                passwordConfirm = PasswordConfirmText.getText().toString();
                if (!email.matches(EmailPattern)) {
                    EmailText.setError("Enter A Valid Email Address");
                } else if (password.isEmpty() || password.length() < 8) {
                    PasswordText.setError("Enter A Valid Password");
                } else if (!password.equals(passwordConfirm)) {
                    PasswordConfirmText.setError("Passwords Do Not Match");
                } else {
                    progressDialog.setMessage("Registration...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    //progressDialog.dismiss();
                                    Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                    mUser = mAuth.getCurrentUser();
                                    /*UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(username)
                                            .build();
                                    mUser.updateProfile(profileUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(RegisterActivity.this, "Name sent to "+username, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                    FirebaseAuth.getInstance().signOut();
                                    mAuth.signInWithEmailAndPassword(email, password)
                                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        // Sign in success, update UI with the signed-in user's information
                                                        progressDialog.dismiss();
                                                        mUser = mAuth.getCurrentUser();
                                                        updateUI(mUser);
                                                    } else {
                                                        // If sign in fails, display a message to the user.
                                                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });*/
                                    updateUI(mUser);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    progressDialog.dismiss();
                                    if((task.getException() instanceof FirebaseAuthUserCollisionException)) {
                                        EmailText.setError("Email Already In Use");
                                    }else{
                                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                }
            }
        });
    }
    private void updateUI(FirebaseUser user) {
        Intent intent =new Intent( RegisterActivity.this,AccountSetUpActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("key_email", email);
        intent.putExtra("key_password", password);
        startActivity(intent);
    }
}