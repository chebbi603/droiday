package com.autofill.droiday;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
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

public class ChallengeActivity extends AppCompatActivity {

    ListView listView;
    TextView SubjectTitle, QuestionText, XpCounter, ValidationText;
    Button SubmitBtn;
    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore db;
    LocalDate dateClicked;
    AnswerAdapter adapter;
    String Quiz, type;
    boolean BtnOn = true;
    int currentQuestion = 0;
    int Choice =-1;
    int TotalXp = 0;
    int nbCorrectAnswers = 0;
    List<Question> quizList = new ArrayList<>();
    List<Integer> monthParticipation;
    MediaPlayer correctMp = new MediaPlayer();
    MediaPlayer wrongMp = new MediaPlayer();

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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        LottieAnimationView lottie = findViewById(R.id.lottie2);
        listView = (ListView) findViewById(R.id.listviewQuiz);
        SubjectTitle = findViewById(R.id.subjectTitle);
        QuestionText = findViewById(R.id.questionText);
        ValidationText = findViewById(R.id.ValidationText);
        XpCounter = findViewById(R.id.XpCounter);
        SubmitBtn = findViewById(R.id.SubmitBtn);

        ValidationText.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        correctMp = MediaPlayer.create(this, R.raw.correct);
        wrongMp =  MediaPlayer.create(this, R.raw.wrong);

        XpCounter.setText("0");

        //Get dateClicked from prev Activity
        Bundle extras = getIntent().getExtras();
        dateClicked = LocalDate.parse(extras.getString("dateClicked"));

        db.collection("challenges")
            .document(extras.getString("dateClicked"))
            .get()
            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            //Getting the challenge type (key)
                            type = "";
                            for (String elem : document.getData().keySet()){
                                type = elem;
                            }

                            //Set the Quiz List
                            if(type.equals("quiz")){
                                Quiz = document.getData().get(type).toString();
                                Log.d("quizText", "Question: "+Quiz);
                                String Q[] = Quiz.split("&");
                                String Subject = Q[0];
                                //Set the alignment
                                String AlignText = Q[1];
                                if(AlignText.equals("left ")){
                                    QuestionText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                                }else QuestionText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                                for(int i=2;i<Q.length ;i++) {
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
                                }//List Finished

                                //Show first question
                                SubjectTitle.setText(Subject);
                                QuestionText.setText(quizList.get(currentQuestion).question().replaceAll("@",System.getProperty("line.separator")));
                                adapter = new AnswerAdapter(ChallengeActivity.this, quizList.get(currentQuestion).answers(), new int[quizList.get(currentQuestion).answers().size()]);
                                listView.setAdapter(adapter);

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        Choice = i;

                                        //update Checkmark
                                        int checkS[] = new int[quizList.get(currentQuestion).answers().size()];
                                        checkS[i]=1;
                                        adapter = new AnswerAdapter(ChallengeActivity.this, quizList.get(currentQuestion).answers(), checkS);
                                        listView.setAdapter(adapter);
                                    }
                                });

                                SubmitBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if(Choice >-1 && BtnOn){
                                            //Disable Submit Button until next Question is on screen
                                            BtnOn = false;

                                            //Evaluate
                                            if (Choice == quizList.get(currentQuestion).rightAnswer()) {
                                                TotalXp += 10;
                                                ValidationText.setText("Correct Answer");
                                                lottie.setAnimation("welldone.json");
                                                lottie.playAnimation();
                                                //ValidationText.setTextColor(ColorStateList.valueOf(0xff228b22));
                                                correctMp.start();
                                                nbCorrectAnswers++;
                                            }else{
                                                ValidationText.setText("Incorrect Answer");
                                                lottie.setAnimation("wrong.json");
                                                lottie.playAnimation();
                                                //ValidationText.setTextColor(ColorStateList.valueOf(0xffff0000));
                                                wrongMp.start();
                                            }
                                            lottie.setVisibility(View.VISIBLE);
                                            ValidationText.setVisibility(View.VISIBLE);
                                            listView.setVisibility(View.INVISIBLE);
                                            QuestionText.setVisibility(View.INVISIBLE);
                                            XpCounter.setText(""+TotalXp);

                                            final Handler handler = new Handler(Looper.getMainLooper());
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (currentQuestion < quizList.size()-1) {
                                                        Choice = -1;

                                                        //Move on to the next question
                                                        currentQuestion++;
                                                        QuestionText.setText(quizList.get(currentQuestion).question());
                                                        adapter = new AnswerAdapter(ChallengeActivity.this, quizList.get(currentQuestion).answers(), new int[quizList.get(currentQuestion).answers().size()]);
                                                        listView.setAdapter(adapter);

                                                        ValidationText.setVisibility(View.INVISIBLE);
                                                        lottie.setVisibility(View.INVISIBLE);
                                                        listView.setVisibility(View.VISIBLE);
                                                        QuestionText.setVisibility(View.VISIBLE);
                                                    }else{
                                                        ValidationText.setVisibility(View.INVISIBLE);
                                                        lottie.setVisibility(View.INVISIBLE);
                                                        if(nbCorrectAnswers == quizList.size()){
                                                            ValidationText.setText("All answers are correct !");
                                                            //ValidationText.setTextSize(30);
                                                            lottie.setAnimation("amazing.json");
                                                            lottie.playAnimation();
                                                            //ValidationText.setTextColor(ColorStateList.valueOf(0xff228b22));
                                                        }else {
                                                            ValidationText.setText("You got " + nbCorrectAnswers + " questions out of " +quizList.size() + " right");
                                                            lottie.setAnimation("welldone.json");
                                                            lottie.playAnimation();
                                                            //ValidationText.setTextColor(ColorStateList.valueOf(0xff000000));
                                                        }
                                                        handler.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                ValidationText.setVisibility(View.VISIBLE);
                                                                lottie.setVisibility(View.VISIBLE);
                                                                SubmitBtn.setVisibility(View.INVISIBLE);
                                                                handler.postDelayed(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        UpdateDB_and_Leave();
                                                                    }
                                                                }, 5000);
                                                            }
                                                        }, 500);
                                                    }

                                                    //Enable Submit Button again
                                                    BtnOn = true;
                                                }
                                            }, 3000);
                                        }
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
            ImageView myBack = answer.findViewById(R.id.backimgquizz);
            TextView myText = answer.findViewById(R.id.AnswerText);
            RadioButton check = answer.findViewById(R.id.AnswerCheck);
            Typeface latobold = ResourcesCompat.getFont(context, R.font.lato_bold);
            Typeface lato = ResourcesCompat.getFont(context, R.font.lato);
            myBack.setBackground(getDrawable(R.drawable.ckeckboxon));
            //check.setBackground(getDrawable(R.drawable.success));
            //check.setScaleType(ImageView.ScaleType.FIT_START);
            check.setClickable(false);
            myText.setText(rText.get(position));
            if(rCheckStatus[position] == 1) {
                //check.setVisibility(View.VISIBLE);
                check.setChecked(true);
                myBack.setBackground(getDrawable(R.drawable.checkboxselected));
                myText.setTypeface(latobold);
            }
            else {
                //check.setVisibility(View.INVISIBLE);
                check.setChecked(false);
                myBack.setBackground(getDrawable(R.drawable.ckeckboxon));
                myText.setTypeface(lato);
            }
            return answer;
        }
    }
    public void UpdateDB_and_Leave(){
        db.collection("users")
                .document(mUser.getUid())
                .collection("Participation")
                .document("" + dateClicked.getYear() + "-" + dateClicked.getMonthValue())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                monthParticipation = (List<Integer>) document.get("mnthP");
                                monthParticipation.add(dateClicked.getDayOfMonth());
                                Map<String, Object> map = new HashMap<>();
                                map.put("mnthP", monthParticipation);
                                db.collection("users").document(mUser.getUid()).collection("Participation")
                                        .document("" + dateClicked.getYear() + "-" + dateClicked.getMonthValue())
                                        .set(map);
                                db.collection("users")
                                        .document(mUser.getUid())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        int userXp = Integer.valueOf(document.getData().get("xp").toString());
                                                        Map<String, Object> user = new HashMap<>();
                                                        user.put("xp", userXp + TotalXp);
                                                        user.put("first", document.getData().get("first").toString());
                                                        user.put("last", document.getData().get("last").toString());
                                                        user.put("avatar", document.getData().get("avatar").toString());
                                                        user.put("type", document.getData().get("type").toString());
                                                        user.put("level", document.getData().get("level").toString());
                                                        user.put("first_day", document.getData().get("first_day").toString());
                                                        //
                                                        // Add a new document with a generated ID
                                                        DocumentReference doc = db.collection("users").document(mUser.getUid());
                                                        doc.set(user);

                                                        Intent intent = new Intent(ChallengeActivity.this, CalenderActivity.class);
                                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                                        startActivity(intent);
                                                    }else{

                                                    }
                                                }
                                            };
                                        });
                            }else{

                            }
                        }
                    };
                });
    }
}