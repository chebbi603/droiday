package com.autofill.droiday;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ChallengeActivity extends AppCompatActivity {

    ListView listView;
    TextView SubjectTitle, QuestionText;
    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore db;
    LocalDate dateClicked;
    String Quiz;
    List<Question> quizList = new ArrayList<>();

    public class Question
    {
        private final String question;
        private final List<String> answers;
        private final int rightAnswer;

        public Question(String aQuestion, List<String> aAnswer, int aRightAnswer)
        {
            question   = aQuestion;
            answers = aAnswer;
            rightAnswer = aRightAnswer;
        }

        public String question()   { return question; }
        public List<String> answers() { return answers; }
        public int rightAnswer() { return rightAnswer; }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        listView = (ListView) findViewById(R.id.listviewQuiz);
        SubjectTitle = findViewById(R.id.subjectTitle);
        QuestionText = findViewById(R.id.questionText);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        Bundle extras = getIntent().getExtras();
        /* (extras != null) {
            dateClicked = LocalDate.parse(extras.getString("dateClicked"));
        }*/
        Log.d("TAG", "Daaaaaaaaaaaaaaaate" + extras.getString("dateClicked"));
        db.collection("challenges")
                .document(extras.getString("dateClicked"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                //geting the key
                                String type = "";
                                for (String elem : document.getData().keySet()){
                                    type = elem;
                                }
                                Log.d("TAG", "onComplete: " + type);
                                if(type.equals("quiz")){
                                    Quiz = document.getData().get(type).toString();
                                    Log.d("quizText", "Question: "+Quiz);
                                    String Q[] = Quiz.split("&");
                                    String Subject = Q[0];
                                    for(int i=1;i<Q.length ;i++) {
                                        String A[] = Q[i].split("#");
                                        String questionString = A[0];
                                        List<String> answers = new ArrayList<>();
                                        for (int j = 1; j < A.length - 1; j++) {
                                            answers.add(A[j]);
                                        }
                                        String rAnsText ="";
                                        for(int k=0;k<A[A.length-1].length();k++){
                                            if(A[A.length-1].charAt(k) != ' '){
                                                rAnsText += A[A.length-1].charAt(k);
                                            }
                                        }
                                        int rightAnswer = Integer.valueOf(rAnsText);
                                        Log.d("quiz", "Question: "+questionString);
                                        Log.d("quiz", "answers: "+answers);
                                        Log.d("quiz", "right Answer: " + rightAnswer);
                                        Question question = new Question(questionString, answers, rightAnswer);
                                        quizList.add(question);
                                    }
                                    QuestionText.setText(quizList.get(0).question());
                                    AnswerAdapter adapter = new AnswerAdapter(ChallengeActivity.this, quizList.get(0).answers(), new int[quizList.get(0).answers().size()]);
                                    listView.setAdapter(adapter);
                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            int checkS[] = new int[quizList.get(0).answers().size()];
                                            checkS[i]=1;
                                            AnswerAdapter adapter = new AnswerAdapter(ChallengeActivity.this, quizList.get(0).answers(), checkS);
                                            listView.setAdapter(adapter);//
                                        }
                                    });
                                }
                            }
                        }
                    }
                });
    }
    class AnswerAdapter extends ArrayAdapter<String> {

        Context context;
        List<String> rText;
        int rCheckStatus[];

        AnswerAdapter(Context c, List<String> text, int checkStatus[]) {
            super(c, R.layout.answer, R.id.AnswerText, text);
            this.context = c;
            this.rText = text;
            this.rCheckStatus = checkStatus;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("ViewHolder") View answer = layoutInflater.inflate(R.layout.answer, parent, false);
            ImageView myBack = answer.findViewById(R.id.backimgQuiz);
            TextView myText = answer.findViewById(R.id.AnswerText);
            ImageView check = answer.findViewById(R.id.AnswerCheck);
            Typeface latobold = ResourcesCompat.getFont(context, R.font.lato_bold);
            myText.setText(rText.get(position));
            if(rCheckStatus[position] == 1) {
                check.setVisibility(View.VISIBLE);
            }
            else {
                check.setVisibility(View.INVISIBLE);
            }
            myText.setTypeface(latobold);
            myText.setX(myBack.getX() + 8);
            return answer;
        }
    }
}