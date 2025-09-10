package com.example.pathology;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText etIDnumber, etPassword;
    Button btnlogin;
    TextView crtaccount, ForgotPassword;

    private static final String URL_LOGIN = "https://pathologylabtrack.swuitapp.com/login.php";

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

        if (inputIDnumber.isEmpty() || inputPassword.isEmpty()) {
            Toast.makeText(this, "Please enter ID and Password", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equals("success")) {
                            int dbId = jsonObject.getInt("db_id");
                            String firstname = jsonObject.getString("firstname");
                            String lastname = jsonObject.getString("lastname");
                            String email = jsonObject.getString("email");

                            Toast.makeText(LoginActivity.this, "Welcome " + firstname, Toast.LENGTH_SHORT).show();

                            // ✅ Redirect to HomeScreen with user info
                            Intent intent = new Intent(LoginActivity.this, HomeScreen.class);
                            intent.putExtra("db_id", dbId);
                            intent.putExtra("firstname", firstname);
                            intent.putExtra("lastname", lastname);
                            intent.putExtra("email", email);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(LoginActivity.this, "Response parse error: " + response, Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                },
                error -> {
                    if (error.networkResponse != null) {
                        String responseBody = new String(error.networkResponse.data);
                        Toast.makeText(LoginActivity.this, "Server said: " + responseBody, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Network error: " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", inputIDnumber); // ✅ match with login.php
                params.put("password", inputPassword);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}