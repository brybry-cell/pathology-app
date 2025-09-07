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

public class Signup extends AppCompatActivity {

    EditText etIDNumber, etpassword, etConfirmPassword;
    Button btnRegister;
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        etIDNumber = findViewById(R.id.etIDNumber);
        etpassword = findViewById(R.id.etpassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        login = findViewById(R.id.login);

        btnRegister.setOnClickListener(v -> Register());
        login.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));

    }
    private void Register() {
        String inputIDnumber = etIDNumber.getText().toString().trim();
        String inputpassword = etpassword.getText().toString().trim();
        String inputconfirm = etConfirmPassword.getText().toString().trim();


        if (inputIDnumber.isEmpty() || inputpassword.isEmpty() || inputconfirm.isEmpty()) {
                Toast.makeText(this, "Fill in all fields", Toast.LENGTH_SHORT).show();
        } else if (!inputpassword.equals(inputconfirm)) {
            Toast.makeText(this, "Password do not match!", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, HomeScreen.class);
            startActivity(intent);
            finish();
        }
    }
}