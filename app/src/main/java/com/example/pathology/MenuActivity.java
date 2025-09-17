package com.example.pathology;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MenuActivity extends AppCompatActivity {

    private TextView tvStudentID, tvStudentName, tvStudentEmail;
    private EditText etCurrentPassword, etNewPassword, etConfirmPassword;
    private Button btnResetPassword, btnHelp, btnExit;
    private ImageView back;

    private String userId, email;

    private static final String MENU_URL = "https://pathologylabtrack.swuitapp.com/menu.php"; // ✅ update with your server path

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Bind UI elements
        tvStudentID = findViewById(R.id.tvStudentID);
        tvStudentName = findViewById(R.id.tvStudentName);
        tvStudentEmail = findViewById(R.id.tvStudentEmail);
        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        btnHelp = findViewById(R.id.btnHelp);
        btnExit = findViewById(R.id.btnExit);
        back = findViewById(R.id.back);

        Intent intent = getIntent();
        userId = intent.getStringExtra("user_id");
        String firstname = intent.getStringExtra("firstname");
        String lastname = intent.getStringExtra("lastname");
        email = intent.getStringExtra("email");

// Optional: show initial data directly before fetch
        tvStudentName.setText("Name: " + firstname + " " + lastname);
        tvStudentEmail.setText("Email: " + email);
        tvStudentID.setText("ID: " + userId);


        // ✅ Load student info
        fetchUserInfo();

        // ✅ Reset Password button
        btnResetPassword.setOnClickListener(v -> {
            String currentPass = etCurrentPassword.getText().toString().trim();
            String newPass = etNewPassword.getText().toString().trim();
            String confirmPass = etConfirmPassword.getText().toString().trim();

            if (currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPass.equals(confirmPass)) {
                Toast.makeText(this, "New passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            resetPassword(currentPass, newPass);
        });

        // ✅ Help Button
        btnHelp.setOnClickListener(v ->
                Toast.makeText(this, "Contact admin for help.", Toast.LENGTH_SHORT).show()
        );

        // ✅ Exit Button
        btnExit.setOnClickListener(v -> {
            Intent loginIntent = new Intent(MenuActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        });

        // ✅ Back Button
        back.setOnClickListener(v -> finish());
    }

    private void fetchUserInfo() {
        StringRequest request = new StringRequest(Request.Method.POST, MENU_URL,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (!obj.getBoolean("error")) {
                            JSONObject user = obj.getJSONObject("user");

                            tvStudentID.setText("ID: " + user.getString("user_id"));
                            tvStudentName.setText("Name: " + user.getString("firstname") + " " + user.getString("lastname"));
                            tvStudentEmail.setText("Email: " + user.getString("email"));

                            email = user.getString("email");
                        } else {
                            Toast.makeText(this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, "Parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("action", "get_info");   // ✅ matches PHP
                params.put("user_id", userId != null ? userId : ""); // ✅ send correct key
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    private void resetPassword(String currentPass, String newPass) {
        StringRequest request = new StringRequest(Request.Method.POST, MENU_URL,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        Toast.makeText(this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Toast.makeText(this, "Parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("action", "reset_password"); // ✅ matches PHP
                params.put("user_id", userId);
                params.put("email", email);
                params.put("current_password", currentPass);
                params.put("new_password", newPass);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}
