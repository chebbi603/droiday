package com.autofill.droiday;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    FirebaseFirestore db;
    ListView listView;
    List<String> Names = new ArrayList<>();
    List<String> Scores = new ArrayList<>();
    LeaderboardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        listView = findViewById(R.id.listview);

        db = FirebaseFirestore.getInstance();

        db.collection("users")
                .orderBy("xp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Names.add(document.getData().get("first").toString() + " " +document.getData().get("last").toString());
                                Scores.add(document.getData().get("xp").toString());
                                adapter = new LeaderboardAdapter(LeaderboardActivity.this, Names, Scores);
                                listView.setAdapter(adapter);
                            }
                        }
                    }
                });
    }
    class LeaderboardAdapter extends ArrayAdapter<String> {

        Context context;
        List<String> rText;
        List<String> rScore;

        LeaderboardAdapter(Context c, List<String> text, List<String> score) {
            super(c, R.layout.leaderboard_row, R.id.LeaderName, text);
            this.context = c;
            this.rText = text;
            this.rScore = score;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("ViewHolder") View answer = layoutInflater.inflate(R.layout.leaderboard_row, parent, false);
            ImageView myBack = answer.findViewById(R.id.backgroundLeader);
            TextView myName = answer.findViewById(R.id.LeaderName);
            TextView myScore = answer.findViewById(R.id.score);
            Typeface latobold = ResourcesCompat.getFont(context, R.font.lato_bold);
            myBack.setBackground(getDrawable(R.drawable.redtitle));
            myName.setTypeface(latobold);
            myScore.setTypeface(latobold);
            myName.setText(rText.get(position));
            myScore.setText(rScore.get(position));
            return answer;
        }
    }
}