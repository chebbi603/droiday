package com.autofill.droiday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AccountSetUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] Levels = { "1ere" , "2eme", "3eme", "4eme", "5eme", "6eme", "7eme", "8eme", "9eme"};
    EditText FirstName, LastName;
    Button SetUpBtn;
    String firstname, lastname;
    String email = "";
    String password = "";
    int position = -1;
    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_set_up);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        email = getIntent().getStringExtra("key_email");
        password = getIntent().getStringExtra("key_password");
        FirstName = findViewById(R.id.inpFirstName);
        LastName = findViewById(R.id.inpLastName);
        SetUpBtn = findViewById(R.id.SetUpBtn);
        Spinner LvlChoice = findViewById(R.id.lvlChoice);
        LvlChoice.setOnItemSelectedListener(this);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,Levels);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        LvlChoice.setAdapter(aa);

        SetUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstname = FirstName.getText().toString();
                lastname = LastName.getText().toString();
                if(firstname.isEmpty()) {
                    FirstName.setError("Invalid Username");
                }else if(firstname.isEmpty()) {
                    FirstName.setError("Invalid Username");
                }else if (position>0){
                    //Capitalize The Names
                    firstname = firstname.substring(0, 1).toUpperCase() + firstname.substring(1);
                    lastname = lastname.substring(0, 1).toUpperCase() + lastname.substring(1);

                    //Update Username and Profile Picture
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(firstname)
                            //.setPhotoUri()
                            .build();
                    mUser.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    //Toast.makeText(AccountSetUpActivity.this, "Name Updated To"+firstname, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    // Create a new user with a first and last name
                    Map<String, Object> user = new HashMap<>();
                    user.put("first", firstname);
                    user.put("last", lastname);
                    user.put("level", position);
                    user.put("avatar", "0");
                    user.put("xp", "0");

                    // Add a new document with a generated ID
                    db.collection("users").document(mUser.getUid()).set(user);
                    //Sign Out
                    if(email!=null && password!=null ) {
                        FirebaseAuth.getInstance().signOut();

                        //Sign In Again
                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(AccountSetUpActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            mUser = mAuth.getCurrentUser();
                                            //Go to MainActivity
                                            updateUI(mUser);
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(AccountSetUpActivity.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }else{updateUI(mUser);}
                }
            }
        });

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Toast.makeText(this, "" +Levels[pos], Toast.LENGTH_SHORT).show();
        position = pos+1;
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
    private void updateUI(FirebaseUser user) {
        Intent intent =new Intent( AccountSetUpActivity.this,HomePage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("key_email", email);
        intent.putExtra("key_password", password);
        startActivity(intent);
    }
}