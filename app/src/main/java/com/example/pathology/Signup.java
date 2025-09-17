package com.example.pathology;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
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

public class Signup extends AppCompatActivity {

    EditText etUserId, etPassword, etConfirmPassword;
    Button btnRegister;
    TextView login;

    private boolean isPasswordVisible = true;
    private static final String URL_SIGNUP = "https://pathologylabtrack.swuitapp.com/signup.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        etUserId = findViewById(R.id.etIDNumber);
        etPassword = findViewById(R.id.etpassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        login = findViewById(R.id.login);

        btnRegister.setOnClickListener(v -> sendSignupRequest());
        login.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));

        etPassword.setOnTouchListener((v, event) -> {
            final int DRAWABLE_END = 2; // right side drawable index

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (etPassword.getRight() - etPassword.getCompoundDrawables()[DRAWABLE_END].getBounds().width())) {

                    if (isPasswordVisible) {
                        // Hide password
                        etPassword.setTransformationMethod(android.text.method.PasswordTransformationMethod.getInstance());
                        etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.hidden, 0);
                        isPasswordVisible = false;
                    } else {
                        // Show password
                        etPassword.setTransformationMethod(android.text.method.HideReturnsTransformationMethod.getInstance());
                        etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.nothidden, 0);
                        isPasswordVisible = true;
                    }

                    etPassword.setSelection(etPassword.getText().length()); // keep cursor at end
                    return true;
                }
            }
            return false;
        });

        etConfirmPassword.setOnTouchListener((v, event) -> {
            final int DRAWABLE_END = 2; // right side drawable index

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (etPassword.getRight() - etPassword.getCompoundDrawables()[DRAWABLE_END].getBounds().width())) {

                    if (isPasswordVisible) {
                        // Hide password
                        etPassword.setTransformationMethod(android.text.method.PasswordTransformationMethod.getInstance());
                        etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.hidden, 0);
                        isPasswordVisible = false;
                    } else {
                        // Show password
                        etPassword.setTransformationMethod(android.text.method.HideReturnsTransformationMethod.getInstance());
                        etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.nothidden, 0);
                        isPasswordVisible = true;
                    }

                    etPassword.setSelection(etPassword.getText().length()); // keep cursor at end
                    return true;
                }
            }
            return false;
        });
    }

    private boolean isValidPassword(String password) {
        // Min 12 chars, at least 1 letter, 1 number, 1 special character
        String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{12,}$";
        return password.matches(passwordPattern);
    }

    private void sendSignupRequest() {
        String inputUserId = etUserId.getText().toString().trim();
        String inputPassword = etPassword.getText().toString().trim();
        String inputConfirm = etConfirmPassword.getText().toString().trim();

        if (inputUserId.isEmpty() || inputPassword.isEmpty() || inputConfirm.isEmpty()) {
            Toast.makeText(this, "Fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!inputPassword.equals(inputConfirm)) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidPassword(inputPassword)) {
            Toast.makeText(this, "Password must be at least 12 characters, include letters, numbers, and 1 special character.", Toast.LENGTH_LONG).show();
            return;
        }

        int dbId = getIntent().getIntExtra("db_id", -1);
        if (dbId == -1) {
            Toast.makeText(this, "Missing db_id. Please return to the previous step.", Toast.LENGTH_LONG).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SIGNUP,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.optString("status", "error");
                        String message = jsonObject.optString("message", "Unknown error");

                        if ("success".equals(status)) {
                            Toast.makeText(Signup.this, "Signup completed!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Signup.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Signup.this, "Error: " + message, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // Show raw response for debugging
                        Toast.makeText(Signup.this, "Response parse error: " + response, Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                },
                error -> {
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        Toast.makeText(Signup.this, "Server said: " + responseBody, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Signup.this, "Network error: " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("db_id", String.valueOf(dbId));
                params.put("user_id", inputUserId);
                params.put("password", inputPassword);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
