package com.example.pathology;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.ImageButton;
public class InboxMessageActivity extends AppCompatActivity {

    ImageButton back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inbox_message);

        back_button = findViewById(R.id.back_button);

        back_button.setOnClickListener(v-> startActivity(new Intent(this, InboxActivity.class)));
    }
}