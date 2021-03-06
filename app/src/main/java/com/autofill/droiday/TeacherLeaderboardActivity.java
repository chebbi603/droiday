package com.autofill.droiday;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TeacherLeaderboardActivity extends AppCompatActivity {

    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    FirebaseUser mUser;

    ListView listView;
    List<String> Names = new ArrayList<>();
    List<String> Scores = new ArrayList<>();
    LeaderboardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_leaderboard);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        listView = findViewById(R.id.listview);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        db.collection("users")
                .orderBy("rep", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Integer> l = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Names.add(document.getData().get("first").toString() + " " +document.getData().get("last").toString());
                                Scores.add(document.getData().get("rep").toString());
                                if(document.getId().equals(mUser.getUid())){
                                    l.add(1);
                                }else l.add(0);
                            }
                            adapter = new LeaderboardAdapter(TeacherLeaderboardActivity.this, Names, Scores, l);
                            listView.setAdapter(adapter);
                        }
                    }
                });
    }
    class LeaderboardAdapter extends ArrayAdapter<String> {

        Context context;
        List<String> rText;
        List<String> rScore;
        List<Integer> rMe;

        LeaderboardAdapter(Context c, List<String> text, List<String> score, List<Integer> me) {
            super(c, R.layout.leaderboard_row, R.id.LeaderName, text);
            this.context = c;
            this.rText = text;
            this.rScore = score;
            this.rMe = me;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("ViewHolder") View answer = layoutInflater.inflate(R.layout.leaderboard_row, parent, false);
            ImageView myBack = answer.findViewById(R.id.backgroundLeader);
            ImageView myMedal = answer.findViewById(R.id.medal);
            TextView myName = answer.findViewById(R.id.LeaderName);
            TextView rank = answer.findViewById(R.id.rank);
            TextView myScore = answer.findViewById(R.id.score);
            Typeface latobold = ResourcesCompat.getFont(context, R.font.lato_bold);
            if(position == 0) {
                myMedal.setBackground(getDrawable(R.drawable.gold_medal));
                rank.setText("");
            }
            else if(position == 1){
                myMedal.setBackground(getDrawable(R.drawable.silver_medal));
                rank.setText("");
            }
            else if(position == 2) {
                myMedal.setBackground(getDrawable(R.drawable.bronze_medal));
                rank.setText("");
            }
            else {
                myMedal.setBackground(getDrawable(R.drawable.no_medal));
                int pos = position+1;
                rank.setText(""+pos);
            }
            myBack.setBackground(getDrawable(R.drawable.them_leader));
            myName.setTextColor(Color.BLACK);
            myScore.setTextColor(Color.BLACK);
            if(rMe.get(position) == 1){
                myBack.setBackground(getDrawable(R.drawable.me_leader));
                myName.setTextColor(Color.WHITE);
                myScore.setTextColor(Color.WHITE);
            }
            myName.setTypeface(latobold);
            myScore.setTypeface(latobold);
            myName.setText(rText.get(position));
            myScore.setText(rScore.get(position));
            return answer;
        }
    }
}