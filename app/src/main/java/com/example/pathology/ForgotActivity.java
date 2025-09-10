package com.example.pathology;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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

public class ForgotActivity extends AppCompatActivity {

    EditText etEmail, etOTP, etPassword, etConfirmPassword;
    Button btnUpdate;

    private static final String URL_FORGOT = "https://pathologylabtrack.swuitapp.com/forgotpassword.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot);

        etEmail = findViewById(R.id.etEmail);
        etOTP = findViewById(R.id.etOTP);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnUpdate = findViewById(R.id.btnUpdate);

        btnUpdate.setOnClickListener(v -> sendForgotRequest());
    }

    private void sendForgotRequest() {
        String inputEmail = etEmail.getText().toString().trim();
        String inputOTP = etOTP.getText().toString().trim();
        String inputPassword = etPassword.getText().toString().trim();
        String inputConfirm = etConfirmPassword.getText().toString().trim();

        if (inputEmail.isEmpty() || inputOTP.isEmpty() || inputPassword.isEmpty() || inputConfirm.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!inputPassword.equals(inputConfirm)) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_FORGOT,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equals("success")) {
                            Toast.makeText(ForgotActivity.this, "✅ " + message, Toast.LENGTH_LONG).show();
                            finish(); // go back to login after success
                        } else {
                            Toast.makeText(ForgotActivity.this, "❌ " + message, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(ForgotActivity.this, "Response parse error: " + response, Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                },
                error -> {
                    if (error.networkResponse != null) {
                        String responseBody = new String(error.networkResponse.data);
                        Toast.makeText(ForgotActivity.this, "Server said: " + responseBody, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ForgotActivity.this, "Network error: " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", inputEmail);
                params.put("otp", inputOTP);
                params.put("password", inputPassword);
                params.put("confirm_password", inputConfirm);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
