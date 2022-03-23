package com.autofill.droiday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CalenderActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener
{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private static LocalDate selectedDate;
    public static LocalDate today;
    public static LocalDate firstDay;
    public static List<Integer> monthParticipation;


    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore db;

    public LocalDate getToday(){
        return today;
    }
    public LocalDate getSelectedDate(){
        return selectedDate;
    }
    public LocalDate getFistDay(){
        return firstDay;
    }
    public List<Integer> getMonthParticipation(){
        return monthParticipation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        ImageView indic  = (ImageView) findViewById(R.id.indic2);
        ImageView home_but = (ImageView) findViewById(R.id.home_but2);
        ImageView cal_but = (ImageView) findViewById(R.id.cal_but2);
        ImageView games_but = (ImageView) findViewById(R.id.games_but2);
        //FireBase
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        db.collection("users")
                .document(mUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                firstDay = LocalDate.parse(document.getData().get("first_day").toString());
                                initWidgets();
                                selectedDate = LocalDate.now();
                                today = selectedDate;
                                setMonthViewAfterCheck();
                            }
                        }
                    }
                });


        home_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalenderActivity.this, HomePage.class);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(intent);
            }
        });

        games_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalenderActivity.this, ResourcesActivity.class);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(intent);
            }
        });
    }

    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    private void setMonthView()
    {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    public void setMonthViewAfterCheck(){
        db.collection("users")
                .document(mUser.getUid())
                .collection("Participation")
                .document("" + selectedDate.getYear() + "-" + selectedDate.getMonthValue())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                monthParticipation = (List<Integer>) document.get("mnthP");
                                Log.d("ffg", "onComplete: " + monthParticipation);
                                setMonthView();
                            }else{
                                monthParticipation=null;
                                setMonthView();
                            }
                        }
                    };
                });
    }

    private ArrayList<String> daysInMonthArray(LocalDate date)
    {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for(int i = 1; i <= 42; i++)
        {
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek)
            {
                daysInMonthArray.add("");
            }
            else
            {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return  daysInMonthArray;
    }

    private String monthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    public void previousMonthAction(View view)
    {
        selectedDate = selectedDate.minusMonths(1);
        setMonthViewAfterCheck();
    }

    public void nextMonthAction(View view)
    {
        selectedDate = selectedDate.plusMonths(1);
        setMonthViewAfterCheck();
    }


    @Override
    public void onItemClick(int position, String dayText)
    {
        if(!dayText.equals(""))
        {
            String message = "Selected Date " + dayText + " " + monthYearFromDate(selectedDate);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            LocalDate clickedDate = selectedDate.withDayOfMonth(Integer.valueOf(dayText));
            long day = Integer.valueOf(dayText);
            if (!monthParticipation.contains(day) && clickedDate.isAfter(firstDay.minusDays(1)) && clickedDate.isBefore(today.plusDays(1))) {
                Intent intent = new Intent(CalenderActivity.this, ChallengeActivity.class);
                intent.putExtra("dateClicked","" + clickedDate);
                startActivity(intent);
            }
        }
    }
}








