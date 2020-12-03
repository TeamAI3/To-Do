package com.example.to_do;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void Task(View view){
        Intent addtask= new Intent(MainActivity.this, AddTaskActivity.class);
        startActivity(addtask);
    }

}