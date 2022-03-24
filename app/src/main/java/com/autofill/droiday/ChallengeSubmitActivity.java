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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.Subject;

public class ChallengeSubmitActivity extends AppCompatActivity {

    ConstraintLayout Const1, Const2;
    EditText SubjectName, QuestionText, nbOfQuestions;
    TextView QuestionTitle;
    ListView listView;
    Button add;
    int nbAnswers;
    List<String> Answers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_submit);

        Const1 = findViewById(R.id.Constraint1);
        Const2 = findViewById(R.id.Constraint2);

        add = findViewById(R.id.addButton);
        SubjectName = findViewById(R.id.SubjectName);
        nbOfQuestions = findViewById(R.id.NbOfQuestions);
        QuestionTitle = findViewById(R.id.QuestionTitle);
        QuestionText = findViewById(R.id.QuestionText);
        listView = findViewById(R.id.answer_to_submit_listview);

        nbAnswers = 1;
        Answers.add("hiiii");
        AnswersAdapter adapter = new AnswersAdapter(this, Answers);
        listView.setAdapter(adapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nbAnswers++;
            }
        });
    }

    class AnswersAdapter extends ArrayAdapter<String> {

        Context context;
        List<String> rText;

        AnswersAdapter(Context c, List<String> text) {
            super(c, R.layout.answer_to_submit);
            this.context = c;
            this.rText = text;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("ViewHolder") View answerToSubmit = layoutInflater.inflate(R.layout.answer_to_submit, parent, false);
            //ImageView myBack = answer.findViewById(R.id.backgroundLeader);
            EditText answerText = findViewById(R.id.answerToSubmit);
            Typeface lato = ResourcesCompat.getFont(context, R.font.lato);
            //myBack.setBackground(getDrawable(R.drawable.them_leader));
            answerText.setTypeface(lato);
            answerText.setText(rText.get(position));
            return answerToSubmit;
        }
    }
}