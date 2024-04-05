package com.juanfruto.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.juanfruto.utils.ValidationResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText textInputName;
    private TextInputEditText textInputEmail;
    private TextInputEditText textInputPassword;
    private TextInputEditText textInputConfirmPassword;
    private Button btnSingUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.textInputName = (TextInputEditText) findViewById(R.id.textInputSingUpName);
        this.textInputEmail = (TextInputEditText) findViewById(R.id.textInputSingUpEmail);
        this.textInputPassword = (TextInputEditText) findViewById(R.id.textInputSingUpPassword);
        this.textInputConfirmPassword = (TextInputEditText) findViewById(R.id.textInputSingUpConfirmPassword);

        this.btnSingUp = (Button) findViewById(R.id.buttonSingUp);
        this.btnSingUp.setOnClickListener(this::singUp);
    }

    public void singUp(View view) {
        String name = this.textInputName.getText().toString();
        String email = this.textInputEmail.getText().toString();
        String password = this.textInputPassword.getText().toString();
        String confirmPassword = this.textInputConfirmPassword.getText().toString();

        ValidationResult validations = this.validations(name, email, password, confirmPassword);

        if(validations.getSuccess()) {
            Toast.makeText(this, "Validations passed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, validations.getErrors().get(0), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private ValidationResult validations(String name, String email, String password, String confirmPassword) {
        List<String> errors = new ArrayList<>();
        boolean success;

        if(! name.isEmpty() && ! email.isEmpty() && ! password.isEmpty() && ! confirmPassword.isEmpty()){
            if(!(this.isEmailValid(email))) errors.add("Invalid email format");
            if(!(password.equals(confirmPassword))) errors.add("Passwords do not match");
            if(password.length() <= 5) errors.add("Password must be at least 5 characters");

        } else {
            errors.add("All fields are required");
        }

        success = errors.size() > 0 ? false : true;

        return new ValidationResult(success, errors);
    }
}