package com.example.pathology;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RequestActivity extends AppCompatActivity {

    private static final String MENU_URL = "https://pathologylabtrack.swuitapp.com/item.php";
    RecyclerView recyclerItems;
    Button viewSummaryBtn;
    List<Item> itemList = new ArrayList<>();
    ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        recyclerItems = findViewById(R.id.recyclerItems);
        viewSummaryBtn = findViewById(R.id.viewSummaryBtn);

        adapter = new ItemAdapter(itemList);
        recyclerItems.setLayoutManager(new GridLayoutManager(this, 2)); // 2 cards per row
        recyclerItems.setAdapter(adapter);

        fetchItems();

        viewSummaryBtn.setOnClickListener(v -> {
            ArrayList<Item> selectedItems = new ArrayList<>();
            for (Item item : itemList) {
                if (item.isSelected()) {
                    selectedItems.add(item);
                }
            }

            if (selectedItems.isEmpty()) {
                Toast.makeText(this, "No items selected", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(RequestActivity.this, SummaryActivity.class);
            intent.putExtra("selectedItems", selectedItems);
            startActivity(intent);
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.bottom_request);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

            if (id == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), HomeScreen.class));
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

    private void fetchItems() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, MENU_URL,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean error = jsonObject.getBoolean("error");
                        if (!error) {
                            JSONArray itemsArray = jsonObject.getJSONArray("items");
                            itemList.clear();
                            for (int i = 0; i < itemsArray.length(); i++) {
                                JSONObject item = itemsArray.getJSONObject(i);
                                String id = item.getString("item_id");
                                String name = item.getString("item_name");
                                int stock = item.getInt("stock");
                                itemList.add(new Item(id, name, stock));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(this, "No items found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error fetching data: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );
        Volley.newRequestQueue(this).add(stringRequest);
    }
}
