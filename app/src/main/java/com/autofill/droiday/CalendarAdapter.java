package com.autofill.droiday;

import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>
{
    private final ArrayList<String> daysOfMonth;
    private final OnItemListener onItemListener;
    CalenderActivity calender = new CalenderActivity();
    private LocalDate today = calender.getToday();
    private LocalDate selecetedDate;

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
        return new CalendarViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        selecetedDate = calender.getSelectedDate();
        holder.dayOfMonth.setText(daysOfMonth.get(position));
        Log.d("TAAAAG", "" + selecetedDate + " *** " + today);
        if (!daysOfMonth.get(position).equals("")){
            if (selecetedDate.equals(today)) {
                if (today.getDayOfMonth() == position - 1) {
                    holder.dayOfMonth.setBackgroundTintList(ColorStateList.valueOf(0xff03a9f4));
                } else if (today.getDayOfMonth() > position - 1) {
                    holder.dayOfMonth.setBackgroundTintList(ColorStateList.valueOf(0xffaef5c8));
                }
            } else if (selecetedDate.isBefore(today)) {
                holder.dayOfMonth.setBackgroundTintList(ColorStateList.valueOf(0xffaef5c8));
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
