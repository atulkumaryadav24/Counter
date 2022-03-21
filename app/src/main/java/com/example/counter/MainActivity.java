package com.example.counter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView SoberTextView,TotalTextView,PercentageTextView,StreakTextView;
    SharedPreferences sharedPreferences;
    String startDate,resetDate;
    int sober,total=0,percentage,streak,resetCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SoberTextView = findViewById(R.id.soberTextView);
        TotalTextView = findViewById(R.id.totalTextView);
        PercentageTextView = findViewById(R.id.percentageTextView);
        StreakTextView = findViewById(R.id.streakTextView);
        sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        if(sharedPreferences.getString("startDate","") == ""){
            setStartDate(getTimeNow());
            setResetDate(getTimeNow());
        }
        assureValues();
        showTextViewDetails();
    }
    //onClick
    public void onReset(View view){
        setResetDate(getTimeNow());
        int count = sharedPreferences.getInt("resetCount", 0);
        setResetCount(count);
        assureValues();
        showTextViewDetails();
        Toast.makeText(this, "Don't worry it's only a relapse.", Toast.LENGTH_SHORT).show();
    }
    public void onResetAll(View view){
        setStartDate(getTimeNow());
        setResetDate(getTimeNow());
        setResetCount(0);
        assureValues();
        showTextViewDetails();
        Toast.makeText(this, "Well! It's a fresh start!", Toast.LENGTH_SHORT).show();
    }
    //set
    private void setStartDate(String startDateString){
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("startDate", startDateString);
        myEdit.commit();
        getStartDate();
    }
    private void setResetCount(int resetCount){
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putInt("resetCount", resetCount);
        myEdit.commit();
        getResetCount();
    }
    private void setResetDate(String resetDateString){
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("resetDate", resetDateString);
        myEdit.commit();
        resetDate = sharedPreferences.getString("resetDate","");
    }
    //get
    private void getStartDate(){
        startDate = sharedPreferences.getString("startDate","");
    }
    private void getResetDate(){
        resetDate = sharedPreferences.getString("resetDate","");
    }
    private void getResetCount(){
        resetCount = sharedPreferences.getInt("resetCount",0);
    }
    private String getTimeNow(){
        return Calendar.getInstance().getTime().toString();
    }
    //calculate
    private int calculateDateDifference(String date1,String date2){
        Date d1 = new Date(date1);
        Date d2 = new Date(date2);
        long diffInTime = d1.getTime() - d2.getTime();
        int diffInDays = (int)(diffInTime / (1000 * 60 * 60 * 24)) % 365;
        return diffInDays;
    }
    private void calculateSoberTotalStreak(){
        total = calculateDateDifference(getTimeNow(),startDate);
        if(total - resetCount >= 0)
            sober = total - resetCount;
        else
            sober = 0;
        streak = calculateDateDifference(getTimeNow(),resetDate);
    }
    private void calculatePercentage() {
        if(total == 0)
            percentage = 0;
        else
            percentage = ( sober / total ) * 100;
    }
    //updates
    private void assureValues(){
        getStartDate();
        getResetDate();
        getResetCount();
        calculatePercentage();
        calculateSoberTotalStreak();
    }
    public void showTextViewDetails(){
        SoberTextView.setText(sober+" days");
        TotalTextView.setText(total+" days");
        StreakTextView.setText(streak+" days");
        PercentageTextView.setText(percentage+" %");
    }
}