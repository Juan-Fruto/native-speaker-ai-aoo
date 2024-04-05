package com.juanfruto.app;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    TextInputEditText textInputEmail;
    TextInputEditText textInputPassword;
    Button buttonLogin;
    TextView textViewSingUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.textInputEmail = (TextInputEditText) findViewById(R.id.textInputLoginEmail);
        this.textInputPassword = (TextInputEditText) findViewById(R.id.textInputLoginPassword);

        this.buttonLogin = (Button) findViewById(R.id.ButtonLogin);
        this.buttonLogin.setOnClickListener(this::goToChatActivity);

        this.textViewSingUp = (TextView) findViewById(R.id.textViewSingUp);
        this.textViewSingUp.setOnClickListener(this::goToRegisterActivity);

        // fct token
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log
                        String msg = "token " + token;
                        Log.d(TAG, msg);
                    }
                });
    }

    public void login(View view) {
        String email = textInputEmail.getText().toString();
        String password = textInputPassword.getText().toString();

        Log.d("debuging", "email:" + email + ", password:" + password);
    }

    public void goToChatActivity(View view) {
        Intent intent = new Intent(this, ContextActivity.class);
        startActivity(intent);
    }

    public void goToRegisterActivity(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}


