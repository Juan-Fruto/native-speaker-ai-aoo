package com.juanfruto.app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

interface IApiCallback {
    void onApiSuccess(InputStream inputStream);
    void onApiFailure(String error);
}

public class Chat extends AppCompatActivity implements IApiCallback {

    private Toolbar toolbar;
    private CircleImageView sendButton;

    private CircleImageView speechButton;
    private TextInputEditText textRecognized;

    private LinearLayout linearLayoutSendMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        String title = intent.getStringExtra(ContextActivity.TITLE);
        String language = intent.getStringExtra(ContextActivity.LANGUAGE);
        String gender = intent.getStringExtra(ContextActivity.GENDER);

        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.toolbar.setTitle(title);
        // View components
        speechButton = (CircleImageView) findViewById(R.id.buttonSpeech);
        speechButton.setOnClickListener(this::speak);

        sendButton = (CircleImageView) findViewById(R.id.buttonSend);
        sendButton.setOnClickListener(v -> {
            linearLayoutSendMessage.setVisibility(View.INVISIBLE);
            String message = Objects.requireNonNull(textRecognized.getText()).toString();
            sendMessage(message, gender, language);
        });

        textRecognized = (TextInputEditText) findViewById(R.id.textRecognized);

        linearLayoutSendMessage = (LinearLayout) findViewById(R.id.layoutSendMessage);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.get_back_arrow_button){
            finish();
        }
        return true;
    }

    public void sendMessage(String message, String gender, String language) {
        Toast.makeText(this, "The message has been sended", Toast.LENGTH_SHORT).show();
        // Fetch API
        APIFetching apiFetching = new APIFetching(this);
        apiFetching.message(message, gender, language);
    }

    @Override
    public void onApiSuccess(InputStream inputStream) {
        runOnUiThread(() -> {
            try {
                // save the audio as a temp file
                File tempFile = File.createTempFile("temp_audio", ".mp3", getCacheDir());
                FileOutputStream fos = new FileOutputStream(tempFile);
                byte[] buffer = new byte[1024];
                int read;

                while ((read = inputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, read);
                }
                fos.close();
                Log.d("Audio Path", tempFile.getAbsolutePath());

                // media player settings
                MediaPlayer mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC) // try speech later
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build());
                mediaPlayer.setDataSource(tempFile.getAbsolutePath());
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onApiFailure(String error) {
        runOnUiThread(() -> {
            Log.e("API Request", "Error en la solicitud: " +  error);
        });
    }

    // speech recognition
    public void speak(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Start speaking");
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100 && resultCode == RESULT_OK) {
            linearLayoutSendMessage.setVisibility(View.VISIBLE);
            textRecognized.setText(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0));
        }
    }
}