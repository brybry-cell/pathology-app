package com.example.pathology;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.EdgeToEdge;

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

    boolean isPasswordVisible = false;
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

        etPassword.setOnTouchListener((v, event) -> {
            final int DRAWABLE_END = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (etPassword.getRight() - etPassword.getCompoundDrawables()[DRAWABLE_END].getBounds().width())) {
                    if (isPasswordVisible) {
                        etPassword.setTransformationMethod(android.text.method.PasswordTransformationMethod.getInstance());
                        etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.hidden, 0);
                        isPasswordVisible = false;
                    } else {
                        etPassword.setTransformationMethod(android.text.method.HideReturnsTransformationMethod.getInstance());
                        etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.nothidden, 0);
                        isPasswordVisible = true;
                    }
                    etPassword.setSelection(etPassword.getText().length());
                    return true;
                }
            }
            return false;
        });
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
                            String firstname = jsonObject.getString("firstname");
                            String lastname = jsonObject.getString("lastname");
                            String email = jsonObject.getString("email");

                            // ✅ Save to SharedPreferences
                            SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("user_id", inputIDnumber);
                            editor.putString("firstname", firstname);
                            editor.putString("lastname", lastname);
                            editor.putString("email", email);
                            editor.apply();

                            Toast.makeText(LoginActivity.this, "Welcome " + firstname, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(LoginActivity.this, HomeScreen.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Response parse error", Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(LoginActivity.this, "Network error: " + error, Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", inputIDnumber);
                params.put("password", inputPassword);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
