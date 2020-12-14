package com.example.to_do;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeechService;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    final String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Data").child(userUid);
    private Adapter adapter;
    private ArrayList<Data> list;
    TextToSpeech tts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new Adapter(this, list);
        recyclerView.setAdapter(adapter);
        tts = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                initializeSpeechRecognizer();
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot Snapshot) {
                for (DataSnapshot dataSnapshot : Snapshot.getChildren()) {
                    Data data = dataSnapshot.getValue(Data.class);
                    list.add(data);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void Task(View view) {
        Intent addtask = new Intent(MainActivity.this, AddTaskActivity.class);
        startActivity(addtask);
    }

    private void initializeSpeechRecognizer() {
        Intent voice = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        voice.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
        voice.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now\n Say create task/add task ");
        startActivityForResult(voice, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            List values = Arrays.asList(result.get(0).split(" "));

            if (values.contains("create task") || values.contains("add task") || values.contains("create") || values.contains("add") || values.contains("activity")) {
                Intent addTask = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivity(addTask);
                finish();
            } else {
                Intent voice = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                startActivityForResult(voice, 1);
            }
        }
    }

    @Override
    protected void onPause() {
        while (tts.isSpeaking()) {
            continue;
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        tts.shutdown();
        super.onDestroy();
    }
}