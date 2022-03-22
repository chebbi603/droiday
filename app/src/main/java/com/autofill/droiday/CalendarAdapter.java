package com.autofill.droiday;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.resources.MaterialResources;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>
{
    private final ArrayList<String> daysOfMonth;
    List<Integer> monthParticipation;
    private final OnItemListener onItemListener;
    private static Context context;
    CalenderActivity calender = new CalenderActivity();
    private LocalDate today = calender.getToday();
    private LocalDate firstDay = calender.getFistDay();
    private LocalDate selectedDate;
    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore db;
    Random rand;
    int rand_int, prev_rand_int=0;

    public CalendarAdapter(ArrayList<String> daysOfMonth, OnItemListener onItemListener)
    {
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        selectedDate = calender.getSelectedDate();
        rand = new Random();
        //Log.d("TAAAaaaaaaaaaaaaAG", "helllllllllllllllllloooooooooooooo" + selectedDate.getYear() + "-" + selectedDate.getMonthValue());
        /*db.collection("users")
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
                        }
                    }
                };
            });*/
        monthParticipation = calender.getMonthParticipation();
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        //layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        layoutParams.height = (int) (parent.getWidth() * 0.14285714285);//1/7
        //monthParticipation = calender.getMonthParticipation();
        return new CalendarViewHolder(view, onItemListener);
    }



    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        //monthParticipation = calender.getMonthParticipation();
        holder.dayOfMonth.setText(daysOfMonth.get(position));
        if (!daysOfMonth.get(position).equals("") && firstDay!= null && selectedDate!=null){
            selectedDate = selectedDate.withDayOfMonth(Integer.valueOf(daysOfMonth.get(position)));
            //Log.d("TAAAAG", ""+ selectedDate + " *** " + today);
            if (selectedDate.equals(today)) {
                holder.dayOfMonth.setBackgroundResource(R.drawable.today);
            } else if (selectedDate.isBefore(today) && selectedDate.isAfter(firstDay.minusDays(1))) {
                long diff = ChronoUnit.DAYS.between(firstDay, selectedDate);
                Log.d("TAG", "onBindViewHolder: " + monthParticipation + " " + selectedDate.getDayOfMonth());

                long day = selectedDate.getDayOfMonth();
                if (monthParticipation.contains(day)) {
                    holder.dayOfMonth.setBackgroundResource(R.drawable.success);
                }else{
                    rand_int = rand.nextInt(4);
                    while(rand_int == prev_rand_int){
                        rand_int = rand.nextInt(4);
                    }
                    //Log.d("RAAAAANDOM", selectedDate + "onComplete:"+ rand_int + " " + prev_rand_int);
                    prev_rand_int = rand_int;
                    if(rand_int==0) {
                        holder.dayOfMonth.setBackgroundResource(R.drawable.fail);
                        //Log.d("RAAAAANDOM", "fail");
                    }else if(rand_int==1) {
                        holder.dayOfMonth.setBackgroundResource(R.drawable.fail2);
                        //Log.d("RAAAAANDOM", "fail2");
                    }else if(rand_int==2) {
                        holder.dayOfMonth.setBackgroundResource(R.drawable.fail3);
                        //Log.d("RAAAAANDOM", "fail3");
                    }else if(rand_int==3){
                        holder.dayOfMonth.setBackgroundResource(R.drawable.fail4);
                        //Log.d("RAAAAANDOM", "fail4");
                    }

                }

                /*db.collection("users")
                        .document(mUser.getUid())
                        .collection("Participation")
                        .document(""+diff)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        holder.dayOfMonth.setBackgroundResource(R.drawable.success);
                                    }else{
                                        rand_int = rand.nextInt(4);
                                        while(rand_int == prev_rand_int){
                                            rand_int = rand.nextInt(4);
                                        }
                                        //Log.d("RAAAAANDOM", selectedDate + "onComplete:"+ rand_int + " " + prev_rand_int);
                                        prev_rand_int = rand_int;
                                        if(rand_int==0) {
                                            holder.dayOfMonth.setBackgroundResource(R.drawable.fail);
                                            //Log.d("RAAAAANDOM", "fail");
                                        }else if(rand_int==1) {
                                            holder.dayOfMonth.setBackgroundResource(R.drawable.fail2);
                                            //Log.d("RAAAAANDOM", "fail2");
                                        }else if(rand_int==2) {
                                            holder.dayOfMonth.setBackgroundResource(R.drawable.fail3);
                                            //Log.d("RAAAAANDOM", "fail3");
                                        }else if(rand_int==3){
                                            holder.dayOfMonth.setBackgroundResource(R.drawable.fail4);
                                            //Log.d("RAAAAANDOM", "fail4");
                                        }

                                    }
                                }
                            }
                        });*/

            } else {
                holder.dayOfMonth.setBackgroundTintList(ColorStateList.valueOf(0x00000000));
            }
        }else{
            holder.dayOfMonth.setBackgroundTintList(ColorStateList.valueOf(0x00000000));
        }
    }

    @Override
    public int getItemCount()
    {
        return daysOfMonth.size();
    }

    public interface  OnItemListener
    {
        void onItemClick(int position, String dayText);
    }
}
