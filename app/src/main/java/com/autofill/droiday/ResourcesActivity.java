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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ResourcesActivity extends AppCompatActivity {

    ListView listView;
    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore db;
    String lvl;
    int nbBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);

        List<String> bookNames = new ArrayList<String>();
        List<String> bookUrls = new ArrayList<String>();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        ImageView indic  = (ImageView) findViewById(R.id.indic);
        ImageView home_but = (ImageView) findViewById(R.id.home_but);
        ImageView cal_but = (ImageView) findViewById(R.id.cal_but);
        ImageView games_but = (ImageView) findViewById(R.id.games_but);
        TextView title = (TextView) findViewById(R.id.title_res);
        Button manuels = (Button) findViewById(R.id.books_btn);
        Button exam = (Button) findViewById(R.id.exams_btn);
        Button series = (Button) findViewById(R.id.series_btn);
        Button return_btn = (Button) findViewById(R.id.return_btn);
        listView = (ListView) findViewById(R.id.listview);

        title.setText("");
        manuels.setVisibility(View.VISIBLE);
        exam.setVisibility(View.VISIBLE);
        series.setVisibility(View.VISIBLE);
        return_btn.setVisibility(View.INVISIBLE);

        db.collection("users")
                .document(mUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                lvl = document.getData().get("lvl").toString();
                                db.collection("Books")
                                        .document(""+lvl)
                                        .collection("bk")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        bookNames.add(document.getData().get("name").toString());
                                                        bookUrls.add(document.getData().get("link").toString());
                                                        Log.d("BOOKS", "onComplete: "+ Arrays.toString(bookNames.toArray()));
                                                    }
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });

        nbBooks = bookNames.size();
        String booknames[] = (String[]) bookNames.toArray();
        String bookurl[] = (String[]) bookUrls.toArray();
        int[] bookstatus = new int[nbBooks];

        return_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title.setText("");
                manuels.setVisibility(View.VISIBLE);
                exam.setVisibility(View.VISIBLE);
                series.setVisibility(View.VISIBLE);
                return_btn.setVisibility(View.INVISIBLE);
            }
        });

        manuels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title.setText("Manuels :");
                manuels.setVisibility(View.INVISIBLE);
                exam.setVisibility(View.INVISIBLE);
                series.setVisibility(View.INVISIBLE);
                return_btn.setVisibility(View.VISIBLE);
            }
        });

        exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title.setText("Examens et épreuves :");
                manuels.setVisibility(View.INVISIBLE);
                exam.setVisibility(View.INVISIBLE);
                series.setVisibility(View.INVISIBLE);
                return_btn.setVisibility(View.VISIBLE);
            }
        });

        series.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title.setText("Series d'exercices :");
                manuels.setVisibility(View.INVISIBLE);
                exam.setVisibility(View.INVISIBLE);
                series.setVisibility(View.INVISIBLE);
                return_btn.setVisibility(View.VISIBLE);
            }
        });

        cal_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indic.setX(cal_but.getX() + 40);
            }
        });

        home_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indic.setX(home_but.getX()+40);
            }
        });

        games_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indic.setX(games_but.getX()+40);
            }
        });
    }

    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        String rTitle[];
        String rUrl[];
        int rStatus[];

        MyAdapter(Context c, String title[], String url[], int[] status) {
            super(c, R.layout.row, R.id.title, title);
            this.context = c;
            this.rTitle = title;
            this.rUrl = url;
            this.rStatus = status;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("ViewHolder") View row = layoutInflater.inflate(R.layout.row, parent, false);
            ImageView myBack = row.findViewById(R.id.backimg);
            TextView myTitle = row.findViewById(R.id.title);
            Typeface latobold = ResourcesCompat.getFont(context, R.font.lato_bold);
            myTitle.setText(rTitle[position]);
            if(rStatus[position] == 1) myBack.setBackground(getDrawable(R.drawable.blue_but_done));
            else myBack.setBackground(getDrawable(R.drawable.grey_but_download));
            myTitle.setTypeface(latobold);
            myTitle.setX(myBack.getX() + 8);
            return row;
        }


        }



}