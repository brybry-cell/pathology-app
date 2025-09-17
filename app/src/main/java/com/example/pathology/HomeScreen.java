package com.example.pathology;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeScreen extends AppCompatActivity {

    TextView tvName, tvEmail;
    ImageView menuIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        menuIcon = findViewById(R.id.menuIcon);

        // ✅ Load student info from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String firstname = prefs.getString("firstname", "User");
        String lastname = prefs.getString("lastname", "");
        String email = prefs.getString("email", "[Email]");

        tvName.setText("Hello, " + firstname + " " + lastname);
        tvEmail.setText(email);

        menuIcon.setOnClickListener(v -> {
            Intent intentMenu = new Intent(HomeScreen.this, MenuActivity.class);
            intentMenu.putExtra("user_id", prefs.getString("user_id", ""));
            startActivity(intentMenu);
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.bottom_home) {
                return true;
            } else if (id == R.id.bottom_request) {
                startActivity(new Intent(getApplicationContext(), RequestActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            } else if (id == R.id.bottom_return) {
                startActivity(new Intent(getApplicationContext(), ReturnActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            } else if (id == R.id.bottom_inbox) {
                startActivity(new Intent(getApplicationContext(), InboxActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            }
            return false;
        });
    }
}
