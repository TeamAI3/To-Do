package com.example.to_do;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {


    private EditText RegEmail, RegPassword;
    private Button RegButton;
    private TextView RegPageQuestion;
    private FirebaseAuth mAuth;
    private TextToSpeech tts;

    private ProgressDialog loader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        initializeTextToSpeech();

        mAuth = FirebaseAuth.getInstance();
        loader = new ProgressDialog(this);

        RegEmail = findViewById(R.id.RegistrationEmail);
        RegPassword = findViewById(R.id.RegistrationPassword);
        RegButton = findViewById(R.id.RegistrationButton);
        RegPageQuestion =findViewById(R.id.RegistrationPageQuestion);

        RegPageQuestion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        RegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = RegEmail.getText().toString().trim();
                String password = RegPassword.getText().toString().trim();


                if (TextUtils.isEmpty(email)) {
                    RegEmail.setError("Email is Required");
                    assistant("Please enter your Email address");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    RegPassword.setError("Password Required");
                    assistant("Please enter your password");
                    return;
                } else {
                    loader.setMessage("Registration in progress");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                loader.dismiss();
                            } else {
                                String error = task.getException().toString();
                                Toast.makeText(RegisterActivity.this, "Registration Failed" + error, Toast.LENGTH_SHORT).show();
                                loader.dismiss();
                            }

                        }
                    });
                }
            }
        });
    }
                private void initializeTextToSpeech () {
                    tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if (tts.getEngines().size() == 0) {
                                Toast.makeText(RegisterActivity.this, "Engine is not available", Toast.LENGTH_SHORT).show();
                            } else {
                                assistant(" Please Register here");
                            }
                        }
                    });
                }
                private void assistant (String msg){

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null, null);
                    } else {
                        tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null);
                    }
                }


}

