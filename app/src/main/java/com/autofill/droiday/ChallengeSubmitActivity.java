package com.autofill.droiday;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import com.autofill.droiday.databinding.ActivityChallengeSubmitBinding;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import javax.security.auth.Subject;


public class ChallengeSubmitActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore db;
    ConstraintLayout Const1, Const2;
    EditText answer1, answer2, answer3, answer4;
    EditText SubjectName, QuestionText, nbOfQuestions, RightAnswer;
    TextView QuestionTitle;
    ListView listView;
    Button next, add , Submit;
    String Finalstring, Subject;
    int nbQuestions,questionIndex, nbAnswers;
    List<String> Answers = new ArrayList<>();
    //private ActivityChallengeSubmitBinding binding;
    EditText ans[] = new EditText[4];
    List<String> Questions;
    LocalDate lastChallenge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*binding = ActivityChallengeSubmitBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View inflater = LayoutInflater.from(ChallengeSubmitActivity.this).inflate(R.layout.answer_to_submit, null);
                binding.parentLinearLayout.addView(inflater, binding.parentLinearLayout.getChildCount());
            }
        });*/
        setContentView(R.layout.activity_challenge_submit);

        //FireBase
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        Const1 = findViewById(R.id.Constraint1);
        Const2 = findViewById(R.id.Constraint2);

        next = findViewById(R.id.Next);
        add = findViewById(R.id.addButton);
        Submit = findViewById(R.id.Submit_Answer);
        SubjectName = findViewById(R.id.SubjectName);
        nbOfQuestions = findViewById(R.id.NbOfQuestions);
        QuestionTitle = findViewById(R.id.QuestionTitle);
        QuestionText = findViewById(R.id.QuestionText);
        listView = findViewById(R.id.answer_to_submit_listview);
        ans[0] = findViewById(R.id.answer1);
        ans[1] = findViewById(R.id.answer2);
        ans[2] = findViewById(R.id.answer3);
        ans[3] = findViewById(R.id.answer4);
        RightAnswer = findViewById(R.id.Right_Answer);

        Const1.setVisibility(View.VISIBLE);
        Const2.setVisibility(View.INVISIBLE);
        QuestionTitle.setText("Question N°1");

        questionIndex=0;
        nbAnswers = 0;

        db.collection("challenges")
            .document("lastChallenge")
            .get()
            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                                lastChallenge = LocalDate.parse(document.getData().get("date").toString());
                            }
                        }
                    }
            });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nbAnswers<4) nbAnswers++;
                if(nbAnswers==1){
                    ans[0].setVisibility(View.VISIBLE);
                }else if(nbAnswers==2){
                    ans[1].setVisibility(View.VISIBLE);
                }else if(nbAnswers==3){
                    ans[2].setVisibility(View.VISIBLE);
                }else{
                    ans[3].setVisibility(View.VISIBLE);
                }
                /*nbAnswers++;
                Answers.add("");
                AnswersAdapter adapter = new AnswersAdapter(ChallengeSubmitActivity.this, Answers);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(ChallengeSubmitActivity.this, "fffffffff", Toast.LENGTH_SHORT).show();
                    }
                });*/
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SubjectName.getText().toString().isEmpty()){
                    SubjectName.setError("type the subject");
                }else if(nbOfQuestions.getText().toString().isEmpty()){
                    nbOfQuestions.setError("Enter number");
                }else if(Integer.valueOf(nbOfQuestions.getText().toString())<=0){
                    nbOfQuestions.setError("Enter number");
                }else{
                    Subject = SubjectName.getText().toString();
                    nbQuestions = Integer.valueOf(nbOfQuestions.getText().toString());

                    Const1.setVisibility(View.INVISIBLE);
                    Const2.setVisibility(View.VISIBLE);

                    ans[0].setVisibility(View.INVISIBLE);
                    ans[1].setVisibility(View.INVISIBLE);
                    ans[2].setVisibility(View.INVISIBLE);
                    ans[3].setVisibility(View.INVISIBLE);

                    Finalstring = Subject + " &left ";
                }
            }
        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(QuestionText.getText().toString().isEmpty()){
                    QuestionText.setError("Enter Question");
                }else if(RightAnswer.getText().toString().isEmpty()){
                    RightAnswer.setError("Provide Right Answer");
                }else if(Integer.valueOf(RightAnswer.getText().toString())>nbAnswers){
                    RightAnswer.setError("Enter Valid Number");
                }else if (nbAnswers !=0){
                    Answers = new ArrayList<>();
                    boolean check = true;
                    for (int i = 0; i < nbAnswers; i++) {
                        if (ans[i].getText().toString().isEmpty()) {
                            ans[i].setError("Add Answer");
                            check = false;
                            break;
                        }
                        Answers.add(ans[i].getText().toString());
                    }
                    if (check) {
                        String CapitalizedQuestionText = QuestionText.getText().toString().substring(0, 1).toUpperCase() + QuestionText.getText().toString().substring(1);
                        String question = "&" + CapitalizedQuestionText + " ";
                        for(int j = 0 ; j < nbAnswers; j++){
                            question += "#" + Answers.get(j) +" ";
                        }
                        Finalstring += question + "#" + (Integer.valueOf(RightAnswer.getText().toString())-1);
                        questionIndex++;

                        if(questionIndex == nbQuestions){
                            //out
                            Log.d("question", Finalstring);
                            db.collection("challenges")
                                    .document("lastChallenge")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    lastChallenge = LocalDate.parse(document.getData().get("date").toString());
                                                    Map<String, Object> map = new HashMap<>();
                                                    map.put("quiz", Finalstring);
                                                    DocumentReference doc = db.collection("challenges").document(""+lastChallenge.plusDays(1));
                                                    doc.set(map);
                                                    map = new HashMap<>();
                                                    map.put("date", ""+lastChallenge.plusDays(1));
                                                    doc = db.collection("challenges").document("lastChallenge");
                                                    doc.set(map);
                                                }
                                            }
                                        }
                                    });

                        }else{
                            //Next Question
                            QuestionTitle.setText("Question N°" + (questionIndex+1));
                            QuestionText.setText("");
                            RightAnswer.setText("");
                            nbAnswers = 0;
                            ans[0].setText("");
                            ans[1].setText("");
                            ans[2].setText("");
                            ans[3].setText("");
                            ans[0].setVisibility(View.INVISIBLE);
                            ans[1].setVisibility(View.INVISIBLE);
                            ans[2].setVisibility(View.INVISIBLE);
                            ans[3].setVisibility(View.INVISIBLE);
                        }
                    }
                }


            }
        });
    }

    /*class AnswersAdapter extends ArrayAdapter<String> {

        Context context;
        List<String> rText;

        AnswersAdapter(Context c, List<String> text) {
            super(c, R.layout.answer_to_submit, text);
            this.context = c;
            this.rText = text;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("ViewHolder") View answerToSubmit = layoutInflater.inflate(R.layout.answer_to_submit, parent, false);
            //ImageView myBack = answer.findViewById(R.id.backgroundLeader);
            EditText answerText = answerToSubmit.findViewById(R.id.answerToSubmit);
            Typeface lato = ResourcesCompat.getFont(context, R.font.lato);
            answerText.setFocusable(true);
            answerText.setClickable(true);
            //myBack.setBackground(getDrawable(R.drawable.them_leader));
            //answerText.setTypeface(lato);
            if(!rText.get(position).equals("")) answerText.setText(rText.get(position));
            return answerToSubmit;
        }
    }*/
}