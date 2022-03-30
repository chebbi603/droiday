package com.autofill.droiday;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import io.github.muddz.styleabletoast.StyleableToast;

public class LoginActivity extends AppCompatActivity{

    Intent intent;
    Button fbButton, GglButton, LoginBtn ;
    EditText EmailText, PasswordText;
    String email, password;
    String EmailPattern = "[a-zA-Z0-9_.-]+@[a-zA-Z0-9_.-]+[a-zA-Z0-9_.-]";
    TextView CreateAcc, ForgotBtn;
    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        progressDialog = new ProgressDialog(this);

        LoginBtn = findViewById(R.id.LoginBtn);
        fbButton = findViewById(R.id.fbButton);
        GglButton = findViewById(R.id.GglButton);
        EmailText = findViewById(R.id.inpEmail);
        PasswordText = findViewById(R.id.inpPassword);
        CreateAcc = findViewById(R.id.NewAccBtn);
        ForgotBtn = findViewById(R.id.ForgotBtn);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        /*FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();*/

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = EmailText.getText().toString();
                password = PasswordText.getText().toString();
                if (!email.matches(EmailPattern)) {
                    EmailText.setError("Enter A Valid Email Address");
                } else if (password.isEmpty() || password.length() < 8) {
                    PasswordText.setError("Enter A Valid Password");
                } else {
                    progressDialog.setMessage("Login...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);
                                        StyleableToast.makeText(LoginActivity.this, "Logged In", Toast.LENGTH_SHORT,R.style.mytoast).show();
                                    } else {
                                        progressDialog.dismiss();
                                        if(task.getException() instanceof FirebaseAuthInvalidUserException){
                                            EmailText.setError("Invalid Email");
                                        }else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                                            PasswordText.setError("Invalid Password");
                                        }else {
                                            // If sign in fails, display a message to the user.
                                            StyleableToast.makeText(LoginActivity.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT,R.style.mytoast).show();
                                        }
                                    }
                                }
                            });
                }
            }
        });
        GglButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent( LoginActivity.this,GoogleSignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        fbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent( LoginActivity.this,FacebookAuthActivity.class);
                //Intent intent =new Intent( LoginActivity.this,MiniGameActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        CreateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( LoginActivity.this,RegisterActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        ForgotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( LoginActivity.this,PasswordResetActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

    }
    /*private void updateUI(FirebaseUser user) {
        Intent intent =new Intent( LoginActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }*/
    private void updateUI(FirebaseUser user) {
        db.collection("users")
                .document(mUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                if(document.getData().get("type").equals("teacher")){
                                    intent = new Intent( LoginActivity.this,TeacherHomePage.class);
                                }else{
                                    intent = new Intent( LoginActivity.this,HomePage.class);
                                }
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                StyleableToast.makeText(LoginActivity.this, "exists", Toast.LENGTH_SHORT,R.style.mytoast).show();
                            }else{
                                intent = new Intent( LoginActivity.this,AccountSetUpActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                StyleableToast.makeText(LoginActivity.this, "doesn't exist", Toast.LENGTH_SHORT,R.style.mytoast).show();
                            }
                        }
                    }
                });
    }
}
