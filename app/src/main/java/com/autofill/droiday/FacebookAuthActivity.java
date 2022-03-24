package com.autofill.droiday;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

public class FacebookAuthActivity extends LoginActivity {

    CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore db;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        db = FirebaseFirestore.getInstance();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
    }

    /*@Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            mUser = mAuth.getCurrentUser();
                            updateUI(mUser);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(FacebookAuthActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /*private void updateUI(FirebaseUser user) {
        Intent intent =new Intent( FacebookAuthActivity.this,MainActivity.class);
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
                                    intent = new Intent( FacebookAuthActivity.this,TeacherHomePage.class);
                                }else{
                                    intent = new Intent( FacebookAuthActivity.this,HomePage.class);
                                }
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                Toast.makeText(FacebookAuthActivity.this, "exists", Toast.LENGTH_SHORT).show();
                            }else{
                                intent = new Intent( FacebookAuthActivity.this,AccountSetUpActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                Toast.makeText(FacebookAuthActivity.this, "doesn't exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}