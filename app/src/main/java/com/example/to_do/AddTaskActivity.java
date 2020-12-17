package com.example.to_do;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.text.ParseException;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddTaskActivity extends AppCompatActivity implements View.OnClickListener {


    Button btn_date, btn_time, btn_create;
    ImageView btn_speak;
    String timeTonotify;
    private int mYear, mMonth, mday, mHour, mMinute;

    private EditText title;
    private EditText desc;
    private static final int ADD_DATA = 1;
    DatabaseReference databaseReference;
    Data data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        title = (EditText) findViewById(R.id.title);
        desc = (EditText) findViewById(R.id.desc);
        btn_date = (Button) findViewById(R.id.btn_date);
        btn_time = (Button) findViewById(R.id.btn_time);
        btn_create = (Button) findViewById(R.id.btn_create);
        btn_speak = (ImageView) findViewById(R.id.btn_speak);

        btn_speak.setOnClickListener(this);
        btn_date.setOnClickListener(this);
        btn_time.setOnClickListener(this);
        btn_create.setOnClickListener(this);

        data = new Data();
        final String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Data").child(userUid);


    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        if (view == btn_speak) {
            Speak();
        } else if (view == btn_time) {
            selectTime();
        } else if (view == btn_date) {
            selectDate();
        } else {
            Create();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void Create() {

        Intent create = new Intent(AddTaskActivity.this, MainActivity.class);
        startActivity(create);

        //data = new Data();
        data.setTitle(title.getText().toString());
        data.setDesc(desc.getText().toString());
        data.setSelectdate(btn_date.getText().toString());
        data.setSelecttime(btn_time.getText().toString());
//        String tit = (title.getText().toString().trim());
//        String des = (desc.getText().toString().trim());
//        String date = (btn_date.getText().toString().trim());
//        String time = (btn_time.getText().toString().trim());


        databaseReference.push().setValue(data);

        Toast.makeText(getApplicationContext(), "Task created", Toast.LENGTH_SHORT).show();

        String value = (title.getText().toString().trim());
        String date = (btn_date.getText().toString().trim());
        String time = (btn_time.getText().toString().trim());

        data.setSelectdate(date);
        data.setTitle(value);
        data.setSelecttime(time);


        setAlarm(value, date, time);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setAlarm(String text, String date, String time) {


        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);



        Intent intent = new Intent(getApplicationContext(), AlarmBroadcast.class);
        intent.putExtra("event", text);
        intent.putExtra("time", date);
        intent.putExtra("date", time);


//        intent.putExtra("desc", des);
//        intent.putExtra("date", date);
//        intent.putExtra("time", time);



        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String dateandtime = date + " " + timeTonotify;
        DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
        try {
            Date date1 = formatter.parse(dateandtime);
            am.setExact(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        finish();

    }



    private void Speak() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, ADD_DATA);
        } else {
            Toast.makeText(this, "Your Device Doesn't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }


    private void selectTime() {

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                timeTonotify = i + ":" + i1;
                btn_time.setText(FormatTime(i, i1));
            }
        }, hour, minute, false);
        timePickerDialog.show();


    }


    private void selectDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                btn_date.setText(day + "-" + (month + 1) + "-" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }


    public String FormatTime(int hour, int minute) {

        String time;
        time = "";
        String formattedMinute;

        if (minute / 10 == 0) {
            formattedMinute = "0" + minute;
        } else {
            formattedMinute = "" + minute;
        }


        if (hour == 0) {
            time = "12" + ":" + formattedMinute + " AM";
        } else if (hour < 12) {
            time = hour + ":" + formattedMinute + " AM";
        } else if (hour == 12) {
            time = "12" + ":" + formattedMinute + " PM";
        } else {
            int temp = hour - 12;
            time = temp + ":" + formattedMinute + " PM";
        }


        return time;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case ADD_DATA:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (title.getText() != null && title.getText().toString().isEmpty()) {
                        title.setText(result.get(0));
                    } else {
                        desc.setText(result.get(0));
                    }
                }
                break;
        }
    }



}


