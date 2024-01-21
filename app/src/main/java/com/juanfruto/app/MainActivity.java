package com.juanfruto.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

interface IApiCallback {
    void onApiSuccess(InputStream inputStream);
    void onApiFailure(String error);
}

public class MainActivity extends AppCompatActivity implements IApiCallback {

    private Button send_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // View components
        send_button = (Button) findViewById(R.id.send_btn);
        send_button.setOnClickListener(v -> sendMessage("Hello World!"));

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_chat, menu);
        return true;
    }

    public void sendMessage(String message) {
        // Fetch API
        APIFetching apiFetching = new APIFetching(this);
        apiFetching.message(message);
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
}