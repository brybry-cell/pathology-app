package com.example.pathology;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeScreen extends AppCompatActivity {

    TextView tvName, tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        // Get user data from login intent
        Intent intent = getIntent();
        String firstname = intent.getStringExtra("firstname");
        String lastname = intent.getStringExtra("lastname");
        String email = intent.getStringExtra("email");

        // Find the TextViews
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);

        // ✅ Reset them first (so they don’t overlap)
        tvName.setText("");
        tvEmail.setText("");

        // ✅ Show the actual logged-in name & email
        if (firstname != null && lastname != null) {
            tvName.setText("Hello, " + firstname + " " + lastname);
        } else {
            tvName.setText("Hello, User");
        }

        if (email != null) {
            tvEmail.setText(email);
        } else {
            tvEmail.setText("[Email]");
        }

        // Bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.bottom_home) {
                return true;
            } else if (id == R.id.bottom_request) {
                startActivity(new Intent(getApplicationContext(), RequestActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            } else if (id == R.id.bottom_return) {
                startActivity(new Intent(getApplicationContext(), ReturnActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            } else if (id == R.id.bottom_inbox) {
                startActivity(new Intent(getApplicationContext(), InboxActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            }
            return false;
        });
    }
}
