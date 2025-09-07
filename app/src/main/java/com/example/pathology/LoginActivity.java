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

public class LoginActivity extends AppCompatActivity {

    EditText etIDnumber, etPassword;
    Button btnlogin;
    TextView crtaccount, ForgotPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        etIDnumber = findViewById(R.id.etIDnumber);
        etPassword = findViewById(R.id.etPassword);
        btnlogin = findViewById(R.id.btnlogin);
        crtaccount = findViewById(R.id.crtaccount);
        ForgotPassword = findViewById(R.id.ForgotPassword);

        btnlogin.setOnClickListener(v -> loginUser());
        crtaccount.setOnClickListener(v -> startActivity(new Intent(this, Register.class)));
        ForgotPassword.setOnClickListener(v -> startActivity(new Intent(this, ForgotActivity.class)));



    }
    private void loginUser() {
        String inputIDnumber = etIDnumber.getText().toString().trim();
        String inputPassword = etPassword.getText().toString().trim();

        if (inputIDnumber.equals("05-2425-000240") && inputPassword.equals("123")) {
            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, HomeScreen.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid ID or Password", Toast.LENGTH_SHORT).show();
        }
    }
}