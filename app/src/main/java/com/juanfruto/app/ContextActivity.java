package com.juanfruto.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class ContextActivity extends AppCompatActivity {

    private Spinner languageSpinner;
    private Spinner genderSpinner;
    public static final String TITLE = "title";
    public static final String USER_ROLE = "user_role";
    public static final String BOT_ROLE = "bot_role";
    public static final String CONTEXT = "context";
    public static final String LANGUAGE = "language";
    public static final String GENDER = "gender";
    private TextInputEditText textInputTitle;
    private TextInputEditText textInputUserRole;
    private TextInputEditText textInputBotRole;
    private TextInputEditText textInputContext;
    private String language;
    private String gender;
    private Button conversationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context);

        this.textInputTitle = (TextInputEditText) findViewById(R.id.ConversationTitle);
        this.textInputUserRole = (TextInputEditText) findViewById(R.id.ConversationUserRole);
        this.textInputBotRole = (TextInputEditText) findViewById(R.id.ConversationBotRole);
        this.textInputContext = (TextInputEditText) findViewById(R.id.ConversationContext);

        this.conversationButton = (Button) findViewById(R.id.conversationButton);
        this.conversationButton.setOnClickListener(this::goToChatActivity);

        // define spinners

        this.languageSpinner = (Spinner) findViewById(R.id.spinnerLanguage);
        this.genderSpinner = (Spinner) findViewById(R.id.spinnerGender);

        String[] languages = {
                "English",
                "Spanish",
                "German",
                "Italian",
                "Japanese",
                "Greek",
                "Mandarin",
                "French",
                "Arabic",
                "Turkish",
                "Indonesian",
                "Russian",
                "Polish",
                "Ukrainian"
        };
        String[] genders = {
                "Male",
                "Female"
        };

        ArrayAdapter<String> languageAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, languages);
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        languageSpinner.setAdapter(languageAdapter);

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genders);
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        genderSpinner.setAdapter(genderAdapter);

        // set the values by spinner values

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = parent.getItemAtPosition(position).toString();
                language = selectedOption;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(ContextActivity.this, "You should select one option", Toast.LENGTH_SHORT).show();
            }
        });

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = parent.getItemAtPosition(position).toString();
                gender = selectedOption;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(ContextActivity.this, "You should select one option", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void goToChatActivity(View view) {

        Intent intent = new Intent(this, Chat.class);

        intent.putExtra(TITLE, this.textInputTitle.getText().toString());
        intent.putExtra(USER_ROLE, this.textInputUserRole.getText().toString());
        intent.putExtra(BOT_ROLE, this.textInputBotRole.getText().toString());
        intent.putExtra(CONTEXT, this.textInputContext.getText().toString());
        intent.putExtra(LANGUAGE, this.language.toLowerCase());
        intent.putExtra(GENDER, this.gender.toLowerCase());

        startActivity(intent);
    }

}