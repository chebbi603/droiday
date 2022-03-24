package com.autofill.droiday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class AccountSetUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] Levels = { "1ere" , "2eme", "3eme", "4eme", "5eme", "6eme", "7eme", "8eme", "9eme"};
    EditText FirstName, LastName;
    Button SetUpBtn , SetUpBtn1, SetUpBtn2;
    Button Teacher, Student;
    String firstname, lastname;
    String email = "";
    String password = "";
    int position = -1;
    int radio = 0;
    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore db;
    public  LocalDate today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_set_up);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ConstraintLayout const1 = findViewById(R.id.const1);
        ConstraintLayout const2 = findViewById(R.id.const2);
        ConstraintLayout const3 = findViewById(R.id.const3);
        email = getIntent().getStringExtra("key_email");
        password = getIntent().getStringExtra("key_password");
        FirstName = findViewById(R.id.inpFirstName);
        LastName = findViewById(R.id.inpLastName);
        SetUpBtn = findViewById(R.id.SetUpBtn);
        SetUpBtn1 = findViewById(R.id.SetUpBtn1);
        SetUpBtn2 = findViewById(R.id.SetUpBtn2);
        Student = findViewById(R.id.student);
        Teacher = findViewById(R.id.teacher);
        TextView title = findViewById(R.id.titleinstance);
        TextView desc = findViewById(R.id.desc);
        TextView title2 = findViewById(R.id.titleinstance2);
        TextView desc2 = findViewById(R.id.desc2);
        TextView title3 = findViewById(R.id.titleinstance3);
        TextView desc3 = findViewById(R.id.desc3);
        TextView welcome = findViewById(R.id.welcome_msg);

        Spinner LvlChoice = findViewById(R.id.lvlChoice);
        LvlChoice.setOnItemSelectedListener(this);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        today = LocalDate.now();

        Animation fadeout = AnimationUtils.loadAnimation(this,R.anim.fade_out);
        Animation fadein = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        ArrayAdapter aa = new ArrayAdapter(this,R.layout.spinner,Levels);
        aa.setDropDownViewResource(R.layout.dropdown);
        LvlChoice.setAdapter(aa);
        Student.setBackground(getDrawable(R.drawable.checkboxisoff));
        Student.setTextColor(Color.BLACK);
        Teacher.setBackground(getDrawable(R.drawable.checkboxisoff));
        Teacher.setTextColor(Color.BLACK);
        const1.setVisibility(View.VISIBLE);
        const2.setVisibility(View.INVISIBLE);
        const1.setBackgroundColor(Color.rgb(254,234,148));
        title.setTextColor(Color.rgb(215,138,73));
        //desc.setText("Please provide us with your full name");
        title2.setTextColor(Color.rgb(45,124,225));
        final int[] isTrue = {0};

        final boolean[] isStudent = {false};
        final boolean[] isTeacher = {false};

        SetUpBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstname = FirstName.getText().toString();
                lastname = LastName.getText().toString();
                if (firstname.isEmpty()) {
                    FirstName.setError("Invalid Username");
                } else if (lastname.isEmpty()) {
                    LastName.setError("Invalid Username");
                }else {
                    const1.startAnimation(fadeout);
                    const1.setVisibility(View.INVISIBLE);
                    const3.startAnimation(fadein);
                    const3.setVisibility(View.VISIBLE);

                }
            }
        });

        Student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStudent[0] = true;
                isTeacher[0] = false;
                Student.setBackground(getDrawable(R.drawable.checkboxison));
                Student.setTextColor(Color.WHITE);
                Teacher.setBackground(getDrawable(R.drawable.checkboxisoff));
                Teacher.setTextColor(Color.BLACK);

            }
        });
        Teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStudent[0] = false;
                isTeacher[0] = true;
                Teacher.setBackground(getDrawable(R.drawable.checkboxison));
                Teacher.setTextColor(Color.WHITE);
                Student.setBackground(getDrawable(R.drawable.checkboxisoff));
                Student.setTextColor(Color.BLACK);

            }
        });

        SetUpBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStudent[0] || isTeacher[0]) {
                    if (isTeacher[0]) {
                        radio = 1;
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
                        user.put("avatar", "0");
                        user.put("type", "teacher");
                        user.put("xp", 0);
                        //
                        // Add a new document with a generated ID
                        DocumentReference doc = db.collection("users").document(mUser.getUid());
                        doc.set(user);

                        //create Participation
                        Map<String, Object> part = new HashMap<>();
                        doc.collection("Participation").document("-1").set(part);

                        //Sign Out
                        if (email != null && password != null) {
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
                        } else {
                            updateUI(mUser);
                        }
                    } else {
                        const1.startAnimation(fadeout);
                        const1.setVisibility(View.INVISIBLE);
                        const2.startAnimation(fadein);
                        const2.setVisibility(View.VISIBLE);
                        desc2.setText("Select your class from the dropdown below");
                        welcome.setText("Welcome " + firstname + " !");

                    }

                }
            }
        });

        SetUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                   /* if (firstname.isEmpty()) {
                        FirstName.setError("Invalid Username");
                    } else if (lastname.isEmpty()) {
                        FirstName.setError("Invalid Username");

                    }else{
                        isTrue[0]++;
                        FirstName.startAnimation(fadeout);
                        LastName.startAnimation(fadeout);
                        FirstName.setVisibility(View.INVISIBLE);
                        LastName.setVisibility(View.INVISIBLE);
                        LvlChoice.startAnimation(fadein);
                        LvlChoice.setVisibility(View.VISIBLE);
                        title.setTextColor(Color.rgb(45,124,225));
                        constraintLayout.setBackgroundColor(Color.rgb(205,225,252));
                        desc.setText("Select your class");
                        line.setBackground(getDrawable(R.drawable.designlineblue));


                }*/
                if (position>0){
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
                        /*String UserType = "student";
                        if (radio == 1) UserType ="teacher";*/

                        // Create a new user with a first and last name
                        Map<String, Object> user = new HashMap<>();
                        user.put("first", firstname);
                        user.put("last", lastname);
                        user.put("level", position);
                        user.put("avatar", "0");
                        user.put("type", "student");
                        user.put("xp", 0);
                        user.put("first_day", ""+today);
                        //
                        // Add a new document with a generated ID
                        DocumentReference doc = db.collection("users").document(mUser.getUid());
                        doc.set(user);

                        //create Participation
                        Map<String, Object> part = new HashMap<>();
                        doc.collection("Participation").document("-1").set(part);

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
        Intent intent;
        if(radio == 0) {
            intent = new Intent(AccountSetUpActivity.this, HomePage.class);
        }else {
            intent = new Intent(AccountSetUpActivity.this, TeacherHomePage.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("key_email", email);
        intent.putExtra("key_password", password);
        startActivity(intent);
    }
}