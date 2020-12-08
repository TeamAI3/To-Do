package com.example.to_do;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddTaskActivity extends AppCompatActivity implements View.OnClickListener {

    TextView select_date, select_time;
    Button date, time;
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

        date = (Button) findViewById(R.id.date);
        time = (Button) findViewById(R.id.time);
        select_date = (TextView) findViewById(R.id.selectdate);
        select_time = (TextView) findViewById(R.id.selecttime);

        date.setOnClickListener(this);
        time.setOnClickListener(this);


        title = (EditText) findViewById(R.id.title);
        desc = (EditText) findViewById(R.id.desc);
        data = new Data();
        final String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Data").child(userUid);


    }

    public void Create(View view) {
        Intent create = new Intent(AddTaskActivity.this, MainActivity.class);
        startActivity(create);
        data.setTitle(title.getText().toString());
        data.setDesc(desc.getText().toString());
        data.setSelectdate(select_date.getText().toString());
        data.setSelecttime(select_time.getText().toString());


        databaseReference.push().setValue(data);

        Toast.makeText(getApplicationContext(), "Task created", Toast.LENGTH_SHORT).show();
    }


    public void Speak(View view) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, ADD_DATA);
        } else {
            Toast.makeText(this, "Your Device Doesn't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
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

    @Override
    public void onClick(View v) {
        if (v == date) {
            final Calendar calendar = Calendar.getInstance();
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mday = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    select_date.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                }
            }, mYear, mMonth, mday);
            datePickerDialog.show();
        }

        if (v == time) {
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfday, int minute) {
                    select_time.setText(hourOfday + ":" + minute);
                }
            }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }
}

