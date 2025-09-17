package com.example.pathology;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ConfrimReqActivity extends AppCompatActivity {

    Button btnConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_confrim_req);

        btnConfirm = findViewById(R.id.btnConfirm);

        btnConfirm.setOnClickListener(v -> {
            // Create dialog builder
            AlertDialog.Builder builder = new AlertDialog.Builder(ConfrimReqActivity.this);
            builder.setTitle("Confirm this REQUEST?");
            builder.setMessage("Review all information before proceeding.\n\n" +
                    "This action is final and cannot be undone.\n\n" +
                    "If everything is correct, check the box to confirm your transaction.");




            // "Yes" button
            builder.setPositiveButton("Yes", (dialog, which) -> {
                    Intent intent = new Intent(ConfrimReqActivity.this, ReceiptActivity.class);
                    startActivity(intent);
                
            });

            // "No" button
            builder.setNegativeButton("No", (dialog, which) -> {
                dialog.dismiss(); // Close dialog, stay on this screen
            });

            // Show dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }
}