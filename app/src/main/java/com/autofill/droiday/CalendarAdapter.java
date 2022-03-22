package com.autofill.droiday;

import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>
{
    private final ArrayList<String> daysOfMonth;
    private final OnItemListener onItemListener;
    CalenderActivity calender = new CalenderActivity();
    private LocalDate today = calender.getToday();
    private LocalDate firstDay = calender.getFistDay();
    private LocalDate selectedDate;
    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore db;

    public CalendarAdapter(ArrayList<String> daysOfMonth, OnItemListener onItemListener)
    {
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
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
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        return new CalendarViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        selectedDate = calender.getSelectedDate();
        holder.dayOfMonth.setText(daysOfMonth.get(position));
        if (!daysOfMonth.get(position).equals("") && firstDay!= null && selectedDate!=null){
            selectedDate = selectedDate.withDayOfMonth(position-1);
            Log.d("TAAAAG", ""+ selectedDate + " *** " + today);
            if (selectedDate.equals(today)) {
                holder.dayOfMonth.setBackgroundTintList(ColorStateList.valueOf(0xff03a9f4));
            } else if (selectedDate.isBefore(today) && selectedDate.isAfter(firstDay.minusDays(1))) {
                long diff = ChronoUnit.DAYS.between(firstDay, selectedDate);
                db.collection("users")
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
                                        holder.dayOfMonth.setBackgroundTintList(ColorStateList.valueOf(0xffaef5c8));
                                    }else{
                                        holder.dayOfMonth.setBackgroundTintList(ColorStateList.valueOf(0xfff5aec8));
                                    }
                                }
                            }
                        });

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
