package com.example.to_do;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {

    private EditText title;
    private static final int ADD_DATA = 1;
    DatabaseReference databaseReference;
    Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        title = (EditText) findViewById(R.id.title);
        data = new Data();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Data");

    }
    public void Create(View view) {
        Intent create = new Intent(AddTaskActivity.this, MainActivity.class);
        startActivity(create);
        data.setTitle(title.getText().toString());

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

                    }
                }
                break;
        }
    }
}