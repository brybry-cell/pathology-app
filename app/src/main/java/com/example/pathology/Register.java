package com.example.pathology;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Register extends AppCompatActivity {

    EditText etlastname, etfirstname, etbirthday, etEmail;
    Button btnNext;
    TextView login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        etlastname = findViewById(R.id.etlastname);
        etfirstname = findViewById(R.id.etfirstname);
        etbirthday = findViewById(R.id.etbirthday);
        etEmail = findViewById(R.id.etEmail);
        btnNext = findViewById(R.id.btnNext);
        login = findViewById(R.id.login);

        btnNext.setOnClickListener(v -> Next());
        login.setOnClickListener(v-> startActivity(new Intent(this, LoginActivity.class)));

    }
    private void Next() {
        String inputlastname = etlastname.getText().toString().trim();
        String inputfirstname = etfirstname.getText().toString().trim();
        String inputbirthday = etbirthday.getText().toString().trim();
        String inputemail = etEmail.getText().toString().trim();

        if (inputlastname.isEmpty() || inputfirstname.isEmpty() || inputbirthday.isEmpty() || inputemail.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, Signup.class);
            startActivity(intent);
            finish();
        }
    }
}