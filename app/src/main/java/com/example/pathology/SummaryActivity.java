package com.example.pathology;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import android.widget.Button;
public class SummaryActivity extends AppCompatActivity {

    LinearLayout inboxContainer;
    Button btnConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        btnConfirm = findViewById(R.id.btnConfirm);
        inboxContainer = findViewById(R.id.inboxContainer);


        btnConfirm.setOnClickListener(v-> startActivity(new Intent(this, ConfrimReqActivity.class)));
        ArrayList<Item> selectedItems =
                (ArrayList<Item>) getIntent().getSerializableExtra("selectedItems");

        if (selectedItems != null) {
            for (Item item : selectedItems) {
                addCard(item);
            }
        }
    }

    private void addCard(Item item) {
        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 12);
        cardView.setLayoutParams(params);
        cardView.setRadius(12f);
        cardView.setCardElevation(6f);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setPadding(12, 30, 12, 30);

        ImageView img = new ImageView(this);
        img.setLayoutParams(new LinearLayout.LayoutParams(60, 60));
        img.setImageResource(R.drawable.ic_launcher_foreground);

        LinearLayout textLayout = new LinearLayout(this);
        textLayout.setOrientation(LinearLayout.VERTICAL);
        textLayout.setPadding(12, 0, 0, 0);

        TextView tvTitle = new TextView(this);
        tvTitle.setText(item.getName());
        tvTitle.setTextSize(16);
        tvTitle.setTextColor(getResources().getColor(R.color.black));

        TextView tvNo = new TextView(this);
        tvNo.setText("Item No. " + item.getId());
        tvNo.setTextSize(14);
        tvNo.setTextColor(getResources().getColor(R.color.grey));

        textLayout.addView(tvTitle);
        textLayout.addView(tvNo);

        layout.addView(img);
        layout.addView(textLayout);
        cardView.addView(layout);

        inboxContainer.addView(cardView);
    }
}
