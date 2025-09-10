package com.example.pathology;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.nio.charset.StandardCharsets;

public class Register extends AppCompatActivity {

    EditText etLastname, etFirstname, etBirthday, etEmail;
    Button btnNext;
    TextView login;

    private static final String URL_REGISTER = "https://pathologylabtrack.swuitapp.com/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        etLastname = findViewById(R.id.etlastname);
        etFirstname = findViewById(R.id.etfirstname);
        etBirthday = findViewById(R.id.etbirthday);
        etEmail = findViewById(R.id.etEmail);
        btnNext = findViewById(R.id.btnNext);
        login = findViewById(R.id.login);

        btnNext.setOnClickListener(v -> sendRegisterRequest());
        login.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));
    }

    private void sendRegisterRequest() {
        String inputLastname = etLastname.getText().toString().trim();
        String inputFirstname = etFirstname.getText().toString().trim();
        String inputBirthday = etBirthday.getText().toString().trim();
        String inputEmail = etEmail.getText().toString().trim();

        if (inputLastname.isEmpty() || inputFirstname.isEmpty() || inputBirthday.isEmpty() || inputEmail.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Email validation
        if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        // Birthday must be YYYY-MM-DD
        if (!inputBirthday.matches("\\d{4}-\\d{2}-\\d{2}")) {
            Toast.makeText(this, "Birthday must be in YYYY-MM-DD format", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER,
                response -> {
                    // Server should return JSON string
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.optString("status", "error");
                        String message = jsonObject.optString("message", "Unknown error");

                        if ("success".equals(status)) {
                            int dbId = jsonObject.optInt("db_id", -1);
                            if (dbId == -1) {
                                Toast.makeText(Register.this, "Missing db_id from server", Toast.LENGTH_LONG).show();
                                return;
                            }
                            Intent intent = new Intent(Register.this, Signup.class);
                            intent.putExtra("db_id", dbId);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Register.this, "Error: " + message, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // Show raw response so we can debug server output
                        Toast.makeText(Register.this, "Response parse error: " + response, Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                },
                error -> {
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        Toast.makeText(Register.this, "Server said: " + responseBody, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Register.this, "Network error: " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("lastname", inputLastname);
                params.put("firstname", inputFirstname);
                params.put("birthday", inputBirthday); // already validated YYYY-MM-DD
                params.put("email", inputEmail);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
